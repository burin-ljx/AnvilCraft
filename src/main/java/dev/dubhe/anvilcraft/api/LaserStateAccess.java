package dev.dubhe.anvilcraft.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface LaserStateAccess {
    BlockPos getIrradiateBlockPos();

    int getLaserLevel();

    Direction getFacing();

    BlockPos getBlockPos();

    float getLaserOffset();

    boolean removed();
}
