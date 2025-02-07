package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.AnvilCraftRecipeComponents;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.IDRecipeConstructor;
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
    @SuppressWarnings({"DataFlowIssue", "unused"})
    class ItemProcessKubeRecipe extends KubeRecipe {
        public ItemProcessKubeRecipe requires(Ingredient[] ingredient) {
            for (Ingredient ingredient1 : ingredient) {
                requires(ingredient1);
            }
            return this;
        }

        public ItemProcessKubeRecipe requires(Ingredient ingredient, int count) {
            if (getValue(INGREDIENTS) == null) setValue(INGREDIENTS, new ArrayList<>());
            for (int i = 0; i < count; i++) {
                getValue(INGREDIENTS).add(ingredient);
            }
            save();
            return this;
        }

        public ItemProcessKubeRecipe requires(Ingredient ingredient) {
            return requires(ingredient, 1);
        }

        public ItemProcessKubeRecipe result(ItemStack stack, float chance) {
            if (getValue(RESULTS) == null) setValue(RESULTS, new ArrayList<>());
            getValue(RESULTS).add(ChanceItemStack.of(stack).withChance(chance));
            save();
            return this;
        }

        public ItemProcessKubeRecipe result(ItemStack stack) {
            return result(stack, 1.0f);
        }
    }
    
    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.asList().inputKey("ingredients").defaultOptional();
    RecipeKey<List<ChanceItemStack>> RESULTS = AnvilCraftRecipeComponents.CHANCE_ITEM_STACK.asList().inputKey("results").defaultOptional();
    
    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, RESULTS)
        .factory(new KubeRecipeFactory(AnvilCraft.of("item_process"), ItemProcessKubeRecipe.class, ItemProcessKubeRecipe::new))
        .constructor(INGREDIENTS, RESULTS)
        .constructor(new IDRecipeConstructor())
        .constructor();
}
