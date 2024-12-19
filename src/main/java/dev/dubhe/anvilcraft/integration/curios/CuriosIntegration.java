package dev.dubhe.anvilcraft.integration.curios;

import dev.anvilcraft.lib.integration.Integration;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.item.IEngineerGoggles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import java.util.Optional;

public class CuriosIntegration implements Integration {
    @Override
    public void apply() {
        Optional<IEventBus> optional = ModList.get()
            .getModContainerById(AnvilCraft.MOD_ID)
            .map(ModContainer::getEventBus);
        optional.ifPresent(iEventBus -> iEventBus.addListener(CuriosIntegration::setup));
        optional.ifPresent(iEventBus -> iEventBus.addListener(CuriosIntegration::onLayerRegister));
    }

    public static void setup(FMLCommonSetupEvent event) {
        IEngineerGoggles.HAS_GOGGLES_SET.add(GogglesCurioItem::hasGoggles);
        CuriosApi.registerCurio(ModItems.ANVIL_HAMMER.get(), new GogglesCurioItem());
        CuriosApi.registerCurio(ModItems.ROYAL_ANVIL_HAMMER.get(), new GogglesCurioItem());
        CuriosApi.registerCurio(ModItems.EMBER_ANVIL_HAMMER.get(), new GogglesCurioItem());
    }

    @Override
    public void applyClient() {
        CuriosRendererRegistry.register(
            ModItems.ANVIL_HAMMER.get(),
            () -> new GogglesCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GogglesCurioRenderer.LAYER))
        );
        CuriosRendererRegistry.register(
            ModItems.ROYAL_ANVIL_HAMMER.get(),
            () -> new GogglesCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GogglesCurioRenderer.LAYER))
        );
        CuriosRendererRegistry.register(
            ModItems.EMBER_ANVIL_HAMMER.get(),
            () -> new GogglesCurioRenderer(Minecraft.getInstance().getEntityModels().bakeLayer(GogglesCurioRenderer.LAYER))
        );
    }

    public static void onLayerRegister(final EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
            GogglesCurioRenderer.LAYER,
            () -> LayerDefinition.create(GogglesCurioRenderer.mesh(), 1, 1)
        );
    }
}
