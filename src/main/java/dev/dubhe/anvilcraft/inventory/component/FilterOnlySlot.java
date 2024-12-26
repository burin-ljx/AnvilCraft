package dev.dubhe.anvilcraft.inventory.component;

import dev.dubhe.anvilcraft.inventory.container.FilterOnlyContainer;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FilterOnlySlot extends Slot {
    public FilterOnlySlot(FilterOnlyContainer container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Override
    public ItemStack safeInsert(ItemStack stack, int increment) {
        this.container.setItem(this.getSlotIndex(), stack.copyWithCount(increment));
        return stack;
    }
}
