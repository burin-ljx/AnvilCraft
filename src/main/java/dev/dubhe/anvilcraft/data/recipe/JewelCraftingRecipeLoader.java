package dev.dubhe.anvilcraft.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItemTags;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.recipe.JewelCraftingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public class JewelCraftingRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        JewelCraftingRecipe.builder()
            .requires(Items.EXPERIENCE_BOTTLE, 16)
            .requires(Items.GOLD_BLOCK, 8)
            .requires(Items.GOLDEN_APPLE)
            .result(new ItemStack(Items.ENCHANTED_GOLDEN_APPLE))
            .save(provider);

        JewelCraftingRecipe.builder()
            .requires(Items.EMERALD_BLOCK, 2)
            .requires(Items.ENCHANTED_GOLDEN_APPLE)
            .requires(ModBlocks.ROYAL_STEEL_BLOCK)
            .result(new ItemStack(Items.TOTEM_OF_UNDYING))
            .save(provider);

        JewelCraftingRecipe.builder()
            .requires(Items.PHANTOM_MEMBRANE, 8)
            .requires(Items.BAMBOO, 4)
            .requires(ModItemTags.TITANIUM_INGOTS)
            .result(new ItemStack(Items.ELYTRA))
            .save(provider);

        JewelCraftingRecipe.builder()
            .requires(ModBlocks.SILVER_BLOCK, 4)
            .requires(Blocks.EMERALD_BLOCK)
            .requires(ModItems.ROYAL_STEEL_INGOT)
            .result(new ItemStack(ModItems.EMERALD_AMULET.asItem()))
            .save(provider);

        JewelCraftingRecipe.builder()
                .requires(ModBlocks.SILVER_BLOCK, 4)
                .requires(ModBlocks.TOPAZ_BLOCK)
                .requires(ModItems.ROYAL_STEEL_INGOT)
                .result(new ItemStack(ModItems.TOPAZ_AMULET.asItem()))
                .save(provider);

        JewelCraftingRecipe.builder()
                .requires(ModBlocks.SILVER_BLOCK, 4)
                .requires(ModBlocks.RUBY_BLOCK)
                .requires(ModItems.ROYAL_STEEL_INGOT)
                .result(new ItemStack(ModItems.RUBY_AMULET.asItem()))
                .save(provider);

        JewelCraftingRecipe.builder()
                .requires(ModBlocks.SILVER_BLOCK, 4)
                .requires(ModBlocks.SAPPHIRE_BLOCK)
                .requires(ModItems.ROYAL_STEEL_INGOT)
                .result(new ItemStack(ModItems.SAPPHIRE_AMULET.asItem()))
                .save(provider);
    }
}
