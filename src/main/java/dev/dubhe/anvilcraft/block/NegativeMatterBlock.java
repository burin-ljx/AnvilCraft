package dev.dubhe.anvilcraft.block;

import net.minecraft.world.level.block.Block;

public class NegativeMatterBlock extends Block implements INegativeShapeBlock<NegativeMatterBlock> {
    public NegativeMatterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<NegativeMatterBlock> getBlockType() {
        return NegativeMatterBlock.class;
    }
}
