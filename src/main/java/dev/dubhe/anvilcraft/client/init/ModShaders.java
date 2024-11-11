package dev.dubhe.anvilcraft.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.dubhe.anvilcraft.AnvilCraft;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

public class ModShaders {
    public static final ResourceLocation BLOOM_LOCATION = ResourceLocation.fromNamespaceAndPath(
        "anvilcraft",
        "shaders/post/laser_bloom.json"
    );

    @Getter
    private static PostChain laserBloomChain;
    static final Minecraft MINECRAFT = Minecraft.getInstance();

    static ShaderInstance renderTypeLaserShader;


    public static void register(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(
                    event.getResourceProvider(),
                    AnvilCraft.of("rendertype_laser"),
                    DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL
                ),
                it -> renderTypeLaserShader = it
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void resize(int width, int height) {
        if (laserBloomChain != null) {
            laserBloomChain.resize(width, height);
        }
    }

    public static void loadBlurEffect(ResourceProvider resourceProvider) throws IOException {
        laserBloomChain = new PostChain(
            MINECRAFT.getTextureManager(),
            resourceProvider,
            Minecraft.getInstance().getMainRenderTarget(),
            BLOOM_LOCATION
        );
        laserBloomChain.resize(
            Minecraft.getInstance().getWindow().getWidth(),
            Minecraft.getInstance().getWindow().getHeight()
        );
        ModRenderTargets.renderTargetLoaded(laserBloomChain.getTempTarget("input"));
    }
}
