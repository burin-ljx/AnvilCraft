package dev.dubhe.anvilcraft.block.piston;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface IMoveableEntityBlock extends EntityBlock {
    default @NotNull CompoundTag clearData(@NotNull Level level, @NotNull BlockPos pos) {
        return new CompoundTag();
    }

    default void setData(@NotNull Level level, @NotNull BlockPos pos, @NotNull CompoundTag nbt) {
    }
}
