package dev.dubhe.anvilcraft.event;

import dev.dubhe.anvilcraft.AnvilCraft;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.NotNull;

import static dev.dubhe.anvilcraft.init.ModDataAttachments.IMMUNE_TO_LIGHTNING;

@EventBusSubscriber(modid = AnvilCraft.MOD_ID)
public class LivingEntityEventListener {
    @SubscribeEvent
    public static void PreEntityHurt(@NotNull LivingDamageEvent.Pre event){
        if(event.getSource().is(DamageTypeTags.IS_LIGHTNING)
            && event.getEntity().hasData(IMMUNE_TO_LIGHTNING)){
            if(event.getEntity().getData(IMMUNE_TO_LIGHTNING)){
                event.getContainer().setNewDamage(0);
            }
        }

    }
}
