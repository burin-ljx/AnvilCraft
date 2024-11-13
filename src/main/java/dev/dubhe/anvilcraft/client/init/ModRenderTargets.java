package dev.dubhe.anvilcraft.client.init;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.dubhe.anvilcraft.client.renderer.RenderState;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;

import static dev.dubhe.anvilcraft.client.init.ModShaders.MINECRAFT;

public class ModRenderTargets {
    @Getter
    static RenderTarget laserTarget;
    @Getter
    static RenderTarget lineTarget;

    public static final RenderStateShard.OutputStateShard LASER_TARGET = new RenderStateShard.OutputStateShard(
        "anvilcraft:laser",
        () -> {
            if (RenderState.isEnhancedRenderingAvailable() && RenderState.isBloomRenderStage()) {
                laserTarget.bindWrite(false);
            } else {
                MINECRAFT.getMainRenderTarget().bindWrite(false);
            }
        },
        () -> {
            if (RenderState.isEnhancedRenderingAvailable() && RenderState.isBloomRenderStage()) {
                laserTarget.unbindWrite();
            }
            MINECRAFT.getMainRenderTarget().bindWrite(false);
        }
    );

    public static final RenderStateShard.OutputStateShard LINE_BLOOM_TARGET = new RenderStateShard.OutputStateShard(
        "anvilcraft:line_bloom",
        () -> {
            if (RenderState.isEnhancedRenderingAvailable()) {
                lineTarget.bindWrite(false);
            } else {
                MINECRAFT.getMainRenderTarget().bindWrite(false);
            }
        },
        () -> {
            if (RenderState.isEnhancedRenderingAvailable()) {
                lineTarget.unbindWrite();
            }
            MINECRAFT.getMainRenderTarget().bindWrite(false);
        }
    );

    public static void clear() {
        laserTarget.clear(Minecraft.ON_OSX);
    }

    public static void renderTargetLoaded(
        RenderTarget laserTarget,
        RenderTarget lineTarget
    ){
        ModRenderTargets.laserTarget = laserTarget;
        ModRenderTargets.lineTarget = lineTarget;
    }

}
