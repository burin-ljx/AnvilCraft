package dev.dubhe.anvilcraft.block.entity;

import dev.dubhe.anvilcraft.block.SimpleChuteBlock;

import dev.dubhe.anvilcraft.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class SimpleChuteBlockEntity extends BaseChuteBlockEntity {
    public SimpleChuteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected boolean shouldSkipDirection(@NotNull Direction direction) {
        return Direction.UP == direction;
    }

    @Override
    protected boolean validateBlockState(@NotNull BlockState state) {
        return state.is(ModBlocks.SIMPLE_CHUTE.get());
    }

    @Override
    protected @NotNull DirectionProperty getFacingProperty() {
        return SimpleChuteBlock.FACING;
    }

    @Override
    protected @NotNull Direction getOutputDirection() {
        return getDirection();
    }

    @Override
    protected @NotNull Direction getInputDirection() {
        return Direction.UP;
    }

    @Override
    protected boolean isEnabled() {
        return getBlockState().getValue(SimpleChuteBlock.ENABLED);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.anvilcraft.simple_chute");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, @NotNull Inventory inventory, @NotNull Player player) {
        return null;
    }

    /**
     * tick
     */
    @SuppressWarnings({"UnreachableCode", "DuplicatedCode"})
//    public void tick() {
//        if (getCooldown() <= 0) {
//            if (getBlockState().getValue(SimpleChuteBlock.ENABLED)) {
//                IItemHandler target = getLevel()
//                    .getCapability(
//                        Capabilities.ItemHandler.BLOCK,
//                        getBlockPos().relative(getDirection()),
//                        getDirection().getOpposite()
//                    );
//                if (target != null) {
//                    // 尝试向朝向容器输出
//                    ItemHandlerUtil.exportToTarget(itemHandler, 64, stack -> true, target);
//                } else {
//                    Vec3 center = getBlockPos().relative(getDirection()).getCenter();
//                    List<ItemEntity> itemEntities = getLevel()
//                        .getEntitiesOfClass(
//                            ItemEntity.class,
//                            new AABB(getBlockPos().relative(getDirection())),
//                            itemEntity -> !itemEntity.getItem().isEmpty()
//                        );
//                    AABB aabb = new AABB(
//                        center.add(-0.125, -0.125, -0.125),
//                        center.add(0.125, 0.125, 0.125)
//                    );
//                    if (getLevel().noCollision(aabb)) {
//                        for (int i = 0; i < itemHandler.getSlots(); i++) {
//                            ItemStack stack = itemHandler.getStackInSlot(i);
//                            if (stack.isEmpty()) {
//                                continue;
//                            }
//                            int sameItemCount = 0;
//                            for (ItemEntity entity : itemEntities) {
//                                if (entity.getItem().getItem() == stack.getItem()) {
//                                    sameItemCount += entity.getItem().getCount();
//                                }
//                            }
//                            if (sameItemCount < stack.getItem().getMaxStackSize(stack)) {
//                                ItemStack droppedItemStack = stack.copy();
//                                int droppedItemCount =
//                                    Math.min(stack.getCount(), stack.getMaxStackSize() - sameItemCount);
//                                droppedItemStack.setCount(droppedItemCount);
//                                stack.setCount(stack.getCount() - droppedItemCount);
//                                if (stack.getCount() == 0) stack = ItemStack.EMPTY;
//                                ItemEntity itemEntity = new ItemEntity(
//                                    getLevel(), center.x, center.y, center.z, droppedItemStack, 0, 0, 0);
//                                itemEntity.setDefaultPickUpDelay();
//                                getLevel().addFreshEntity(itemEntity);
//                                itemHandler.setStackInSlot(i, stack);
//                                cooldown = AnvilCraft.config.chuteMaxCooldown;
//                                break;
//                            }
//
//                        }
//                    }
//                }
//            }
//        } else {
//            cooldown--;
//        }
//        if (level != null) {
//            level.updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
//        }
//    }

    public @NotNull Direction getDirection() {
        if (getLevel() == null) return Direction.DOWN;
        BlockState state = getLevel().getBlockState(getBlockPos());
        if (state.getBlock() instanceof SimpleChuteBlock) {
            return state.getValue(SimpleChuteBlock.FACING);
        }
        return Direction.DOWN;
    }

    /**
     * @return 红石信号强度
     */
    public int getRedstoneSignal() {
        int i = 0;
        for (int j = 0; j < itemHandler.getSlots(); ++j) {
            ItemStack itemStack = itemHandler.getStackInSlot(j);
            if (itemStack.isEmpty()) {
                continue;
            }
            ++i;
        }
        return i;
    }
}
