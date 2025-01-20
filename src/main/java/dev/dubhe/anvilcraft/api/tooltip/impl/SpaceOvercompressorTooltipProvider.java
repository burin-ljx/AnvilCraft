package dev.dubhe.anvilcraft.api.tooltip.impl;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.tooltip.providers.IAnvilHammerTooltipProvider;
import dev.dubhe.anvilcraft.block.entity.SpaceOvercompressorBlockEntity;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nullable;
import java.util.List;

public class SpaceOvercompressorTooltipProvider implements IAnvilHammerTooltipProvider {
    public SpaceOvercompressorTooltipProvider() {}

    @Override
    public boolean accepts(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        return entity instanceof SpaceOvercompressorBlockEntity;
    }

    @Nullable
    @Override
    public List<Component> tooltip(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (Util.jadePresent.get() && AnvilCraft.config.doNotShowTooltipWhenJadePresent) return null;
        if (!(entity instanceof SpaceOvercompressorBlockEntity compressor)) return null;
        return List.of(Component.translatable("tooltip.anvilcraft.space_overcompressor.stored_mass",
            compressor.displayStoredMass()));
    }

    @Override
    public ItemStack icon(Level level, BlockPos blockPos) {
        return ModBlocks.SPACE_OVERCOMPRESSOR.asStack();
    }

    @Override
    public int priority() {
        return 0;
    }
}
