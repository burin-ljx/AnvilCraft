package dev.dubhe.anvilcraft.mixin.forge;

import dev.dubhe.anvilcraft.block.ResinBlock;
import dev.dubhe.anvilcraft.init.ModItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PistonMovingBlockEntity.class)
abstract class PistonMovingBlockEntityMixin {
    @Redirect(
        method = "moveCollidedEntities",
        at = @At(
            value = "INVOKE",
            remap = false,
            target = "Lnet/minecraft/world/level/block/state/BlockState;isSlimeBlock()Z"
        )
    )
    private static boolean isElastic(@NotNull BlockState instance) {
        Block block = instance.getBlock();
        return block instanceof SlimeBlock || block instanceof ResinBlock;
    }

    @Redirect(
        method = "moveCollidedEntities",
        at = @At(
            value = "INVOKE",
            remap = false,
            target = "Lnet/minecraft/world/entity/Entity;getPistonPushReaction()Lnet/minecraft/world/level/material/PushReaction;"
        )
    )
    private static PushReaction cancelPistonPushForNeutronium(Entity entity) {
        if (entity instanceof ItemEntity itemEntity && itemEntity.getItem().is(ModItems.NEUTRONIUM_INGOT.get())) {
            return PushReaction.IGNORE;
        }
        return entity.getPistonPushReaction();
    }
}
