package dev.dubhe.anvilcraft.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import dev.dubhe.anvilcraft.api.hammer.IHasHammerEffect;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@Slf4j
@EventBusSubscriber(Dist.CLIENT)
public class HammerEffectRenderEventListener {

    public static final Pair<Direction, Component>[] DIRECTION_TEXTS;

    static {
        Pair<Direction, Component>[] texts = new Pair[Direction.values().length - 2];
        int idx = 0;
        for (int i = 0; i < Direction.values().length; i++) {
            Direction direction = Direction.values()[i];
            MutableComponent component = Component.literal(direction.getName());
            if (direction.getStepY() != 0) continue;
            texts[idx++] = Pair.of(direction, component);
        }
        DIRECTION_TEXTS = texts;
    }

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) return;
        Minecraft mc = Minecraft.getInstance();
        if (!(mc.screen instanceof IHasHammerEffect hasHammerEffect)) return;
        if (!hasHammerEffect.shouldRender()) return;
        BlockPos pos = hasHammerEffect.renderingBlockPos();
        BlockState state = hasHammerEffect.renderingBlockState();
        RenderType renderType = hasHammerEffect.renderType();
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        Camera camera = event.getCamera();
        Vec3 cameraPos = camera.getPosition();
        poseStack.translate(
            pos.getX() - cameraPos.x - 0.0005,
            pos.getY() - cameraPos.y - 0.0005,
            pos.getZ() - cameraPos.z - 0.0005
        );
        poseStack.scale(1.001f, 1.001f, 1.001f);
        BakedModel model = mc.getBlockRenderer().getBlockModel(state);
        ModelBlockRenderer renderer = mc.getBlockRenderer().getModelRenderer();
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        renderer.renderModel(
            poseStack.last(),
            vertexConsumer,
            state,
            model,
            1f,
            1f,
            1f,
            LightTexture.FULL_BLOCK,
            OverlayTexture.NO_OVERLAY
        );

        poseStack.popPose();
        renderDirectionText(poseStack, pos, cameraPos, camera, bufferSource);
        renderDirectionAxis(poseStack, pos, cameraPos, camera, bufferSource);
    }

    private static void renderDirectionAxis(
        PoseStack poseStack,
        BlockPos pos,
        Vec3 cameraPos,
        Camera camera,
        MultiBufferSource.BufferSource bufferSource
    ) {
        poseStack.pushPose();
        poseStack.translate(
            pos.getX() - cameraPos.x + 0.5,
            pos.getY() - cameraPos.y + 0.5,
            pos.getZ() - cameraPos.z + 0.5
        );
        VertexConsumer lineBuffer = bufferSource.getBuffer(RenderType.lines());

        lineBuffer.addVertex(poseStack.last(), 1, 0, 0)
            .setColor(0x77ff0000)
            .setNormal(1, 0, 0);
        lineBuffer.addVertex(poseStack.last(), -1, 0, 0)
            .setColor(0x77ff0000)
            .setNormal(2, 0, 0);

        lineBuffer.addVertex(poseStack.last(), 0, 1, 0)
            .setColor(0x7700ff00)
            .setNormal(0, 1, 0);
        lineBuffer.addVertex(poseStack.last(), 0, -1, 0)
            .setColor(0x7700ff00)
            .setNormal(0, 2, 0);

        lineBuffer.addVertex(poseStack.last(), 0, 0, 1)
            .setColor(0x777f7fff)
            .setNormal(0, 0, 1);
        lineBuffer.addVertex(poseStack.last(), 0, 0, -1)
            .setColor(0x777f7fff)
            .setNormal(0, 0, 2);
        poseStack.popPose();
    }

    private static void renderDirectionText(
        PoseStack poseStack,
        BlockPos pos,
        Vec3 cameraPos,
        Camera camera,
        MultiBufferSource.BufferSource bufferSource
    ) {
        poseStack.pushPose();
        poseStack.translate(
            pos.getX() - cameraPos.x + 0.5,
            pos.getY() - cameraPos.y + 0.5,
            pos.getZ() - cameraPos.z + 0.5
        );
        for (Pair<Direction, Component> value : DIRECTION_TEXTS) {
            Direction direction = value.getFirst();
            Component text = value.getSecond();
            poseStack.pushPose();
            poseStack.translate(
                direction.getStepX(),
                direction.getStepY(),
                direction.getStepZ()
            );
            poseStack.mulPose(camera.rotation());
            poseStack.scale(0.025F, -0.025F, 0.025F);
            Font font = Minecraft.getInstance().font;
            float x = (float) (-font.width(text) / 2);
            font.drawInBatch(
                text,
                x,
                0,
                553648127,
                true,
                poseStack.last().pose(),
                bufferSource,
                Font.DisplayMode.NORMAL,
                0,
                LightTexture.FULL_BRIGHT
            );
            font.drawInBatch(
                text,
                x,
                0,
                -1,
                true,
                poseStack.last().pose(),
                bufferSource,
                Font.DisplayMode.NORMAL,
                0,
                LightTexture.FULL_BRIGHT
            );
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
