package dev.dubhe.anvilcraft.data.generator.recipe;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.data.generator.MyRecipesGenerator;
import dev.dubhe.anvilcraft.data.recipe.anvil.AnvilRecipe;
import dev.dubhe.anvilcraft.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public abstract class BulgingAndCrystallizeRecipesGenerator {
    private BulgingAndCrystallizeRecipesGenerator() {
    }

    public static void buildRecipes(Consumer<FinishedRecipe> exporter) {
        bulging(Items.DIRT, Items.CLAY, exporter);
        bulging(Items.CRIMSON_FUNGUS, Items.NETHER_WART_BLOCK, exporter);
        bulging(Items.WARPED_FUNGUS, Items.WARPED_WART_BLOCK, exporter);
        bulging(Items.SPIDER_EYE, Items.FERMENTED_SPIDER_EYE, exporter);
        bulging(Items.BRAIN_CORAL, Items.BRAIN_CORAL_BLOCK, exporter);
        bulging(Items.BUBBLE_CORAL, Items.BUBBLE_CORAL_BLOCK, exporter);
        bulging(Items.FIRE_CORAL, Items.FIRE_CORAL_BLOCK, exporter);
        bulging(Items.HORN_CORAL, Items.HORN_CORAL_BLOCK, exporter);
        bulging(Items.TUBE_CORAL, Items.TUBE_CORAL_BLOCK, exporter);
        bulging(ModItems.FLOUR, ModItems.DOUGH, exporter);
        bulging(ModItems.BARK, ModItems.PULP, exporter);
        bulging(ModItems.SEED_OF_THE_SEA, ModItems.FRUIT_OF_THE_SEA, exporter);
        crystallize(ModItems.SEED_OF_THE_SEA, ModItems.TEAR_OF_THE_SEA, exporter);
        bulging(ModItems.SPONGE_GEMMULE, Items.WET_SPONGE, exporter);
    }

    public static void bulging(Item item, Item item1, Consumer<FinishedRecipe> exporter) {
        AnvilRecipe.Builder.create(RecipeCategory.MISC)
                .icon(item1)
                .hasBlock(Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))
                .hasItemIngredient(new Vec3(0.0, -1.0, 0.0), item)
                .spawnItem(new Vec3(0.0, -1.0, 0.0), item1)
                .setBlock(Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 2))
                .unlockedBy(MyRecipesGenerator.hasItem(item), FabricRecipeProvider.has(item))
                .save(exporter, AnvilCraft.of("bulging/" + BuiltInRegistries.ITEM.getKey(item1).getPath() + "_3"));
        AnvilRecipe.Builder.create(RecipeCategory.MISC)
                .icon(item1)
                .hasBlock(Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 2))
                .hasItemIngredient(new Vec3(0.0, -1.0, 0.0), item)
                .spawnItem(new Vec3(0.0, -1.0, 0.0), item1)
                .setBlock(Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1))
                .unlockedBy(MyRecipesGenerator.hasItem(item), FabricRecipeProvider.has(item))
                .save(exporter, AnvilCraft.of("bulging/" + BuiltInRegistries.ITEM.getKey(item1).getPath() + "_2"));
        AnvilRecipe.Builder.create(RecipeCategory.MISC)
                .icon(item1)
                .hasBlock(Blocks.WATER_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1))
                .hasItemIngredient(new Vec3(0.0, -1.0, 0.0), item)
                .spawnItem(new Vec3(0.0, -1.0, 0.0), item1)
                .setBlock(Blocks.CAULDRON)
                .unlockedBy(MyRecipesGenerator.hasItem(item), FabricRecipeProvider.has(item))
                .save(exporter, AnvilCraft.of("bulging/" + BuiltInRegistries.ITEM.getKey(item1).getPath() + "_1"));
    }

    public static void crystallize(Item item, Item item1, Consumer<FinishedRecipe> exporter) {
        AnvilRecipe.Builder.create(RecipeCategory.MISC)
                .icon(item1)
                .hasBlock(Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3))
                .hasItemIngredient(new Vec3(0.0, -1.0, 0.0), item)
                .spawnItem(new Vec3(0.0, -1.0, 0.0), item1)
                .setBlock(Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 2))
                .unlockedBy(MyRecipesGenerator.hasItem(item), FabricRecipeProvider.has(item))
                .save(exporter, AnvilCraft.of("crystallize/" + BuiltInRegistries.ITEM.getKey(item1).getPath() + "_3"));
        AnvilRecipe.Builder.create(RecipeCategory.MISC)
                .icon(item1)
                .hasBlock(Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 2))
                .hasItemIngredient(new Vec3(0.0, -1.0, 0.0), item)
                .spawnItem(new Vec3(0.0, -1.0, 0.0), item1)
                .setBlock(Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1))
                .unlockedBy(MyRecipesGenerator.hasItem(item), FabricRecipeProvider.has(item))
                .save(exporter, AnvilCraft.of("crystallize/" + BuiltInRegistries.ITEM.getKey(item1).getPath() + "_2"));
        AnvilRecipe.Builder.create(RecipeCategory.MISC)
                .icon(item1)
                .hasBlock(Blocks.POWDER_SNOW_CAULDRON.defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 1))
                .hasItemIngredient(new Vec3(0.0, -1.0, 0.0), item)
                .spawnItem(new Vec3(0.0, -1.0, 0.0), item1)
                .setBlock(Blocks.CAULDRON)
                .unlockedBy(MyRecipesGenerator.hasItem(item), FabricRecipeProvider.has(item))
                .save(exporter, AnvilCraft.of("crystallize/" + BuiltInRegistries.ITEM.getKey(item1).getPath() + "_1"));
    }
}
