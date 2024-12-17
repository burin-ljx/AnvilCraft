package dev.dubhe.anvilcraft.data.recipe;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.dubhe.anvilcraft.recipe.anvil.StampingRecipe;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.neoforged.neoforge.common.Tags;

public class StampingRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        stamping(provider, Items.IRON_INGOT, Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
        stamping(provider, Items.GOLD_INGOT, Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
        stamping(provider, Items.SNOWBALL, Items.SNOW);
//        stamping(provider, ModItems.WOOD_FIBER.get(), Items.PAPER);
        StampingRecipe.builder()
            .requires(ModItems.WOOD_FIBER.get())
            .result(new ItemStack(Items.PAPER, 4))
            .save(provider, AnvilCraft.of("stamping/paper_from_wood_fiber"));

        StampingRecipe.builder()
            .requires(Items.MILK_BUCKET)
            .result(ModItems.CREAM.asStack(4))
            .result(Items.BUCKET.getDefaultInstance())
            .save(provider, AnvilCraft.of("stamping/cream"));

        StampingRecipe.builder()
            .requires(ModItems.GEODE)
            .result(new ItemStack(Items.AMETHYST_SHARD, 4))
            .result(ChanceItemStack.of(new ItemStack(ModItems.TOPAZ.get())).withChance(0.25f))
            .result(ChanceItemStack.of(new ItemStack(ModItems.SAPPHIRE.get())).withChance(0.25f))
            .result(ChanceItemStack.of(new ItemStack(ModItems.RUBY.get())).withChance(0.25f))
            .save(provider, AnvilCraft.of("stamping/geode_gems"));

        StampingRecipe.builder()
            .requires(Tags.Items.CROPS_WHEAT)
            .result(new ItemStack(ModItems.FLOUR.get()))
            .result(ChanceItemStack.of(new ItemStack(ModItems.FLOUR.get())).withChance(0.5f))
            .save(provider);
        StampingRecipe.builder()
            .requires(Items.SUGAR_CANE)
            .result(new ItemStack(Items.PAPER))
            .result(new ItemStack(Items.SUGAR))
            .save(provider, AnvilCraft.of("stamping/paper_from_sugar_cane"));
        StampingRecipe.builder()
            .requires(Items.COCOA_BEANS)
            .result(new ItemStack(ModItems.COCOA_BUTTER.asItem()))
            .result(new ItemStack(ModItems.COCOA_POWDER.asItem()))
            .save(provider);
        StampingRecipe.builder()
            .requires(Items.HEART_OF_THE_SEA)
            .result(new ItemStack(ModItems.SEA_HEART_SHELL_SHARD.asItem(), 3))
            .result(ChanceItemStack.of(new ItemStack(ModItems.SEA_HEART_SHELL_SHARD.asItem())).withChance(0.5f))
            .result(ChanceItemStack.of(new ItemStack(ModItems.SEA_HEART_SHELL_SHARD.asItem())).withChance(0.5f))
            .result(new ItemStack(ModItems.SAPPHIRE.asItem()))
            .save(provider);
        StampingRecipe.builder()
            .requires(ModItems.PRISMARINE_CLUSTER)
            .result(new ItemStack(Items.PRISMARINE_CRYSTALS, 2))
            .result(new ItemStack(Items.PRISMARINE_SHARD))
            .result(ChanceItemStack.of(new ItemStack(Items.PRISMARINE_CRYSTALS)).withChance(0.5f))
            .result(ChanceItemStack.of(new ItemStack(ModItems.PRISMARINE_BLADE.asItem())).withChance(0.15f))
            .save(provider);
        StampingRecipe.builder()
            .requires(ItemTags.LOGS)
            .result(new ItemStack(ModItems.WOOD_FIBER.asItem()))
            .result(new ItemStack(ModItems.RESIN.get()))
            .save(provider);
        stamping(provider, Items.CHERRY_LEAVES, Items.PINK_PETALS);
        StampingRecipe.builder()
            .requires(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .requires(ModItems.EMBER_METAL_INGOT)
            .result(new ItemStack(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE.asItem()))
            .save(provider);

        StampingRecipe.builder()
            .requires(ModBlocks.NESTING_SHULKER_BOX)
            .result(new ItemStack(Items.SHULKER_BOX))
            .result(new ItemStack(Items.SHULKER_BOX))
            .save(provider, AnvilCraft.of("stamping/shulker_box_from_nesting_shulker_box"));
        StampingRecipe.builder()
            .requires(ModBlocks.OVER_NESTING_SHULKER_BOX)
            .result(new ItemStack(Items.SHULKER_BOX))
            .result(new ItemStack(Items.SHULKER_BOX))
            .result(new ItemStack(Items.SHULKER_BOX))
            .save(provider, AnvilCraft.of("stamping/shulker_box_from_over_nesting_shulker_box"));
        StampingRecipe.builder()
            .requires(ModBlocks.SUPERCRITICAL_NESTING_SHULKER_BOX)
            .result(new ItemStack(Items.SHULKER_BOX))
            .result(new ItemStack(Items.SHULKER_BOX))
            .result(new ItemStack(Items.SHULKER_BOX))
            .result(new ItemStack(Items.SHULKER_BOX))
            .save(provider, AnvilCraft.of("stamping/shulker_box_from_supercritical_nesting_shulker_box"));
    }

    private static void stamping(RegistrateRecipeProvider provider, ItemLike input, ItemLike result, int count) {
        StampingRecipe.builder()
            .requires(input)
            .result(new ItemStack(result, count))
            .save(provider);
    }

    private static void stamping(RegistrateRecipeProvider provider, ItemLike input, ItemLike result) {
        stamping(provider, input, result, 1);
    }
}
