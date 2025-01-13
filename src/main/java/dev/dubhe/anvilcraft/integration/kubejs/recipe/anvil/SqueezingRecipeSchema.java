package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.IDRecipeConstructor;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.level.block.Block;

public interface SqueezingRecipeSchema {
    class SqueezingKubeRecipe extends KubeRecipe {
        public SqueezingKubeRecipe inputBlock(Block block) {
            setValue(INPUT_BLOCK, block);
            save();
            return this;
        }

        public SqueezingKubeRecipe outputBlock(Block block) {
            setValue(RESULT_BLOCK, block);
            save();
            return this;
        }

        public SqueezingKubeRecipe cauldron(Block cauldron) {
            setValue(CAULDRON, cauldron);
            save();
            return this;
        }
    }

    RecipeKey<Block> INPUT_BLOCK = BlockComponent.BLOCK.inputKey("input_block").defaultOptional();
    RecipeKey<Block> RESULT_BLOCK = BlockComponent.BLOCK.outputKey("result_block").defaultOptional();
    RecipeKey<Block> CAULDRON = BlockComponent.BLOCK.outputKey("cauldron").defaultOptional();

    RecipeSchema SCHEMA = new RecipeSchema(INPUT_BLOCK, RESULT_BLOCK, CAULDRON)
        .factory(new KubeRecipeFactory(AnvilCraft.of("squeezing"), SqueezingKubeRecipe.class, SqueezingKubeRecipe::new))
        .constructor(new IDRecipeConstructor())
        .constructor();
}
