package dev.dubhe.anvilcraft.block;

import com.mojang.blaze3d.shaders.Shader;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PlasmaJetsBlock extends Block {
    public PlasmaJetsBlock(Properties properties) {
        super(properties);
    }

    @Override
    @NotNull
    protected VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
            return Shapes.empty();
    }

    @Override
    public @NotNull VoxelShape getVisualShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return box(0, 0, 0, 16, 16, 16);
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!entity.fireImmune()) {
            entity.igniteForSeconds(15.0f);
            if (entity.hurt(entity.damageSources().inFire(), 32.0f)) {
                entity.playSound(SoundEvents.GENERIC_BURN, 0.4f, 2.0f + RandomSource.create().nextFloat() * 0.4f);
            }
        }
    }
}
