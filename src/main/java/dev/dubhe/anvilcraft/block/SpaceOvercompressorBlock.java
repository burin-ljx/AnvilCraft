package dev.dubhe.anvilcraft.block;

import com.mojang.serialization.MapCodec;
import dev.dubhe.anvilcraft.block.better.BetterBaseEntityBlock;
import dev.dubhe.anvilcraft.block.entity.SpaceOvercompressorBlockEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceOvercompressorBlock extends BetterBaseEntityBlock {

    public SpaceOvercompressorBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(SpaceOvercompressorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SpaceOvercompressorBlockEntity(pos, state);
    }
}
