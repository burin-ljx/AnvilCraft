package dev.dubhe.anvilcraft.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import org.jetbrains.annotations.NotNull;

public class EnchantmentDescriptionsLang {
    /**
     * @param provider 提供器
     */
    public static void init(@NotNull RegistrateLangProvider provider) {
        provider.add("enchantment.anvilcraft.harvest.desc", "Chain collection of logs and their variants, with a default configuration of enchantment level n, can chain collect up to 1+2n blocks at a time.");
        provider.add("enchantment.anvilcraft.felling.desc", "Harvest mature crops within a certain range and replant them. The first level range is 3 * 3, the second level is 5 * 5, and the third level is 7 * 7, expanding one circle per level.");
        provider.add("enchantment.anvilcraft.beheading.desc", "Enhance the probability of the Wither Skeleton Skull dropping and cause mobs (including players) that were previously unable to drop their heads to do so.");
    }
}
