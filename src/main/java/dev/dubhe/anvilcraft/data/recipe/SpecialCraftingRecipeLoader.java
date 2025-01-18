package dev.dubhe.anvilcraft.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.recipe.CanningFoodRecipe;
import net.minecraft.data.recipes.SpecialRecipeBuilder;

public class SpecialCraftingRecipeLoader {

    public static void init(RegistrateRecipeProvider provider) {
        SpecialRecipeBuilder.special(CanningFoodRecipe::new)
            .save(provider, AnvilCraft.of("canned_food"));
    }
}
