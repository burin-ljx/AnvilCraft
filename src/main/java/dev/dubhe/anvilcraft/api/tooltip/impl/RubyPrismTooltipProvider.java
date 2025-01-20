package dev.dubhe.anvilcraft.api.tooltip.impl;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.tooltip.providers.IAnvilHammerTooltipProvider;
import dev.dubhe.anvilcraft.block.entity.RubyPrismBlockEntity;
import dev.dubhe.anvilcraft.util.Util;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class RubyPrismTooltipProvider implements IAnvilHammerTooltipProvider {
    public RubyPrismTooltipProvider() {}

    @Override
    public boolean accepts(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        return entity instanceof RubyPrismBlockEntity;
    }

    @Override
    public List<Component> tooltip(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (Util.jadePresent.get() && AnvilCraft.config.doNotShowTooltipWhenJadePresent) {
            return null;
        }
        if (entity instanceof RubyPrismBlockEntity rubyPrismBlockEntity) {
            return List.of(Component.translatable(
                    "tooltip.anvilcraft.jade.ruby_prism.power", rubyPrismBlockEntity.getLaserLevel()));
        }
        return null;
    }

    @Override
    public ItemStack icon(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos).getBlock().asItem().getDefaultInstance();
    }

    @Override
    public int priority() {
        return 0;
    }
}
