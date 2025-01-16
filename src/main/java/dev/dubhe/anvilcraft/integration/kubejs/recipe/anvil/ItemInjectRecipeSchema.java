package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.IDRecipeConstructor;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public interface ItemInjectRecipeSchema {
    @SuppressWarnings({"DataFlowIssue", "unused"})
    class ItemInjectKubeRecipe extends KubeRecipe {
        public ItemInjectKubeRecipe requires(Ingredient ingredient, int count) {
            if (getValue(INGREDIENTS) == null) setValue(INGREDIENTS, new ArrayList<>());
            for (int i = 0; i < count; i++) {
                getValue(INGREDIENTS).add(ingredient);
            }
            save();
            return this;
        }

        public ItemInjectKubeRecipe requires(Ingredient ingredient) {
            return requires(ingredient, 1);
        }

        public ItemInjectKubeRecipe inputBlock(Block block) {
            setValue(INPUT_BLOCK, block);
            save();
            return this;
        }

        public ItemInjectKubeRecipe outputBlock(Block block) {
            setValue(OUTPUT_BLOCK, block);
            save();
            return this;
        }
    }

    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.asList().inputKey("ingredients").defaultOptional();
    RecipeKey<Block> INPUT_BLOCK = BlockComponent.BLOCK.inputKey("input_block").defaultOptional();
    RecipeKey<Block> OUTPUT_BLOCK = BlockComponent.BLOCK.outputKey("result_block").defaultOptional();

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, INPUT_BLOCK, OUTPUT_BLOCK)
        .factory(new KubeRecipeFactory(AnvilCraft.of("item_inject"), ItemInjectKubeRecipe.class, ItemInjectKubeRecipe::new))
        .constructor(INGREDIENTS, INPUT_BLOCK, OUTPUT_BLOCK)
        .constructor(new IDRecipeConstructor())
        .constructor();
}
