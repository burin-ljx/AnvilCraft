package dev.dubhe.anvilcraft.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.dubhe.anvilcraft.client.renderer.laser.LaserRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    @Inject(
        method = "flipFrame",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/Tesselator;clear()V"
        )
    )
    private static void clearBuffers(long windowId, CallbackInfo ci){
        if (LaserRenderer.getInstance() != null) {
            LaserRenderer.getInstance().clear();
        }
    }
}
