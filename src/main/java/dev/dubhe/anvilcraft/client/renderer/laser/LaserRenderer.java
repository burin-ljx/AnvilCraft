package dev.dubhe.anvilcraft.client.renderer.laser;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.api.rendering.CacheableBlockEntityRenderer;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import net.minecraft.client.renderer.RenderType;

public class LaserRenderer implements CacheableBlockEntityRenderer<BaseLaserBlockEntity> {

    public static final LaserRenderer INSTANCE = new LaserRenderer();

    @Override
    public void compileRenderType(
        BaseLaserBlockEntity blockEntity,
        RenderType renderType,
        BufferBuilder bufferBuilder,
        PoseStack poseStack
    ) {
        LaserState laserState = LaserState.create(blockEntity, poseStack);
        if (laserState != null) {
            float width = LaserCompiler.laserWidth(laserState);
            LaserCompiler.compileStage(
                laserState,
                bufferBuilder,
                renderType,
                width
            );
        }
    }
}
