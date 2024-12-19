package dev.dubhe.anvilcraft.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * 工程师护目镜
 */
public interface IEngineerGoggles {
    Set<Function<Player, Boolean>> HAS_GOGGLES_SET = new HashSet<>() {{
        this.add(player -> player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof IEngineerGoggles);
    }};

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean hasGoggles(Player player) {
        for (Function<Player, Boolean> function : HAS_GOGGLES_SET) {
            if (function.apply(player)) {
                return true;
            }
        }
        return false;
    }
}
