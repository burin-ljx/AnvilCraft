package dev.dubhe.anvilcraft.mixin;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.HollowMagnetBlock;
import dev.dubhe.anvilcraft.init.ModBlockTags;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItemTags;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.IFireReforging;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mixin(ItemEntity.class)
abstract class ItemEntityMixin extends Entity {
    @Shadow
    public abstract ItemStack getItem();

    @Shadow
    @Nullable public abstract Entity getOwner();

    @Shadow
    public abstract void setItem(ItemStack stack);

    @Shadow
    @javax.annotation.Nullable private UUID target;

    @Shadow
    protected abstract void setUnderwaterMovement();

    public ItemEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(
            method = "tick",
            at = @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/item/ItemEntity;"
                                    + "getDeltaMovement()Lnet/minecraft/world/phys/Vec3;"
                    )
    )
    private @NotNull Vec3 slowDown(ItemEntity instance) {
        Vec3 vec3 = instance.getDeltaMovement();
        double dy = 1;
        if (this.getItem().is(ModItems.LEVITATION_POWDER.get())) dy *= -0.005;
        if (this.level().getBlockState(this.blockPosition()).is(ModBlocks.HOLLOW_MAGNET_BLOCK.get())) dy *= 0.2;
        if (this.getItem().is(ModItems.NEGATIVE_MATTER_NUGGET.get()) ||
                this.getItem().is(ModItems.NEGATIVE_MATTER.get()) ||
                this.getItem().is(ModBlocks.NEGATIVE_MATTER_BLOCK.asItem())){
            if (this.position().y <= this.level().getMaxBuildHeight())
                if (vec3.y < 0) dy *= -1;
        }
        return new Vec3(vec3.x, vec3.y * dy, vec3.z);
    }

    @Unique private boolean anvilcraft$needMagnetization = true;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void magnetization(CallbackInfo ci) {
        if (this.getServer() == null) return;
        ItemStack itemStack = this.getItem();
        if (!itemStack.is(Items.IRON_INGOT)) return;
        BlockState blockState = this.level().getBlockState(this.blockPosition());
        if (!blockState.is(ModBlocks.HOLLOW_MAGNET_BLOCK.get()) || blockState.getValue(HollowMagnetBlock.LIT)) return;
        if (this.getOwner() == null || !(this.getOwner() instanceof ServerPlayer)) return;
        if (itemStack.getCount() != 1) return;
        if (!this.anvilcraft$needMagnetization) return;
        this.anvilcraft$needMagnetization = false;
        if (this.level().random.nextInt(100) <= 10) {
            this.setItem(new ItemStack(ModItems.MAGNET_INGOT.get()));
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void voidResistant(CallbackInfo ci) {
        if (!this.getItem().is(ModItemTags.VOID_RESISTANT)) return;
        if(this.getY() < this.level().getMinBuildHeight() + 1) {
            double dy = (this.level().getMinBuildHeight() - this.getY()) * 0.01;
            dy += this.getDeltaMovement().y * -0.1;
            this.addDeltaMovement(new Vec3(0, 0.04 + dy, 0));
        }
    }

    @Unique private static final Map<Block, Integer> REPAIR_EFFICIENCY = new HashMap<>();

    static {
        REPAIR_EFFICIENCY.put(Blocks.FIRE, 2);
        REPAIR_EFFICIENCY.put(Blocks.SOUL_FIRE, 5);
        REPAIR_EFFICIENCY.put(Blocks.LAVA, 10);
        REPAIR_EFFICIENCY.put(Blocks.LAVA_CAULDRON, 10);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void fireReforging(CallbackInfo ci) {
        ItemStack item = this.getItem();
        if (!item.isEmpty() && item.getItem() instanceof IFireReforging) {
            if (!this.getItem().isDamaged()) return;
            Block block = this.level().getBlockState(this.blockPosition()).getBlock();
            if (REPAIR_EFFICIENCY.containsKey(block)) {
                this.getItem().setDamageValue(this.getItem().getDamageValue() - REPAIR_EFFICIENCY.get(block));
            }
        }
    }

    @Redirect(method = "tick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/entity/item/ItemEntity;" +
            "moveTowardsClosestSpace(DDD)V")
    )
    private void neutroniumNoBouncingOut(ItemEntity instance, double x, double y, double z) {
        ItemStack item = this.getItem();
        if (item.is(ModItems.NEUTRONIUM_INGOT)){
            return;
        }
        this.moveTowardsClosestSpace(x, y, z);
    }

    @Redirect(method = "tick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/entity/item/ItemEntity;" +
            "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V")
    )
    private void neutroniumMove(ItemEntity instance, MoverType moverType, Vec3 motion) {
        ItemStack item = this.getItem();
        if (!item.is(ModItems.NEUTRONIUM_INGOT)){
            instance.move(moverType, motion);
            return;
        }

        AnvilCraft.LOGGER.debug("timestamp: {} onGround:{} yPos: {} motion: {}", this.level().getGameTime(),
            this.onGround() ,this.position().y, this.getDeltaMovement());

        this.level().getProfiler().push("move");
        //代替原版move方法中的collide调用
        AABB box = this.getBoundingBox().expandTowards(motion);
        int x1 = Mth.floor(box.minX - 1.0E-7) - 1;
        int x2 = Mth.floor(box.maxX + 1.0E-7) + 1;
        int y1 = Mth.floor(box.minY - 1.0E-7) - 1;
        int y2 = Mth.floor(box.maxY + 1.0E-7) + 1;
        int z1 = Mth.floor(box.minZ - 1.0E-7) - 1;
        int z2 = Mth.floor(box.maxZ + 1.0E-7) + 1;
        List<VoxelShape> shapes = new ArrayList<>();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = x1; x < x2; x++) {
            for (int y = y1; y < y2; y++) {
                for (int z = z1; z < z2; z++) {
                    pos.set(x, y, z);
                    BlockState blockState = this.level().getBlockState(pos);
                    //只检测带有特定标签的方块的碰撞
                    if(blockState.is(ModBlockTags.NEUTRONIUM_CANNOT_PASS_THROUGH)) {
                        shapes.add(blockState.getCollisionShape(this.level(), pos).move(x, y, z));
                    }
                }
            }
        }
        AnvilCraft.LOGGER.debug("size: {}", shapes.size());
        Vec3 motion2 = collideWithShapes(motion, this.getBoundingBox(), shapes);
        if (motion2.lengthSqr() > 1.0E-7) {
            this.setPos(this.getX() + motion2.x, this.getY() + motion2.y, this.getZ() + motion2.z);
        }

//        AnvilCraft.LOGGER.debug("Motion before collide: {}, Motion after collide: {}",
//            motion, motion2);

        this.level().getProfiler().popPush("rest");
        // 处理一些原版move方法中，对ItemEntity有必要的后续操作
        boolean collisionX = !Mth.equal(motion2.x, motion.x);
        boolean collisionZ = !Mth.equal(motion2.z, motion.z);
        this.horizontalCollision = collisionX || collisionZ;
        this.verticalCollision = motion2.y != motion.y;
        this.verticalCollisionBelow = this.verticalCollision && motion.y < (double)0.0F;
        this.setOnGroundWithMovement(this.verticalCollisionBelow, motion2);
        BlockPos blockpos = this.getOnPosLegacy();
        BlockState blockState = this.level().getBlockState(blockpos);
        if (this.horizontalCollision) {
            Vec3 vec31 = this.getDeltaMovement();
            this.setDeltaMovement(collisionX ? 0.0 : vec31.x, vec31.y, collisionZ ? 0.0 : vec31.z);
        }
        Block block = blockState.getBlock();
        if (motion2.y != motion.y) {
            block.updateEntityAfterFallOn(this.level(), this);
        }
        if (this.onGround()) {
            block.stepOn(this.level(), blockpos, blockState, this);
        }
        this.tryCheckInsideBlocks();
        float f = this.getBlockSpeedFactor();
        this.setDeltaMovement(this.getDeltaMovement().multiply(f, 1.0, f));

        this.level().getProfiler().pop();
    }

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void explosionProof(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!this.getItem().isEmpty()
                && this.getItem().is(ModItemTags.EXPLOSION_PROOF)
                && source.is(DamageTypeTags.IS_EXPLOSION)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getBlockPosBelowThatAffectsMyMovement", at = @At("HEAD"), cancellable = true)
    private void slidingRailProgress(CallbackInfoReturnable<BlockPos> cir) {
        BlockState blockState = this.level().getBlockState(this.getOnPos(0.1f));
        if (blockState.is(ModBlocks.SLIDING_RAIL) || blockState.is(ModBlocks.SLIDING_RAIL_STOP)) {
            cir.setReturnValue(this.getOnPos(0.1f));
        }
    }

    private static Vec3 collideWithShapes(Vec3 deltaMovement, AABB entityBB, List<VoxelShape> shapes) {
        if (shapes.isEmpty()) {
            return deltaMovement;
        } else {
            double d0 = deltaMovement.x;
            double d1 = deltaMovement.y;
            double d2 = deltaMovement.z;
            if (d1 != (double)0.0F) {
                d1 = Shapes.collide(Direction.Axis.Y, entityBB, shapes, d1);
                if (d1 != (double)0.0F) {
                    entityBB = entityBB.move((double)0.0F, d1, (double)0.0F);
                }
            }

            boolean flag = Math.abs(d0) < Math.abs(d2);
            if (flag && d2 != (double)0.0F) {
                d2 = Shapes.collide(Direction.Axis.Z, entityBB, shapes, d2);
                if (d2 != (double)0.0F) {
                    entityBB = entityBB.move((double)0.0F, (double)0.0F, d2);
                }
            }

            if (d0 != (double)0.0F) {
                d0 = Shapes.collide(Direction.Axis.X, entityBB, shapes, d0);
                if (!flag && d0 != (double)0.0F) {
                    entityBB = entityBB.move(d0, (double)0.0F, (double)0.0F);
                }
            }

            if (!flag && d2 != (double)0.0F) {
                d2 = Shapes.collide(Direction.Axis.Z, entityBB, shapes, d2);
            }

            return new Vec3(d0, d1, d2);
        }
    }
}
