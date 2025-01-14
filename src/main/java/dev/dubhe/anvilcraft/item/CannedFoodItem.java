package dev.dubhe.anvilcraft.item;

import dev.dubhe.anvilcraft.api.item.IExtraItemDisplay;
import dev.dubhe.anvilcraft.init.ModComponents;
import lombok.Getter;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

@Getter
@MethodsReturnNonnullByDefault
public class CannedFoodItem extends Item implements IExtraItemDisplay {

    private final Holder<Item> canItem;

    public CannedFoodItem(Properties properties, Holder<Item> canItem) {
        super(properties);
        this.canItem = canItem;
    }

    //TODO: remove comments

//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(
//        @NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
//        ItemStack canItem = player.getItemInHand(usedHand);
//        if (usedHand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(canItem);
//        if (!level.isClientSide) {
//            ItemStack offHandItem = player.getItemInHand(InteractionHand.OFF_HAND);
//            if (!offHandItem.isEmpty()) {
//                this.setFood(canItem, offHandItem);
//                player.getInventory().setChanged();
//            }
//        }
//        return InteractionResultHolder.sidedSuccess(canItem, level.isClientSide);
//    }

    @Override
    public ItemStack getDisplayedItem(ItemStack stack) {
        return Optional.ofNullable(stack.get(ModComponents.DISPLAY_ITEM))
            .map(StoredItem::stored)
            .orElse(ItemStack.EMPTY);
    }

    public ItemStack setFood(ItemStack canStack, ItemStack foodStack) {
        ItemStack displayStack = foodStack.copy();
        if (displayStack.has(DataComponents.RARITY)) {
            canStack.set(DataComponents.RARITY, displayStack.get(DataComponents.RARITY));
        }
        canStack.set(ModComponents.DISPLAY_ITEM, new StoredItem(displayStack));
        FoodProperties copiedFood = foodStack.get(DataComponents.FOOD);
        if (copiedFood != null) {
            canStack.set(DataComponents.FOOD, new FoodProperties.Builder()
                .nutrition(copiedFood.nutrition())
                .saturationModifier(copiedFood.saturation() / (copiedFood.nutrition() * 2.0f))
                .usingConvertsTo(this.canItem.value())
                .fast()
                .build());
        }
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
