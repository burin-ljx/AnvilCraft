package dev.dubhe.anvilcraft.data.recipe;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.recipe.ChanceItemStack;
import dev.dubhe.anvilcraft.recipe.anvil.StampingRecipe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public class StampingRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        stamping(provider, Items.IRON_INGOT, Items.HEAVY_WEIGHTED_PRESSURE_PLATE);
        stamping(provider, Items.GOLD_INGOT, Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
        stamping(provider, Items.COPPER_INGOT, ModBlocks.COPPER_PRESSURE_PLATE);
        stamping(provider, ModItems.TUNGSTEN_INGOT, ModBlocks.TUNGSTEN_PRESSURE_PLATE);
        stamping(provider, ModItems.TITANIUM_INGOT, ModBlocks.TITANIUM_PRESSURE_PLATE);
        stamping(provider, ModItems.ZINC_INGOT, ModBlocks.ZINC_PRESSURE_PLATE);
        stamping(provider, ModItems.TIN_INGOT, ModBlocks.TIN_PRESSURE_PLATE);
        stamping(provider, ModItems.LEAD_INGOT, ModBlocks.LEAD_PRESSURE_PLATE);
        stamping(provider, ModItems.SILVER_INGOT, ModBlocks.SILVER_PRESSURE_PLATE);
        stamping(provider, ModItems.URANIUM_INGOT, ModBlocks.URANIUM_PRESSURE_PLATE);
        stamping(provider, ModItems.BRONZE_INGOT, ModBlocks.BRONZE_PRESSURE_PLATE);
        stamping(provider, ModItems.BRASS_INGOT, ModBlocks.BRASS_PRESSURE_PLATE);
        stamping(provider, Items.SNOWBALL, Items.SNOW);
        stamping(provider, ModItems.WOOD_FIBER.get(), Items.PAPER);

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
            .result(new ItemStack(Items.WHEAT_SEEDS))
            .save(provider);
        StampingRecipe.builder()
            .requires(Items.SUGAR_CANE)
            .result(new ItemStack(Items.PAPER))
            .result(ChanceItemStack.of(new ItemStack(Items.SUGAR)).withChance(0.25f))
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
            .result(ChanceItemStack.of(new ItemStack(ModItems.RESIN.get())).withChance(0.25f))
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

        armor(provider, Items.CHAINMAIL_HELMET, Items.CHAIN);
        armor(provider, Items.CHAINMAIL_CHESTPLATE, Items.CHAIN);
        armor(provider, Items.CHAINMAIL_LEGGINGS, Items.CHAIN);
        armor(provider, Items.CHAINMAIL_BOOTS, Items.CHAIN);

        armor(provider, Items.LEATHER_HELMET, Items.LEATHER);
        armor(provider, Items.LEATHER_CHESTPLATE, Items.LEATHER);
        armor(provider, Items.LEATHER_LEGGINGS, Items.LEATHER);
        armor(provider, Items.LEATHER_BOOTS, Items.LEATHER);
        armor(provider, Items.LEATHER_HORSE_ARMOR, Items.LEATHER);

        tool(provider, Items.IRON_SWORD, Items.IRON_INGOT);
        tool(provider, Items.IRON_PICKAXE, Items.IRON_INGOT);
        tool(provider, Items.IRON_AXE, Items.IRON_INGOT);
        tool(provider, Items.IRON_HOE, Items.IRON_INGOT);
        tool(provider, Items.IRON_SHOVEL, Items.IRON_INGOT);
        armor(provider, Items.IRON_HELMET, Items.IRON_INGOT);
        armor(provider, Items.IRON_CHESTPLATE, Items.IRON_INGOT);
        armor(provider, Items.IRON_LEGGINGS, Items.IRON_INGOT);
        armor(provider, Items.IRON_BOOTS, Items.IRON_INGOT);
        armor(provider, Items.IRON_HORSE_ARMOR, Items.IRON_INGOT);

        tool(provider, Items.GOLDEN_SWORD, Items.GOLD_INGOT);
        tool(provider, Items.GOLDEN_PICKAXE, Items.GOLD_INGOT);
        tool(provider, Items.GOLDEN_AXE, Items.GOLD_INGOT);
        tool(provider, Items.GOLDEN_HOE, Items.GOLD_INGOT);
        tool(provider, Items.GOLDEN_SHOVEL, Items.GOLD_INGOT);
        armor(provider, Items.GOLDEN_HELMET, Items.GOLD_INGOT);
        armor(provider, Items.GOLDEN_CHESTPLATE, Items.GOLD_INGOT);
        armor(provider, Items.GOLDEN_LEGGINGS, Items.GOLD_INGOT);
        armor(provider, Items.GOLDEN_BOOTS, Items.GOLD_INGOT);
        armor(provider, Items.GOLDEN_HORSE_ARMOR, Items.GOLD_INGOT);

        tool(provider, Items.DIAMOND_SWORD, Items.DIAMOND);
        tool(provider, Items.DIAMOND_PICKAXE, Items.DIAMOND);
        tool(provider, Items.DIAMOND_AXE, Items.DIAMOND);
        tool(provider, Items.DIAMOND_HOE, Items.DIAMOND);
        tool(provider, Items.DIAMOND_SHOVEL, Items.DIAMOND);
        armor(provider, Items.DIAMOND_HELMET, Items.DIAMOND);
        armor(provider, Items.DIAMOND_CHESTPLATE, Items.DIAMOND);
        armor(provider, Items.DIAMOND_LEGGINGS, Items.DIAMOND);
        armor(provider, Items.DIAMOND_BOOTS, Items.DIAMOND);
        armor(provider, Items.DIAMOND_HORSE_ARMOR, Items.DIAMOND);
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

    private static void tool(RegistrateRecipeProvider provider, ItemLike tool, ItemLike result) {
        StampingRecipe.builder()
            .requires(tool)
            .result(ChanceItemStack.of(new ItemStack(result)).withChance(0.5f))
            .save(provider, AnvilCraft.of("stamping/tool_%s_2_%s".formatted(getName(tool), getName(result))));
    }

    private static void armor(RegistrateRecipeProvider provider, ItemLike armor, ItemLike result) {
        StampingRecipe.builder()
            .requires(armor)
            .result(ChanceItemStack.of(new ItemStack(result)).withChance(0.5f))
            .result(ChanceItemStack.of(new ItemStack(result)).withChance(0.5f))
            .save(provider, AnvilCraft.of("stamping/armor_%s_2_%s".formatted(getName(armor), getName(result))));
    }

    private static @NotNull String getName(@NotNull ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }
}
