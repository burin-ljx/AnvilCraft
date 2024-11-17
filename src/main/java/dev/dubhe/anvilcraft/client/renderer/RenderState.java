package dev.dubhe.anvilcraft.client.renderer;

import com.mojang.logging.LogUtils;
import dev.dubhe.anvilcraft.AnvilCraft;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;

public class RenderState {

    private static boolean CONTAINS_INCOMPATIBLE_MODS;
    @Getter
    private static boolean bloomRenderStage;
    private static final String[] INCOMPATIBLE_MODS = {
        "sodium",
        "embeddium",
        "iris"
    };
    private static final Logger logger = LogUtils.getLogger();

    static {
        CONTAINS_INCOMPATIBLE_MODS = false;
        for (String incompatibleMod : INCOMPATIBLE_MODS) {
            if (ModList.get().isLoaded(incompatibleMod)){
                logger.warn("Incompatible mod {} detected, fallback laser rendering into BlockEntityRenderer.", incompatibleMod);
                CONTAINS_INCOMPATIBLE_MODS = true;
            }
        }
    }

    public static void bloomStage(){
        bloomRenderStage = true;
    }

    public static void levelStage(){
        bloomRenderStage = false;
    }

    public static boolean isEnhancedRenderingAvailable() {
        return !(CONTAINS_INCOMPATIBLE_MODS || Minecraft.useShaderTransparency());
    }

    public static boolean isBloomEffectEnabled(){
        return AnvilCraft.config.renderBloomEffect;
    }
}
