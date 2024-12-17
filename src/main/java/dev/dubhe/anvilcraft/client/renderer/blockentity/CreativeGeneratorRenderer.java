package dev.dubhe.anvilcraft.client.renderer.blockentity;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.entity.CreativeGeneratorBlockEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import org.jetbrains.annotations.NotNull;

public class CreativeGeneratorRenderer extends PowerProducerRenderer<CreativeGeneratorBlockEntity> {
    public static final ModelResourceLocation MODEL = ModelResourceLocation.standalone(
        AnvilCraft.of("block/creative_generator_cube")
    );

    /**
     * 创造发电机渲染
     */
    public CreativeGeneratorRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    protected float elevation() {
        return 0.8f;
    }

    @Override
    protected ModelResourceLocation getModel() {
        return MODEL;
    }
}
