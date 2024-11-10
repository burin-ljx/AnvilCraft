package dev.dubhe.anvilcraft.init;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import org.spongepowered.asm.launch.platform.CommandLineOptions;

import static net.minecraft.client.renderer.RenderStateShard.BLOCK_SHEET_MIPPED;
import static net.minecraft.client.renderer.RenderStateShard.CLOUDS_TARGET;
import static net.minecraft.client.renderer.RenderStateShard.COLOR_DEPTH_WRITE;
import static net.minecraft.client.renderer.RenderStateShard.COLOR_WRITE;
import static net.minecraft.client.renderer.RenderStateShard.CULL;
import static net.minecraft.client.renderer.RenderStateShard.DEPTH_WRITE;
import static net.minecraft.client.renderer.RenderStateShard.GREATER_DEPTH_TEST;
import static net.minecraft.client.renderer.RenderStateShard.ITEM_ENTITY_TARGET;
import static net.minecraft.client.renderer.RenderStateShard.LIGHTMAP;
import static net.minecraft.client.renderer.RenderStateShard.MAIN_TARGET;
import static net.minecraft.client.renderer.RenderStateShard.OVERLAY;
import static net.minecraft.client.renderer.RenderStateShard.RENDERTYPE_CLOUDS_SHADER;
import static net.minecraft.client.renderer.RenderStateShard.RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER;
import static net.minecraft.client.renderer.RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER;
import static net.minecraft.client.renderer.RenderStateShard.TRANSLUCENT_TARGET;
import static net.minecraft.client.renderer.RenderStateShard.TRANSLUCENT_TRANSPARENCY;
import static net.minecraft.client.renderer.RenderStateShard.VIEW_OFFSET_Z_LAYERING;
import static net.minecraft.client.renderer.RenderStateShard.WEATHER_TARGET;

public class ModRenderTypes {
    public static final RenderStateShard.TransparencyStateShard LASER_TRANSPARENCY = new RenderStateShard.TransparencyStateShard(
        "anvilcraft:laser_transparency",
        () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_COLOR,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ZERO,
                GlStateManager.DestFactor.ONE
            );
        },
        () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    );

    public static final RenderType LASER = RenderType.create(
        "anvilcraft:laser",
        DefaultVertexFormat.NEW_ENTITY,
        VertexFormat.Mode.QUADS,
        1536,
        true,
        true,
        RenderType.CompositeState.builder()
            .setLightmapState(LIGHTMAP)
            .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
            .setTextureState(new RenderStateShard.TextureStateShard(
                TextureAtlas.LOCATION_BLOCKS,
                false,
                false
            )).setTransparencyState(LASER_TRANSPARENCY)
            .setCullState(CULL)
            .setWriteMaskState(COLOR_DEPTH_WRITE)
            .setOverlayState(OVERLAY)
            .setOutputState(MAIN_TARGET)
            .createCompositeState(true)
    );

    public static final RenderType BEACON_GLASS = RenderType.create(
        "anvilcraft:beacon_glass",
        DefaultVertexFormat.BLOCK,
        VertexFormat.Mode.QUADS,
        786432,
        true,
        true,
        RenderType.CompositeState.builder()
            .setLightmapState(LIGHTMAP)
            .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER)
            .setTextureState(BLOCK_SHEET_MIPPED)
            .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
            .setOutputState(TRANSLUCENT_TARGET)
            .setWriteMaskState(COLOR_WRITE)
            .createCompositeState(true)
    );
}
