package dev.dubhe.anvilcraft.client.init;

import com.mojang.blaze3d.pipeline.RenderTarget;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserRenderState;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;

import static dev.dubhe.anvilcraft.client.init.ModShaders.MINECRAFT;

public class ModRenderTargets {
    @Getter
    static RenderTarget laserTarget;

    public static final RenderStateShard.OutputStateShard LASER_TARGET = new RenderStateShard.OutputStateShard(
        "anvilcraft:laser",
        () -> {
            if (LaserRenderState.isEnhancedRenderingAvailable() && LaserRenderState.isBloomRenderStage()) {
                laserTarget.bindWrite(false);
            } else {
                MINECRAFT.getMainRenderTarget().bindWrite(false);
            }
        },
        () -> {
            if (LaserRenderState.isEnhancedRenderingAvailable() && LaserRenderState.isBloomRenderStage()) {
                laserTarget.unbindWrite();
            }
            MINECRAFT.getMainRenderTarget().bindWrite(false);
        }
    );

    public static void clear() {
        laserTarget.clear(Minecraft.ON_OSX);
    }

    public static void renderTargetLoaded(RenderTarget target){
        laserTarget = target;
    }

}
