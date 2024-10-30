package dev.dubhe.anvilcraft.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HeavyIronWallBlock extends WallBlock {
    public HeavyIronWallBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean supportsExternalFaceHiding(BlockState state) {
        return true;
    }
}
