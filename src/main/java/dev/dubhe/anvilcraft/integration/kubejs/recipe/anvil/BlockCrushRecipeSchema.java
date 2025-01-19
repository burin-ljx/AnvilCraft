package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.IDRecipeConstructor;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.schema.KubeRecipeFactory;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.level.block.Block;

public interface BlockCrushRecipeSchema {
    @SuppressWarnings("unused")
    class BlockCrushKubeRecipe extends KubeRecipe {
        public BlockCrushKubeRecipe input(Block block) {
            setValue(INPUT, block);
            save();
            return this;
        }

        public BlockCrushKubeRecipe result(Block block) {
            setValue(RESULT, block);
            save();
            return this;
        }
    }

    RecipeKey<Block> INPUT = BlockComponent.BLOCK.inputKey("input").defaultOptional();
    RecipeKey<Block> RESULT = BlockComponent.BLOCK.outputKey("result").defaultOptional();

    RecipeSchema SCHEMA = new RecipeSchema(INPUT, RESULT)
        .factory(new KubeRecipeFactory(AnvilCraft.of("block_crush"), BlockCrushKubeRecipe.class, BlockCrushKubeRecipe::new))
        .constructor(INPUT, RESULT)
        .constructor(new IDRecipeConstructor())
        .constructor();
}
