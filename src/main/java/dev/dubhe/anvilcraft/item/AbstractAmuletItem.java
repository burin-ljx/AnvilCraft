package dev.dubhe.anvilcraft.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static dev.dubhe.anvilcraft.init.ModDataAttachments.AMULET_COUNT;
import static dev.dubhe.anvilcraft.init.ModDataAttachments.AMULET_MAX;
import static dev.dubhe.anvilcraft.init.ModDataAttachments.DISCOUNT_RATE;
import static dev.dubhe.anvilcraft.init.ModDataAttachments.IMMUNE_TO_LIGHTNING;

public abstract class AbstractAmuletItem extends Item {

    public AbstractAmuletItem(Properties properties) {
        super(properties);
    }

    abstract void UpdateAccessory(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected);

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        if(!(stack.getItem() instanceof AbstractAmuletItem)) return;
        if(!(entity instanceof Player)) return;
        if (entity.getData(AMULET_COUNT) < entity.getData(AMULET_MAX)){
            UpdateAccessory(stack, level, entity, slotId, isSelected);
            entity.setData(AMULET_COUNT, entity.getData(AMULET_COUNT) + 1);
        }
    }

    public static void resetWorkingAmuletData(@NotNull LivingEntity entity){
        if(entity.hasData(AMULET_COUNT))
            entity.setData(AMULET_COUNT, 0);
        if(entity.hasData(DISCOUNT_RATE)) {
            entity.setData(DISCOUNT_RATE, 0f);
        }
        if(entity.hasData(IMMUNE_TO_LIGHTNING)) {
            entity.setData(IMMUNE_TO_LIGHTNING, false);
        }

    }
}
