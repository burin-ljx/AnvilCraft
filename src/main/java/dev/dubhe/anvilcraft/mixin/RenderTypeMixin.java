package dev.dubhe.anvilcraft.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderType.class)
public class RenderTypeMixin {
    @Mutable
    @Shadow
    @Final
    public static ImmutableList<RenderType> CHUNK_BUFFER_LAYERS;

    @WrapOperation(
        method = "<clinit>",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/RenderType;CHUNK_BUFFER_LAYERS:Lcom/google/common/collect/ImmutableList;")
    )
    private static void registerChunkBufferLayer(ImmutableList<RenderType> value, Operation<Void> original){
        original.call(ImmutableList.builder()
            .addAll(value)
            .add(ModRenderTypes.LASER)
            .build()
        );
    }
}
