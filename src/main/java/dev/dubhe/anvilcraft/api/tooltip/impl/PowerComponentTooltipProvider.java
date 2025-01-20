package dev.dubhe.anvilcraft.api.tooltip.impl;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.api.power.IPowerComponent;
import dev.dubhe.anvilcraft.api.power.PowerComponentInfo;
import dev.dubhe.anvilcraft.api.power.PowerComponentType;
import dev.dubhe.anvilcraft.api.power.SimplePowerGrid;
import dev.dubhe.anvilcraft.api.tooltip.providers.IAnvilHammerTooltipProvider;
import dev.dubhe.anvilcraft.util.Util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PowerComponentTooltipProvider implements IAnvilHammerTooltipProvider {

    public PowerComponentTooltipProvider() {
    }

    @Override
    public boolean accepts(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        return entity instanceof IPowerComponent;
    }

    @Override
    public List<Component> tooltip(Level level, BlockPos blockPos) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (Util.jadePresent.get() && AnvilCraft.config.doNotShowTooltipWhenJadePresent) return null;
        boolean overloaded = false;
        BlockPos pos;
        if (entity instanceof IPowerComponent) {
            if (entity.getBlockState().hasProperty(IPowerComponent.OVERLOAD)) {
                overloaded = entity.getBlockState()
                    .getValues()
                    .getOrDefault(IPowerComponent.OVERLOAD, true)
                    .equals(Boolean.TRUE);
            }
            pos = entity.getBlockPos();
        } else {
            return List.of();
        }
        Optional<SimplePowerGrid> powerGrids = SimplePowerGrid.findPowerGrid(pos);
        if (powerGrids.isEmpty()) return List.of();
        SimplePowerGrid grid = powerGrids.get();
        final Optional<PowerComponentInfo> optional = grid.getInfoForPos(pos);
        if (optional.isEmpty()) return null;
        PowerComponentInfo componentInfo = optional.get();
        overloaded |= grid.getConsume() > grid.getGenerate();
        final List<Component> lines = new ArrayList<>();
        PowerComponentType type = componentInfo.type();

        if (overloaded) {
            for (int i = 1; i <= 3; i++) {
                lines.add(Component.translatable("tooltip.anvilcraft.grid_information.overloaded" + i));
            }
        }
        if (type == PowerComponentType.PRODUCER) {
            lines.add(
                Component.translatable("tooltip.anvilcraft.grid_information.producer_stats")
                .setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE))
            );
            lines.add(
                Component.translatable(
                    "tooltip.anvilcraft.grid_information.output_power",
                    componentInfo.produces()
                ).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY))
            );
        } else if (type == PowerComponentType.CONSUMER) {
            lines.add(
                Component.translatable("tooltip.anvilcraft.grid_information.consumer_stats")
                    .setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE))
            );
            lines.add(
                Component.translatable(
                    "tooltip.anvilcraft.grid_information.input_power",
                    componentInfo.consumes()
                ).setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
        }

        List<Component> tooltipLines = List.of(
            Component.translatable("tooltip.anvilcraft.grid_information.title")
                .setStyle(Style.EMPTY.applyFormat(ChatFormatting.BLUE)),
            Component.translatable("tooltip.anvilcraft.grid_information.total_consumed", grid.getConsume())
                .setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)),
            Component.translatable("tooltip.anvilcraft.grid_information.total_generated", grid.getGenerate())
                .setStyle(Style.EMPTY.applyFormat(ChatFormatting.GRAY)));
        lines.addAll(tooltipLines);
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
