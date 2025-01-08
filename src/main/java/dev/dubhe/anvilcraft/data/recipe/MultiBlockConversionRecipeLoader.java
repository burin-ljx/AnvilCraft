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
            .symbolForInput('A', ModBlocks.CURSED_GOLD_BLOCK.get())
            .symbolForOutput('A', Blocks.SCULK)
            .symbolForInput('B', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "x")
            )
            .symbolForOutput('B', Blocks.COBWEB)
            .symbolForInput('C', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "z")
            )
            .symbolForOutput('C', Blocks.COBWEB)
            .symbolForInput('D', Blocks.SOUL_FIRE)
            .symbolForInput('E', BlockPredicateWithState.of(Blocks.CHAIN)
                .hasState("axis", "y")
            )
            .symbolForOutput('E', Blocks.COBWEB)
            .symbolForInput('F', ModBlocks.RESENTFUL_AMBER_BLOCK.get())
            .symbolForOutput('F', Blocks.SPAWNER)
            .modifySpawnerAction(new ModifySpawnerAction(
                new BlockPos(1, 1, 1),
                new BlockPos(1, 1, 1)))
            .save(provider, AnvilCraft.of("multiblock_conversion/spawner"));
    }
}
