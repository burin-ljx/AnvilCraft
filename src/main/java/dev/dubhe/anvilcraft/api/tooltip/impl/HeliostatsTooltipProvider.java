package dev.dubhe.anvilcraft.api.tooltip.impl;

import dev.dubhe.anvilcraft.api.tooltip.providers.IAnvilHammerTooltipProvider;
import dev.dubhe.anvilcraft.block.entity.HeliostatsBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class HeliostatsTooltipProvider implements IAnvilHammerTooltipProvider {
    public HeliostatsTooltipProvider() {}

    @Override
    public boolean accepts(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        return entity instanceof HeliostatsBlockEntity blockEntity &&
                !blockEntity.getWorkResult().isWorking();
    }

    @Override
    public List<Component> tooltip(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (!(entity instanceof HeliostatsBlockEntity heliostatsBlockEntity)) return null;
        final List<Component> lines = new ArrayList<>();
        lines.add(Component.translatable("tooltip.anvilcraft.heliostats.not_work"));
        lines.add(Component.translatable(heliostatsBlockEntity.getWorkResult().getTranslateKey()));
        return lines;
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
