package dev.dubhe.anvilcraft.mixin.compat;

import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import dev.dubhe.anvilcraft.integration.sodium.ModSodiumMaterials;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.DefaultMaterials;
import net.caffeinemc.mods.sodium.client.render.chunk.terrain.material.Material;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultMaterials.class)
public class SodiumDefaultMaterialsMixin {
    @Inject(
        method = "forRenderLayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void anvilcraft$forRenderLayer(RenderType layer, CallbackInfoReturnable<Material> cir){
        if (layer == ModRenderTypes.LASER) {
            cir.setReturnValue(ModSodiumMaterials.LASER);
            cir.cancel();
        }
    }
}
