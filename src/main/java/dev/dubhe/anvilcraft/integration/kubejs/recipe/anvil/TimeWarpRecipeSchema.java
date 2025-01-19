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
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public interface TimeWarpRecipeSchema {
    @SuppressWarnings({"DataFlowIssue", "unused"})
    class TimeWarpKubeRecipe extends KubeRecipe {
        public TimeWarpKubeRecipe requires(Ingredient ingredient, int count) {
            if (getValue(INGREDIENTS) == null) setValue(INGREDIENTS, new ArrayList<>());
            for (int i = 0; i < count; i++) {
                getValue(INGREDIENTS).add(ingredient);
            }
            save();
            return this;
        }

        public TimeWarpKubeRecipe requires(Ingredient ingredient) {
            return requires(ingredient, 1);
        }

        public TimeWarpKubeRecipe exactRequires(Ingredient ingredient, int count) {
            if (getValue(EXACT_INGREDIENTS) == null) setValue(EXACT_INGREDIENTS, new ArrayList<>());
            for (int i = 0; i < count; i++) {
                getValue(EXACT_INGREDIENTS).add(ingredient);
            }
            save();
            return this;
        }

        public TimeWarpKubeRecipe exactRequires(Ingredient ingredient) {
            return exactRequires(ingredient, 1);
        }

        public TimeWarpKubeRecipe result(ItemStack stack, float chance) {
            if (getValue(RESULTS) == null) setValue(RESULTS, new ArrayList<>());
            getValue(RESULTS).add(ChanceItemStack.of(stack).withChance(chance));
            save();
            return this;
        }

        public TimeWarpKubeRecipe result(ItemStack stack) {
            return result(stack, 1.0f);
        }

        public TimeWarpKubeRecipe cauldron(Block block) {
            setValue(CAULDRON, block);
            save();
            return this;
        }

        public TimeWarpKubeRecipe produceFluid(boolean produceFluid) {
            setValue(PRODUCE_FLUID, produceFluid);
            save();
            return this;
        }

        public TimeWarpKubeRecipe consumeFluid(boolean consumeFluid) {
            setValue(CONSUME_FLUID, consumeFluid);
            save();
            return this;
        }

        public TimeWarpKubeRecipe fromWater(boolean fromWater) {
            setValue(FROM_WATER, fromWater);
            save();
            return this;
        }

        public TimeWarpKubeRecipe requiredFluidLevel(int level) {
            setValue(REQUIRED_FLUID_LEVEL, level);
            save();
            return this;
        }
    }

    RecipeKey<List<Ingredient>> INGREDIENTS = IngredientComponent.INGREDIENT.asList().inputKey("ingredients").defaultOptional();
    RecipeKey<List<Ingredient>> EXACT_INGREDIENTS = IngredientComponent.INGREDIENT.asList().inputKey("exactIngredients").defaultOptional();
    RecipeKey<List<ChanceItemStack>> RESULTS = AnvilCraftRecipeComponents.CHANCE_ITEM_STACK.asList().inputKey("results").defaultOptional();
    RecipeKey<Block> CAULDRON = BlockComponent.BLOCK.outputKey("cauldron").defaultOptional();
    RecipeKey<Boolean> PRODUCE_FLUID = BooleanComponent.BOOLEAN.otherKey("produce_fluid").optional(false);
    RecipeKey<Boolean> CONSUME_FLUID = BooleanComponent.BOOLEAN.otherKey("consume_fluid").optional(false);
    RecipeKey<Boolean> FROM_WATER = BooleanComponent.BOOLEAN.otherKey("from_water").optional(false);
    RecipeKey<Integer> REQUIRED_FLUID_LEVEL = NumberComponent.INT.otherKey("requiredFluidLevel").optional(0);

    RecipeSchema SCHEMA = new RecipeSchema(INGREDIENTS, EXACT_INGREDIENTS, RESULTS, CAULDRON, PRODUCE_FLUID, CONSUME_FLUID, FROM_WATER, REQUIRED_FLUID_LEVEL)
        .factory(new KubeRecipeFactory(AnvilCraft.of("time_warp"), TimeWarpKubeRecipe.class, TimeWarpKubeRecipe::new))
        .constructor(INGREDIENTS, EXACT_INGREDIENTS, RESULTS, CAULDRON, PRODUCE_FLUID, CONSUME_FLUID, FROM_WATER, REQUIRED_FLUID_LEVEL)
        .constructor(new IDRecipeConstructor())
        .constructor();
}
