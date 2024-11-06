package dev.dubhe.anvilcraft.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;

import dev.dubhe.anvilcraft.client.renderer.Line;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserState;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.phys.AABB;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.phys.Vec3;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LaserRenderer implements BlockEntityRenderer<BaseLaserBlockEntity> {

    @SuppressWarnings("unused")
    public LaserRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(
        BaseLaserBlockEntity blockEntity,
        float partialTick,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int packedLight,
        int packedOverlay
    ) {
        if (blockEntity.getLevel() == null) return;
        if (blockEntity.irradiateBlockPos == null) return;
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5);
        float length = (float) (blockEntity
            .irradiateBlockPos
            .getCenter()
            .distanceTo(blockEntity.getBlockPos().getCenter()) - 0.5);
        poseStack.mulPose(blockEntity.getDirection().getRotation());
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.LINES);
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        Line line = new Line(Vec3.ZERO,Vec3.ZERO.add(0, length, 0), length);
        line.render(
            poseStack,
            vertexConsumer,
            Vec3.ZERO,
            0x88ff2020
        );
        if (buffer instanceof MultiBufferSource.BufferSource) {
            ((MultiBufferSource.BufferSource) buffer).endBatch();
        }
        poseStack.popPose();
        return;
    }

    @Override
    public AABB getRenderBoundingBox(BaseLaserBlockEntity blockEntity) {
        int renderDistance = Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
        return AABB.ofSize(
            blockEntity.getBlockPos().getCenter(),
            renderDistance * 2,
            renderDistance * 2,
            renderDistance * 2
        );
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRenderOffScreen(BaseLaserBlockEntity blockEntity) {
        return true;
    }
}
