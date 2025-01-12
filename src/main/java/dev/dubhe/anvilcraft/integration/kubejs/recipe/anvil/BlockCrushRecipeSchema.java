package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.level.block.Block;

public interface BlockCrushRecipeSchema {
    RecipeKey<Block> INPUT = BlockComponent.BLOCK.inputKey("input");
    RecipeKey<Block> RESULT = BlockComponent.BLOCK.outputKey("result");

    RecipeSchema SCHEMA = new RecipeSchema(INPUT, RESULT);
}
