package dev.dubhe.anvilcraft.api.tooltip.providers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * 头戴铁砧锤时显示的tooltip
 */
public interface IAnvilHammerTooltipProvider {
    boolean accepts(Level level, BlockPos blockPos);

    List<Component> tooltip(Level level, BlockPos blockPos);

    ItemStack icon(Level level, BlockPos blockPos);

    int priority();
}
