package dev.dubhe.anvilcraft.integration.curios;

import dev.dubhe.anvilcraft.item.IEngineerGoggles;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

public class GogglesCurioItem implements ICurioItem {
    public static boolean hasGoggles(Player player) {
        Optional<ICuriosItemHandler> inventory = CuriosApi.getCuriosInventory(player);
        if (inventory.isPresent()) {
            ICuriosItemHandler itemHandler = inventory.get();
            for (SlotResult head : itemHandler.findCurios("head")) {
                if (head.stack().getItem() instanceof IEngineerGoggles) return true;
            }
        }
        return false;
    }
}
