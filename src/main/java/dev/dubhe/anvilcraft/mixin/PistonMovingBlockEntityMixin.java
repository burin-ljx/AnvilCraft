package dev.dubhe.anvilcraft.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonMovingBlockEntity.class)
abstract class PistonMovingBlockEntityMixin {

    @Shadow private Direction direction;

    @Shadow private int deathTicks;

    @Shadow public abstract Direction getDirection();

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;"
                            + "neighborChanged(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;"
                            + "Lnet/minecraft/core/BlockPos;)V",
                    shift = At.Shift.AFTER
            )
    )
    private static void slidingRail(Level level, BlockPos pos, BlockState state, PistonMovingBlockEntity blockEntity, CallbackInfo ci) {
        if (level.isClientSide) return;
        /*
        if (level.getBlockState(pos.below()).is(ModBlocks.SLIDING_RAIL)) {
            MinecraftServer server = level.getServer();
            if (server == null) return;
            TimerQueue<MinecraftServer> timerqueue = server.getWorldData().overworldData().getScheduledEvents();
            timerqueue.schedule(
                    String.valueOf(level.getGameTime()),
                    (level.getGameTime() + 1),
                    new SlidingRailBlock.PushBlockTimeCallback(
                            new SlidingRailBlock.PushBlockData(
                                    pos, level, blockEntity.getMovementDirection()
                            )
                    )
            );
        }
         */
    }
}
