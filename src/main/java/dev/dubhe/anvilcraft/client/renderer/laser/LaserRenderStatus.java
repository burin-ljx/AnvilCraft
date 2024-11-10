package dev.dubhe.anvilcraft.client.renderer.laser;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;

public class LaserRenderStatus {

    private static boolean ENHANCED;
    private static final String[] INCOMPATIBLE_MODS = {
        "sodium"
    };
    private static final Logger logger = LogUtils.getLogger();

    static {
        ENHANCED = true;
        for (String incompatibleMod : INCOMPATIBLE_MODS) {
            if (ModList.get().isLoaded(incompatibleMod)){
                logger.warn("Incompatible mod {} detected, fallback laser rendering into BlockEntityRenderer.", incompatibleMod);
                ENHANCED = false;
            }
        }
    }

    public static boolean isEnhancedRenderingAvailable() {
        return ENHANCED;
    }
}
