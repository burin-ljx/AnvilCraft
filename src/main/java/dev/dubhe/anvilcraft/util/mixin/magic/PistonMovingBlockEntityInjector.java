package dev.dubhe.anvilcraft.util.mixin.magic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public interface PistonMovingBlockEntityInjector {
    default CompoundTag anvilcraft$clearData() {
        throw new AssertionError();
    }

    default void anvilcraft$setData(CompoundTag nbt) {
        throw new AssertionError();
    }

    default BlockState anvilcraft$getMoveState() {
        throw new AssertionError();
    }
}
