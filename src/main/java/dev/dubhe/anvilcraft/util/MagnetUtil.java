package dev.dubhe.anvilcraft.util;

import dev.dubhe.anvilcraft.block.MagnetBlock;
import dev.dubhe.anvilcraft.init.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class MagnetUtil {
    public static boolean hasMagnetism(@NotNull Level level, @NotNull BlockPos pos) {
        BlockState state = level.getBlockState(pos.above());
        return (state.is(ModBlockTags.MAGNET) || state.getBlock() instanceof MagnetBlock)
            && state.hasProperty(MagnetBlock.LIT)
            && !state.getValue(MagnetBlock.LIT);
    }
}
