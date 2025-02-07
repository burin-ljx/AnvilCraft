package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.AnvilCraftRecipeComponents;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.IDRecipeConstructor;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.IngredientComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public interface BulgingRecipeSchema {
    @SuppressWarnings({"DataFlowIssue", "unused"})
    class BulgingKubeRecipe extends KubeRecipe {
        public BulgingKubeRecipe requires(Ingredient... ingredient) {
            for (Ingredient ingredient1 : ingredient) {
                requires(ingredient1);
            }
            return this;
        }

        public BulgingKubeRecipe requires(Ingredient ingredient, int count) {
            if (getValue(INGREDIENTS) == null) setValue(INGREDIENTS, new ArrayList<>());
            for (int i = 0; i < count; i++) {
                getValue(INGREDIENTS).add(ingredient);
            }
            save();
            return this;
        }

        public BulgingKubeRecipe requires(Ingredient ingredient) {
            return requires(ingredient, 1);
        }

        public BulgingKubeRecipe result(ItemStack stack, float chance) {
            if (getValue(RESULTS) == null) setValue(RESULTS, new ArrayList<>());
            getValue(RESULTS).add(ChanceItemStack.of(stack).withChance(chance));
            save();
            return this;
        }

        public BulgingKubeRecipe result(ItemStack stack) {
            return result(stack, 1.0f);
        }

        public BulgingKubeRecipe cauldron(Block block) {
            setValue(CAULDRON, block);
            save();
            return this;
        }
        
        public BulgingKubeRecipe produceFluid(boolean produceFluid) {
            setValue(PRODUCE_FLUID, produceFluid);
            save();
            return this;
        }

        public BulgingKubeRecipe consumeFluid(boolean consumeFluid) {
            setValue(CONSUME_FLUID, consumeFluid);
            save();
            return this;
        }
        
        public BulgingKubeRecipe fromWater(boolean fromWater) {
            setValue(FROM_WATER, fromWater);
            save();
            return this;
        }
    }

    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.asList().inputKey("ingredients").defaultOptional();
    RecipeKey<List<ChanceItemStack>> RESULTS = AnvilCraftRecipeComponents.CHANCE_ITEM_STACK.asList().inputKey("results").defaultOptional();
    RecipeKey<Block> CAULDRON = BlockComponent.BLOCK.outputKey("cauldron").defaultOptional();
    RecipeKey<Boolean> PRODUCE_FLUID = BooleanComponent.BOOLEAN.otherKey("produce_fluid").optional(false);
    RecipeKey<Boolean> CONSUME_FLUID = BooleanComponent.BOOLEAN.otherKey("consume_fluid").optional(false);
    RecipeKey<Boolean> FROM_WATER = BooleanComponent.BOOLEAN.otherKey("from_water").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, RESULTS, CAULDRON, PRODUCE_FLUID, CONSUME_FLUID, FROM_WATER)
        .factory(new KubeRecipeFactory(AnvilCraft.of("bulging"), BulgingKubeRecipe.class, BulgingKubeRecipe::new))
        .constructor(INGREDIENTS, RESULTS, CAULDRON, PRODUCE_FLUID, CONSUME_FLUID, FROM_WATER)
        .constructor(INGREDIENTS, RESULTS)
        .constructor(INGREDIENTS, RESULTS, CAULDRON, PRODUCE_FLUID, CONSUME_FLUID)
        .constructor(new IDRecipeConstructor())
        .constructor();
}
