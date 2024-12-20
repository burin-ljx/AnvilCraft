package dev.dubhe.anvilcraft.mixin;

import dev.dubhe.anvilcraft.client.renderer.laser.LaserRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftClientMixin {
    @Inject(
        method = "updateLevelInEngines",
        at = @At("HEAD")
    )
    void updateLevel(ClientLevel level, CallbackInfo ci){
        LaserRenderer.updateLevel(level);
    }
}
