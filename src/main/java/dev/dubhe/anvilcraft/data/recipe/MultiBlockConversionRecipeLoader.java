package dev.dubhe.anvilcraft.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.recipe.multiblock.BlockPredicateWithState;
import dev.dubhe.anvilcraft.recipe.multiblock.ModifySpawnerAction;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockConversionRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class MultiBlockConversionRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        MultiblockConversionRecipe.builder()
            .inputLayer("ABA", "CDC", "ABA")
            .inputLayer("E E", " F ", "E E")
            .inputLayer("ABA", "C C", "ABA")
            .outputLayer("ABA", "C C", "ABA")
            .outputLayer("E E", " F ", "E E")
            .outputLayer("ABA", "C C", "ABA")
            .inputSymbol('A', ModBlocks.CURSED_GOLD_BLOCK.get())
            .outputSymbol('A', Blocks.SCULK)
            .inputSymbol('B', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "x")
            )
            .outputSymbol('B', Blocks.COBWEB)
            .inputSymbol('C', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "z")
            )
            .outputSymbol('C', Blocks.COBWEB)
            .inputSymbol('D', Blocks.SOUL_FIRE)
            .inputSymbol('E', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "y")
            )
            .outputSymbol('E', Blocks.COBWEB)
            .inputSymbol('F', ModBlocks.RESENTFUL_AMBER_BLOCK.get())
            .outputSymbol('F', Blocks.SPAWNER)
            .modifySpawnerAction(new ModifySpawnerAction(
                new BlockPos(1, 1, 1),
                new BlockPos(1, 1, 1)))
            .save(provider, AnvilCraft.of("multiblock_conversion/spawner"));
    }
}
