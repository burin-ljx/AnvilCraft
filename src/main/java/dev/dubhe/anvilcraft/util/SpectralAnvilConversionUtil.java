package dev.dubhe.anvilcraft.util;

import dev.dubhe.anvilcraft.init.ModBlocks;
import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SpectralAnvilConversionUtil {
    private static final Object2DoubleMap<Block> SPECTRAL_ANVIL_CONVERSION_CHANCE = new Object2DoubleOpenHashMap<>();

    static {
        SPECTRAL_ANVIL_CONVERSION_CHANCE.put(Blocks.DAMAGED_ANVIL, 0.01);
        SPECTRAL_ANVIL_CONVERSION_CHANCE.put(Blocks.CHIPPED_ANVIL, 0.02);
        SPECTRAL_ANVIL_CONVERSION_CHANCE.put(Blocks.ANVIL, 0.03);
        SPECTRAL_ANVIL_CONVERSION_CHANCE.put(ModBlocks.ROYAL_ANVIL.get(), 0.5);
        SPECTRAL_ANVIL_CONVERSION_CHANCE.put(ModBlocks.EMBER_ANVIL.get(), 1.0);
    }

    public static double chance(Block block) {
        return SPECTRAL_ANVIL_CONVERSION_CHANCE.getOrDefault(block, 0.03);
    }
}
