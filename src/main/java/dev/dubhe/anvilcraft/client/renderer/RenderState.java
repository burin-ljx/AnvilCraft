package dev.dubhe.anvilcraft.client.renderer;

import com.mojang.logging.LogUtils;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.iris.IrisState;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;

public class RenderState {
    private static boolean IRIS_PRESENT;
    @Getter
    private static boolean bloomRenderStage;
    private static final Logger logger = LogUtils.getLogger();

    static {
        IRIS_PRESENT = ModList.get().isLoaded("iris");
    }

    public static boolean isIrisPresent() {
        return IRIS_PRESENT;
    }

    public static void bloomStage(){
        bloomRenderStage = true;
    }

    public static void levelStage(){
        bloomRenderStage = false;
    }

    public static boolean isEnhancedRenderingAvailable() {
        return !Minecraft.useShaderTransparency() && !IrisState.isShaderEnabled();
    }

    public static boolean isBloomEffectEnabled(){
        return AnvilCraft.config.renderBloomEffect;
    }

    public static boolean hasIncompatibleMods() {
        return false;
    }
}
