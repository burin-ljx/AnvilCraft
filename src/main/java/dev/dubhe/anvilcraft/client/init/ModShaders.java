package dev.dubhe.anvilcraft.client.init;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.dubhe.anvilcraft.AnvilCraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

public class ModShaders {
    private static ShaderInstance renderTypeLaserShader;

    public static RenderStateShard.ShaderStateShard RENDERTYPE_LASER_SHADER = new RenderStateShard.ShaderStateShard(
        () -> renderTypeLaserShader
    );

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
}
