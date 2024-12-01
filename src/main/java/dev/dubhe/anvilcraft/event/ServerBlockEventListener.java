package dev.dubhe.anvilcraft.event;

import dev.dubhe.anvilcraft.api.hammer.IHammerChangeable;
import dev.dubhe.anvilcraft.api.hammer.IHammerRemovable;
import dev.dubhe.anvilcraft.item.AnvilHammerItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ServerBlockEventListener {
    /**
     * 侦听右键方块事件
     *
     * @param event 右键方块事件
     */
    @SubscribeEvent
    public static void anvilHammerUse(@NotNull PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().level().isClientSide) return;
        InteractionHand hand = event.getHand();
        if (event.getEntity().getItemInHand(hand).getItem() instanceof AnvilHammerItem) {
            if (AnvilHammerItem.ableToUseAnvilHammer(event.getLevel(), event.getPos(), event.getEntity())) {
                Block b = event.getLevel().getBlockState(event.getPos()).getBlock();
                if (b instanceof IHammerRemovable
                    && !(b instanceof IHammerChangeable)
                    && !event.getEntity().isShiftKeyDown()
                ) {
                    return;
                }
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
