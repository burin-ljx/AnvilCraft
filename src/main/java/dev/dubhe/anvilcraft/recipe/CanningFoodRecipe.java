package dev.dubhe.anvilcraft.recipe;

import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.stream.IntStream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CanningFoodRecipe extends CustomRecipe {

    public CanningFoodRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, Level level) {
        if (input.ingredientCount() != 2) return false;
        int canIndex = IntStream.range(0, input.ingredientCount())
            .filter(i -> input.getItem(i).is(ModItems.TIN_CAN))
            .findFirst().orElse(-1);
        if (canIndex == -1) return false;
        int foodIndex = IntStream.range(0, input.ingredientCount())
            .filter(i -> input.getItem(i).has(DataComponents.FOOD))
            .findFirst().orElse(canIndex);
        return foodIndex != canIndex;
    }

    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack foodStack = IntStream.range(0, input.ingredientCount())
            .filter(i -> input.getItem(i).has(DataComponents.FOOD))
            .mapToObj(input::getItem)
            .findFirst()
            .orElseThrow();

        return ModItems.CANNED_FOOD.get().setFood(ModItems.CANNED_FOOD.asStack(), foodStack);
    }

    /**
     * Used to determine if this recipe can fit in a grid of the given width/height
     */
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.CANNING_FOOD_SERIALIZER.get();
    }
}
