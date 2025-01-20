package dev.dubhe.anvilcraft.api.tooltip.impl;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.tooltip.providers.IAnvilHammerTooltipProvider;
import dev.dubhe.anvilcraft.util.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComparatorBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.properties.ComparatorMode;

import java.util.ArrayList;
import java.util.List;

public class RedstoneElementTooltipProvider implements IAnvilHammerTooltipProvider {
    @Override
    public boolean accepts(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos).is(Blocks.REDSTONE_WIRE) ||
                level.getBlockState(blockPos).is(Blocks.COMPARATOR);
    }

    @Override
    public List<Component> tooltip(Level level, BlockPos blockPos) {
        if (Util.jadePresent.get() && AnvilCraft.config.doNotShowTooltipWhenJadePresent) return null;
        final List<Component> lines = new ArrayList<>();
        if (level.getBlockState(blockPos).is(Blocks.REDSTONE_WIRE)) {
            int power = level.getBlockState(blockPos).getValue(RedStoneWireBlock.POWER);
            lines.add(Component.translatable("tooltip.anvilcraft.redstone_information.power", power));
        } else if (level.getBlockState(blockPos).is(Blocks.COMPARATOR)) {
            ComparatorMode mode = level.getBlockState(blockPos).getValue(ComparatorBlock.MODE);
            lines.add(Component.translatable("tooltip.anvilcraft.redstone_information.comparator.mode", mode.getSerializedName()));
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof ComparatorBlockEntity comparatorBlockEntity) {
                int power = comparatorBlockEntity.getOutputSignal();
                lines.add(Component.translatable("tooltip.anvilcraft.redstone_information.power", power));
            }
        }
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
