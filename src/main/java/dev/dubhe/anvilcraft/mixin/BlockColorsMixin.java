package dev.dubhe.anvilcraft.mixin;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

    @Inject(method = "createDefault", at = @At(value = "RETURN"))
    private static void fixWaterCauldronRender(CallbackInfoReturnable<BlockColors> cir){
        cir.getReturnValue().register(
            (blockState, level, blockPos, tintIndex) ->
            blockPos != null && level != null ? BiomeColors.getAverageWaterColor(level, blockPos) : 0xFF3F76E4,
            Blocks.WATER_CAULDRON);
    }

}
