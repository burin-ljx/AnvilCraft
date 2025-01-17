package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.AnvilCraftRecipeComponents;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.IDRecipeConstructor;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public interface SuperHeatingRecipeSchema {
    @SuppressWarnings({"DataFlowIssue", "unused"})
    class SuperHeatingKubeRecipe extends KubeRecipe {
        public SuperHeatingKubeRecipe requires(Ingredient ingredient, int count) {
            if (getValue(INGREDIENTS) == null) setValue(INGREDIENTS, new ArrayList<>());
            for (int i = 0; i < count; i++) {
                getValue(INGREDIENTS).add(ingredient);
            }
            save();
            return this;
        }

        public SuperHeatingKubeRecipe requires(Ingredient ingredient) {
            return requires(ingredient, 1);
        }

        public SuperHeatingKubeRecipe result(ChanceItemStack stack) {
            if (getValue(RESULTS) == null) setValue(RESULTS, new ArrayList<>());
            getValue(RESULTS).add(stack);
            save();
            return this;
        }

        public SuperHeatingKubeRecipe result(ItemStack stack) {
            return result(ChanceItemStack.of(stack));
        }

        public SuperHeatingKubeRecipe blockResult(Block block) {
            setValue(BLOCK_RESULT, block);
            save();
            return this;
        }
    }

    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.asList().inputKey("ingredients").defaultOptional();
    RecipeKey<List<ChanceItemStack>> RESULTS = AnvilCraftRecipeComponents.CHANCE_ITEM_STACK.asList().inputKey("results").defaultOptional();
    RecipeKey<Block> BLOCK_RESULT = BlockComponent.BLOCK.outputKey("block_result").optional(Blocks.AIR);

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, RESULTS, BLOCK_RESULT)
        .factory(new KubeRecipeFactory(AnvilCraft.of("super_heating"), SuperHeatingRecipeSchema.class, SuperHeatingKubeRecipe::new))
        .constructor(INGREDIENTS, RESULTS, BLOCK_RESULT)
        .constructor(new IDRecipeConstructor())
        .constructor();
}
