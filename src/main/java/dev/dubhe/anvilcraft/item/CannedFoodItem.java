package dev.dubhe.anvilcraft.item;

import dev.dubhe.anvilcraft.api.item.IExtraItemDisplay;
import dev.dubhe.anvilcraft.init.ModComponents;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@MethodsReturnNonnullByDefault
public class CannedFoodItem extends Item implements IExtraItemDisplay {

    public CannedFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
        @NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack canItem = player.getItemInHand(usedHand);
        if (usedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(canItem);
        if (!level.isClientSide) {
            ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
            if (!offHandItem.isEmpty()) {
                setFood(canItem, offHandItem);
                player.getInventory().setChanged();
            }
        }
        return InteractionResultHolder.sidedSuccess(canItem, level.isClientSide);
    }

    @Override
    public ItemStack getDisplayedItem(ItemStack stack) {
        return Optional.ofNullable(stack.get(ModComponents.DISPLAY_ITEM))
            .map(StoredItem::stored)
            .orElse(ItemStack.EMPTY);
    }

    public static ItemStack setFood(ItemStack canStack, ItemStack foodStack) {
        ItemStack displayStack = foodStack.copy();
        if (displayStack.hasFoil()) {
            displayStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, false);
            canStack.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
        }
        if (displayStack.has(DataComponents.RARITY)) {
            canStack.set(DataComponents.RARITY, displayStack.get(DataComponents.RARITY));
        }
        canStack.set(ModComponents.DISPLAY_ITEM, new StoredItem(displayStack));
//        canStack.set(DataComponents.FOOD, new )
        return canStack;
    }

    @Override
    public int xOffset(ItemStack stack) {
        return 4;
    }

    @Override
    public int yOffset(ItemStack stack) {
        return 6;
    }

    @Override
    public float scale(ItemStack stack) {
        return 0.5f;
    }
}
