package dev.dubhe.anvilcraft.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.stream.Stream;

public class SlidingRailStopBlock extends Block {
    public static final VoxelShape SHAPE = Stream.of(
            Block.box(11, 6, 11, 16, 16, 16),
            Block.box(0, 0, 0, 16, 6, 16),
            Block.box(11, 6, 0, 16, 16, 5),
            Block.box(0, 6, 0, 5, 16, 5),
            Block.box(0, 6, 11, 5, 16, 16)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public SlidingRailStopBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull VoxelShape getShape(
            @NotNull BlockState state,
            @NotNull BlockGetter level,
            @NotNull BlockPos pos,
            @NotNull CollisionContext context
    ) {
        return SHAPE;
    }

    @Override
    public void stepOn(
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull BlockState state,
            @NotNull Entity entity
    ) {
        Vec3 blockPos = pos.getCenter();
        Vec3 entityPos = entity.position();
        Vector3f acceleration = blockPos.toVector3f()
                .sub(entityPos.toVector3f())
                .mul(0.45f)
                .div(0.98f)
                .mul(new Vector3f(1, 0, 1));
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.5f, 0.5f, 0.5f).add(new Vec3(acceleration)));
    }
}
