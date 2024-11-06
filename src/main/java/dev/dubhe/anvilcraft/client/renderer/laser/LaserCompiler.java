package dev.dubhe.anvilcraft.client.renderer.laser;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.dubhe.anvilcraft.init.ModRenderTypes;
import lombok.Getter;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.SectionPos;
import net.neoforged.neoforge.client.event.AddSectionGeometryEvent;

import java.util.List;
import java.util.function.Function;

public class LaserCompiler {
    @Getter
    private static final LaserCompiler instance = new LaserCompiler();

    public void compile(
        SectionPos sectionPos,
        RenderChunkRegion region,
        VertexSorting vertexSorting,
        SectionBufferBuilderPack sectionBufferBuilderPack,
        List<AddSectionGeometryEvent.AdditionalSectionRenderer> additionalRenderers,
        LaserState state,
        Function<RenderType, BufferBuilder> bufferBuilderFunction
    ) {
        BufferBuilder builder = bufferBuilderFunction.apply(RenderType.TRANSLUCENT);
        renderBox(
            builder,
            state.pose(),
            -0.0625f,
            -state.offset(),
            -0.0625f,
            0.0625f,
            state.length(),
            0.0625f,
            state.atlasSprite()
        );
        renderBox(
            builder,
            state.pose(),
            -0.0625f,
            state.length(),
            -0.0625f,
            0.0625f,
            state.length() + 0.3f,
            0.0625f,
            0.35f,
            state.atlasSprite()
        );
        renderBox(
            builder,
            state.pose(),
            -0.0625f,
            state.length() + 0.3f,
            -0.0625f,
            0.0625f,
            state.length() + 0.57f,
            0.0625f,
            0.15f,
            state.atlasSprite()
        );
    }

    private static void renderBox(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        float minX,
        float minY,
        float minZ,
        float maxX,
        float maxY,
        float maxZ,
        TextureAtlasSprite sprite) {
        renderQuadX(consumer, pose, maxX, maxX, minY, minZ, maxY, maxZ, 0.5f, sprite);
        renderQuadX(consumer, pose, minX, minX, minY, maxZ, maxY, minZ, 0.5f, sprite);
        renderQuadY(consumer, pose, maxY, maxY, minX, minZ, maxX, maxZ, 0.5f, sprite);
        renderQuadY(consumer, pose, minY, minY, maxX, minZ, minX, maxZ, 0.5f, sprite);
        renderQuadZ(consumer, pose, maxZ, maxZ, minX, maxY, maxX, minY, 0.5f, sprite);
        renderQuadZ(consumer, pose, minZ, minZ, minX, minY, maxX, maxY, 0.5f, sprite);
    }

    private static void renderBox(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        float minX,
        float minY,
        float minZ,
        float maxX,
        float maxY,
        float maxZ,
        float a,
        TextureAtlasSprite sprite) {
        renderQuadX(consumer, pose, maxX, maxX, minY, minZ, maxY, maxZ, a, sprite);
        renderQuadX(consumer, pose, minX, minX, minY, maxZ, maxY, minZ, a, sprite);
        renderQuadY(consumer, pose, maxY, maxY, minX, minZ, maxX, maxZ, a, sprite);
        renderQuadY(consumer, pose, minY, minY, maxX, minZ, minX, maxZ, a, sprite);
        renderQuadZ(consumer, pose, maxZ, maxZ, minX, maxY, maxX, minY, a, sprite);
        renderQuadZ(consumer, pose, minZ, minZ, minX, minY, maxX, maxY, a, sprite);
    }

    private static void renderQuadX(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        float minX,
        float maxX,
        float minY,
        float minZ,
        float maxY,
        float maxZ,
        float a,
        TextureAtlasSprite sprite) {
        addVertex(consumer, pose, minX, maxY, minZ, sprite.getU1(), sprite.getV1(), a);
        addVertex(consumer, pose, minX, maxY, maxZ, sprite.getU0(), sprite.getV1(), a);
        addVertex(consumer, pose, maxX, minY, maxZ, sprite.getU0(), sprite.getV0(), a);
        addVertex(consumer, pose, maxX, minY, minZ, sprite.getU1(), sprite.getV0(), a);
    }

    private static void renderQuadY(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        float minY,
        float maxY,
        float minX,
        float minZ,
        float maxX,
        float maxZ,
        float a,
        TextureAtlasSprite sprite) {
        addVertex(consumer, pose, minX, minY, minZ, sprite.getU1(), sprite.getV1(), a);
        addVertex(consumer, pose, minX, minY, maxZ, sprite.getU0(), sprite.getV1(), a);
        addVertex(consumer, pose, maxX, maxY, maxZ, sprite.getU0(), sprite.getV0(), a);
        addVertex(consumer, pose, maxX, maxY, minZ, sprite.getU1(), sprite.getV0(), a);
    }

    private static void renderQuadZ(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        float minZ,
        float maxZ,
        float minX,
        float minY,
        float maxX,
        float maxY,
        float a,
        TextureAtlasSprite sprite) {
        addVertex(consumer, pose, minX, maxY, minZ, sprite.getU1(), sprite.getV1(), a);
        addVertex(consumer, pose, maxX, maxY, minZ, sprite.getU0(), sprite.getV1(), a);
        addVertex(consumer, pose, maxX, minY, maxZ, sprite.getU0(), sprite.getV0(), a);
        addVertex(consumer, pose, minX, minY, maxZ, sprite.getU1(), sprite.getV0(), a);
    }

    private static void addVertex(
        VertexConsumer consumer,
        PoseStack.Pose pose,
        float x,
        float y,
        float z,
        float u,
        float v,
        float a) {
        consumer.addVertex(pose.pose(), x, y, z)
            .setColor(1f, .2f, .2f, a)
            .setUv(u, v)
            .setUv1(0, 0)
            .setUv2(240, 240)
            .setNormal(1, 0, 0);
    }


}
