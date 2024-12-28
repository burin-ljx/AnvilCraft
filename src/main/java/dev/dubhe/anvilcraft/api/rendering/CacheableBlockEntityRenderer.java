package dev.dubhe.anvilcraft.api.rendering;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;

public interface CacheableBlockEntityRenderer<T extends CacheableBlockEntity> {
    void compileRenderType(
        T cacheableBlockEntity,
        RenderType renderType,
        BufferBuilder bufferBuilder,
        PoseStack poseStack
    );
}
