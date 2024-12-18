package dev.dubhe.anvilcraft.mixin;

import dev.dubhe.anvilcraft.init.ModBlockTags;
import dev.dubhe.anvilcraft.init.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EndPortalBlock;
import net.minecraft.world.level.block.Portal;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
abstract class EndPortalBlockMixin {
    @Inject(
        method = "entityInside",
        at =
        @At(
            value = "INVOKE",
            target =
                "Lnet/minecraft/world/entity/Entity;setAsInsidePortal(Lnet/minecraft/world/level/block/Portal;Lnet/minecraft/core/BlockPos;)V"),
        cancellable = true)
    private void fallBlockEntityInside(
        BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity, CallbackInfo ci) {
        if (pEntity instanceof FallingBlockEntity fallingBlockEntity
            && !fallingBlockEntity.blockState.is(ModBlockTags.END_PORTAL_UNABLE_CHANGE)) {
            fallingBlockEntity.blockState = ModBlocks.END_DUST.getDefaultState();
            anvil:
            if (fallingBlockEntity.blockState.is(BlockTags.ANVIL)) {
                double rand = pLevel.random.nextDouble();
                if (fallingBlockEntity.blockState.is(Blocks.DAMAGED_ANVIL) && rand >= 0.01) {
                    break anvil;
                } else if (fallingBlockEntity.blockState.is(Blocks.CHIPPED_ANVIL) && rand >= 0.02) {
                    break anvil;
                } else if (fallingBlockEntity.blockState.is(Blocks.ANVIL) && rand >= 0.03) {
                    break anvil;
                } else if (fallingBlockEntity.blockState.is(ModBlocks.ROYAL_ANVIL) && rand >= 0.5) {
                    break anvil;
                } else if (fallingBlockEntity.blockState.is(ModBlocks.EMBER_ANVIL)) {
                    fallingBlockEntity.blockState = ModBlocks.SPECTRAL_ANVIL.getDefaultState();
                    break anvil;
                } else if (rand >= 0.03) {
                    break anvil;
                }
                fallingBlockEntity.blockState = ModBlocks.SPECTRAL_ANVIL.getDefaultState();
            }
            fallingBlockEntity.setAsInsidePortal((Portal) this, pPos);
            ci.cancel();
        }
    }
}
