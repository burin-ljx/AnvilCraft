package dev.dubhe.anvilcraft.mixin.compat;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.integration.embeddium.ModEmbeddiumTerrainRenderPasses;
import org.apache.commons.lang3.ArrayUtils;
import org.embeddedt.embeddium.impl.render.chunk.terrain.DefaultTerrainRenderPasses;
import org.embeddedt.embeddium.impl.render.chunk.terrain.TerrainRenderPass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DefaultTerrainRenderPasses.class)
public class EmbeddiumDefaultTerrainRenderPassesMixin {

    @Shadow
    @Final
    @Mutable
    public static TerrainRenderPass[] ALL;

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "FIELD",
            target = "Lorg/embeddedt/embeddium/impl/render/chunk/terrain/DefaultTerrainRenderPasses;ALL:[Lorg/embeddedt/embeddium/impl/render/chunk/terrain/TerrainRenderPass;"
        )
    )
    private static void wrapAllClinit(TerrainRenderPass[] value, Operation<Void> original) {
        original.call((Object) ArrayUtils.add(value, ModEmbeddiumTerrainRenderPasses.LASER));
    }

}
