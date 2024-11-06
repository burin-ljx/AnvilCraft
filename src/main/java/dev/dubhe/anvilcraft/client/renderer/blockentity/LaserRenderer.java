package dev.dubhe.anvilcraft.client.renderer.blockentity;

import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;

import com.mojang.blaze3d.vertex.PoseStack;

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
