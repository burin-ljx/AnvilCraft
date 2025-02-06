package dev.dubhe.anvilcraft.block.multipart;

import net.minecraft.world.level.block.state.BlockState;

public interface IMultiPartBlockModelHolder {
    default BlockState mapRealModelHolderBlock(BlockState original) {
        return original;
    }
}
