package dev.dubhe.anvilcraft.mixin.compat;

import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import dev.dubhe.anvilcraft.integration.embeddium.ModEmbeddiumMaterials;
import net.minecraft.client.renderer.RenderType;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.DefaultMaterials;
import org.embeddedt.embeddium.impl.render.chunk.terrain.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultMaterials.class)
public class EmbeddiumDefaultMaterialsMixin {
    @Inject(
        method = "forRenderLayer",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void anvilcraft$forRenderLayer(RenderType layer, CallbackInfoReturnable<Material> cir){
        if (layer == ModRenderTypes.LASER){
            cir.setReturnValue(ModEmbeddiumMaterials.LASER);
            cir.cancel();
        }
    }
}
