package dev.dubhe.anvilcraft.api.rendering;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public abstract class CacheableBlockEntity extends BlockEntity {
    public CacheableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @OnlyIn(Dist.CLIENT)
    public abstract CacheableBlockEntityRenderer<? extends CacheableBlockEntity> getRenderer();
}
