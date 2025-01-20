package dev.dubhe.anvilcraft.data.recipe;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItemTags;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.recipe.anvil.StampingRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public class StampingRecipeLoader {
    public static void init(RegistrateRecipeProvider provider) {
        // 压板
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
        stamping(provider, Items.CHERRY_LEAVES, Items.PINK_PETALS);

        // 木质纤维压纸
        StampingRecipe.builder()
            .requires(ModItems.WOOD_FIBER)
            .result(Items.PAPER, 4)
            .save(provider, AnvilCraft.of("stamping/paper_from_wood_fiber"));

        // 奶油
        StampingRecipe.builder()
            .requires(Items.MILK_BUCKET)
            .result(ModItems.CREAM, 4)
            .result(Items.BUCKET)
            .save(provider, AnvilCraft.of("stamping/cream"));

        // 余烬锻造模板
        StampingRecipe.builder()
            .requires(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE)
            .requires(ModItems.EMBER_METAL_INGOT)
            .result(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE)
            .save(provider);

        // 锡罐
        StampingRecipe.builder()
            .requires(ModItemTags.TIN_PLATES)
            .requires(ModItems.ROYAL_STEEL_INGOT)
            .result(ModItems.TIN_CAN)
            .result(ModItems.ROYAL_STEEL_INGOT)
            .save(provider, AnvilCraft.of("stamping/tin_can_from_plate"));

        // 嵌套潜影盒分解
        StampingRecipe.builder()
            .requires(ModBlocks.NESTING_SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .save(provider, AnvilCraft.of("stamping/shulker_box_from_nesting_shulker_box"));
        StampingRecipe.builder()
            .requires(ModBlocks.OVER_NESTING_SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .save(provider, AnvilCraft.of("stamping/shulker_box_from_over_nesting_shulker_box"));
        StampingRecipe.builder()
            .requires(ModBlocks.SUPERCRITICAL_NESTING_SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .result(Items.SHULKER_BOX)
            .save(provider, AnvilCraft.of("stamping/shulker_box_from_supercritical_nesting_shulker_box"));


    }

    private static void stamping(RegistrateRecipeProvider provider, ItemLike input, ItemLike result, int count) {
        StampingRecipe.builder()
            .requires(input)
            .result(result, count)
            .save(provider);
    }

    private static void stamping(RegistrateRecipeProvider provider, ItemLike input, ItemLike result) {
        stamping(provider, input, result, 1);
    }

    private static @NotNull String getName(@NotNull ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
    }
}
