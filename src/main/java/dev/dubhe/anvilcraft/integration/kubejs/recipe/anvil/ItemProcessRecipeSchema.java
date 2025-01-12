package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.AnvilCraftRecipeComponents;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public interface ItemProcessRecipeSchema {
    @SuppressWarnings("DataFlowIssue")
    class ItemProcessKubeRecipe extends KubeRecipe {
        public ItemProcessKubeRecipe requires(Ingredient ingredient, int count) {
            for (int i = 0; i < count; i++) {
                if (getValue(INGREDIENTS) == null) setValue(INGREDIENTS, new ArrayList<>());
                getValue(INGREDIENTS).add(ingredient);
            }
            save();
            return this;
        }

        public ItemProcessKubeRecipe requires(Ingredient ingredient) {
            return requires(ingredient, 1);
        }

        public ItemProcessKubeRecipe result(ChanceItemStack stack) {
            if (getValue(RESULTS) == null) setValue(RESULTS, new ArrayList<>());
            getValue(RESULTS).add(stack);
            save();
            return this;
        }

        public ItemProcessKubeRecipe result(ItemStack stack) {
            return result(ChanceItemStack.of(stack));
        }
    }
    
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.asList().inputKey("ingredients").defaultOptional();
    RecipeKey<List<ChanceItemStack>> RESULTS = AnvilCraftRecipeComponents.CHANCE_ITEM_STACK.asList().inputKey("results").defaultOptional();
    
    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, RESULTS)
        .factory(new KubeRecipeFactory(AnvilCraft.of("item_process"), ItemProcessKubeRecipe.class, ItemProcessKubeRecipe::new))
        .constructor();
}
