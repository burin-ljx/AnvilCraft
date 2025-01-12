package dev.dubhe.anvilcraft.integration.kubejs.recipe.anvil;

import com.mojang.datafixers.util.Either;
import dev.dubhe.anvilcraft.integration.kubejs.recipe.AnvilCraftRecipeComponents;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BlockComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface BlockCompressRecipeSchema {

    RecipeKey<List<Either<TagKey<Block>, Block>>> INPUTS = AnvilCraftRecipeComponents.EITHER_BLOCK
        .asList().key("inputs", ComponentRole.INPUT);
    RecipeKey<Block> RESULT = BlockComponent.BLOCK
        .key("result", ComponentRole.OUTPUT);

    RecipeSchema SCHEMA = new RecipeSchema(INPUTS, RESULT);
}
