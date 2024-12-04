package dev.dubhe.anvilcraft.mixin;

import dev.dubhe.anvilcraft.AnvilCraft;
import mezz.jei.api.IModPlugin;
import mezz.jei.library.load.PluginCaller;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Consumer;

@Mixin(PluginCaller.class)
abstract class PluginCallerMixin {
    @Redirect(at = @At(value = "INVOKE", target = "java/util/function/Consumer.accept(Ljava/lang/Object;)V"),
        method = "callOnPlugins", remap = false)
    private static void callOnPlugins(@NotNull Consumer<IModPlugin> target, Object value) {
        IModPlugin plugin = (IModPlugin) value;
        if (plugin.getPluginUid().getNamespace().equals(AnvilCraft.MOD_ID)) return;
        target.accept(plugin);
    }
}
