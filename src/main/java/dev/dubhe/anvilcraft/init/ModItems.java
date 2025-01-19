package dev.dubhe.anvilcraft.init;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.state.Color;
import dev.dubhe.anvilcraft.data.AnvilCraftDatagen;
import dev.dubhe.anvilcraft.item.AmethystAxeItem;
import dev.dubhe.anvilcraft.item.AmethystHoeItem;
import dev.dubhe.anvilcraft.item.AmethystPickaxeItem;
import dev.dubhe.anvilcraft.item.AmethystShovelItem;
import dev.dubhe.anvilcraft.item.AmethystSwordItem;
import dev.dubhe.anvilcraft.item.AnvilHammerItem;
import dev.dubhe.anvilcraft.item.CannedFoodItem;
import dev.dubhe.anvilcraft.item.CapacitorItem;
import dev.dubhe.anvilcraft.item.CrabClawItem;
import dev.dubhe.anvilcraft.item.CursedItem;
import dev.dubhe.anvilcraft.item.DiskItem;
import dev.dubhe.anvilcraft.item.EmberAnvilHammerItem;
import dev.dubhe.anvilcraft.item.EmberMetalAxeItem;
import dev.dubhe.anvilcraft.item.EmberMetalHoeItem;
import dev.dubhe.anvilcraft.item.EmberMetalPickaxeItem;
import dev.dubhe.anvilcraft.item.EmberMetalShovelItem;
import dev.dubhe.anvilcraft.item.EmberMetalSwordItem;
import dev.dubhe.anvilcraft.item.EmberMetalUpgradeTemplateItem;
import dev.dubhe.anvilcraft.item.EmeraldAmuletItem;
import dev.dubhe.anvilcraft.item.EmptyCapacitorItem;
import dev.dubhe.anvilcraft.item.GeodeItem;
import dev.dubhe.anvilcraft.item.GuideBookItem;
import dev.dubhe.anvilcraft.item.LevitationPowderItem;
import dev.dubhe.anvilcraft.item.MagnetItem;
import dev.dubhe.anvilcraft.item.ModFoods;
import dev.dubhe.anvilcraft.item.RoyalAnvilHammerItem;
import dev.dubhe.anvilcraft.item.RoyalAxeItem;
import dev.dubhe.anvilcraft.item.RoyalHoeItem;
import dev.dubhe.anvilcraft.item.RoyalPickaxeItem;
import dev.dubhe.anvilcraft.item.RoyalShovelItem;
import dev.dubhe.anvilcraft.item.RoyalSwordItem;
import dev.dubhe.anvilcraft.item.RoyalUpgradeTemplateItem;
import dev.dubhe.anvilcraft.item.RubyAmuletItem;
import dev.dubhe.anvilcraft.item.SapphireAmuletItem;
import dev.dubhe.anvilcraft.item.SeedsPackItem;
import dev.dubhe.anvilcraft.item.StructureToolItem;
import dev.dubhe.anvilcraft.item.SuperHeavyItem;
import dev.dubhe.anvilcraft.item.TopazAmuletItem;
import dev.dubhe.anvilcraft.item.TopazItem;
import dev.dubhe.anvilcraft.item.UtusanItem;
import dev.dubhe.anvilcraft.util.ModelProviderUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

import static dev.dubhe.anvilcraft.AnvilCraft.REGISTRATE;

@SuppressWarnings({"unused", "CodeBlock2Expr"})
public class ModItems {
    static {
        REGISTRATE.defaultCreativeTab(ModItemGroups.ANVILCRAFT_TOOL.getKey());
    }

    public static final ItemEntry<GuideBookItem> GUIDE_BOOK = REGISTRATE
        .item("guide_book", GuideBookItem::new)
        .properties(p -> p.stacksTo(1))
        .model((ctx, provider) -> {
        })
        .lang("AnvilCraft Guide Book")
        .register();
    // 工具
    public static final ItemEntry<MagnetItem> MAGNET = REGISTRATE
        .item("magnet", properties -> new MagnetItem(properties.durability(255)))
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" A ")
                .define('A', Items.ENDER_PEARL)
                .define('B', ModItems.MAGNET_INGOT)
                .define('C', Items.REDSTONE)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModItems.MAGNET_INGOT))
                .save(provider);
        })
        .register();
    public static final ItemEntry<GeodeItem> GEODE =
        REGISTRATE.item("geode", GeodeItem::new).register();
    public static final ItemEntry<? extends PickaxeItem> AMETHYST_PICKAXE = REGISTRATE
        .item("amethyst_pickaxe", AmethystPickaxeItem::new)
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                .pattern("AAA")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.STICK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                .save(provider);
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.PICKAXES, ItemTags.CLUSTER_MAX_HARVESTABLES)
        .register();
    public static final ItemEntry<? extends AxeItem> AMETHYST_AXE = REGISTRATE
        .item("amethyst_axe", AmethystAxeItem::new)
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                .pattern("AA ")
                .pattern("AB ")
                .pattern(" B ")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.STICK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                .save(provider);
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.AXES)
        .register();
    public static final ItemEntry<? extends HoeItem> AMETHYST_HOE = REGISTRATE
        .item("amethyst_hoe", AmethystHoeItem::new)
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                .pattern("AA ")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.STICK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                .save(provider);
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.HOES)
        .register();
    public static final ItemEntry<? extends SwordItem> AMETHYST_SWORD = REGISTRATE
        .item("amethyst_sword", AmethystSwordItem::new)
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.STICK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                .save(provider);
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.SWORDS)
        .register();
    public static final ItemEntry<? extends ShovelItem> AMETHYST_SHOVEL = REGISTRATE
        .item("amethyst_shovel", AmethystShovelItem::new)
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', Items.AMETHYST_SHARD)
                .define('B', Items.STICK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(Items.AMETHYST_SHARD))
                .save(provider);
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.SHOVELS)
        .register();
    public static final ItemEntry<? extends Item> ROYAL_STEEL_PICKAXE = REGISTRATE
        .item("royal_steel_pickaxe", RoyalPickaxeItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.ROYAL_STEEL_PICKAXE_BASE),
                    Ingredient.of(ModItems.ROYAL_STEEL_INGOT),
                    RecipeCategory.TOOLS, ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.ROYAL_STEEL_INGOT))
                .save(provider, AnvilCraft.of("smithing/royal_steel_pickaxe"));
        })
        .properties(properties -> properties.durability(1561))
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.PICKAXES)
        .register();
    public static final ItemEntry<? extends Item> ROYAL_STEEL_AXE = REGISTRATE
        .item("royal_steel_axe", RoyalAxeItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.ROYAL_STEEL_AXE_BASE),
                    Ingredient.of(ModItems.ROYAL_STEEL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.ROYAL_STEEL_INGOT))
                .save(provider, AnvilCraft.of("smithing/royal_steel_axe"));
        })
        .properties(properties -> properties.durability(1561))
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.AXES)
        .register();
    public static final ItemEntry<? extends Item> ROYAL_STEEL_SHOVEL = REGISTRATE
        .item("royal_steel_shovel", RoyalShovelItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.ROYAL_STEEL_SHOVEL_BASE),
                    Ingredient.of(ModItems.ROYAL_STEEL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.ROYAL_STEEL_INGOT))
                .save(provider, AnvilCraft.of("smithing/royal_steel_shovel"));
        })
        .properties(properties -> properties.durability(1561))
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.SHOVELS)
        .register();
    public static final ItemEntry<? extends Item> ROYAL_STEEL_HOE = REGISTRATE
        .item("royal_steel_hoe", RoyalHoeItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.ROYAL_STEEL_HOE_BASE),
                    Ingredient.of(ModItems.ROYAL_STEEL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.ROYAL_STEEL_INGOT))
                .save(provider, AnvilCraft.of("smithing/royal_steel_hoe"));
        })
        .properties(properties -> properties.durability(1561))
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.HOES)
        .register();
    public static final ItemEntry<? extends Item> ROYAL_STEEL_SWORD = REGISTRATE
        .item("royal_steel_sword", RoyalSwordItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.ROYAL_STEEL_SWORD_BASE),
                    Ingredient.of(ModItems.ROYAL_STEEL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.ROYAL_STEEL_INGOT))
                .save(provider, AnvilCraft.of("smithing/royal_steel_sword"));
        })
        .properties(properties -> properties.durability(1561))
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.SWORDS)
        .register();
    public static final ItemEntry<EmberMetalPickaxeItem> EMBER_METAL_PICKAXE = REGISTRATE
        .item("ember_metal_pickaxe", EmberMetalPickaxeItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.EMBER_METAL_PICKAXE_BASE),
                    Ingredient.of(ModItems.EMBER_METAL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.EMBER_METAL_INGOT))
                .save(provider, AnvilCraft.of("smithing/ember_metal_pickaxe"));
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.PICKAXES,
            ModItemTags.EXPLOSION_PROOF)
        .register();
    public static final ItemEntry<EmberMetalAxeItem> EMBER_METAL_AXE = REGISTRATE
        .item("ember_metal_axe", EmberMetalAxeItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.EMBER_METAL_AXE_BASE),
                    Ingredient.of(ModItems.EMBER_METAL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.EMBER_METAL_INGOT))
                .save(provider, AnvilCraft.of("smithing/ember_metal_axe"));
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.AXES)
        .register();
    public static final ItemEntry<EmberMetalShovelItem> EMBER_METAL_SHOVEL = REGISTRATE
        .item("ember_metal_shovel", EmberMetalShovelItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.EMBER_METAL_SHOVEL_BASE),
                    Ingredient.of(ModItems.EMBER_METAL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.EMBER_METAL_INGOT))
                .save(provider, AnvilCraft.of("smithing/ember_metal_shovel"));
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.SHOVELS)
        .register();
    public static final ItemEntry<EmberMetalHoeItem> EMBER_METAL_HOE = REGISTRATE
        .item("ember_metal_hoe", EmberMetalHoeItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.EMBER_METAL_HOE_BASE),
                    Ingredient.of(ModItems.EMBER_METAL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.EMBER_METAL_INGOT))
                .save(provider, AnvilCraft.of("smithing/ember_metal_hoe"));
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.HOES)
        .register();
    public static final ItemEntry<EmberMetalSwordItem> EMBER_METAL_SWORD = REGISTRATE
        .item("ember_metal_sword", EmberMetalSwordItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItemTags.EMBER_METAL_SWORD_BASE),
                    Ingredient.of(ModItems.EMBER_METAL_INGOT),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModItems.EMBER_METAL_INGOT))
                .save(provider, AnvilCraft.of("smithing/ember_metal_sword"));
        })
        .model((ctx, provider) -> provider.handheld(ctx))
        .tag(ItemTags.SWORDS)
        .register();
    public static final ItemEntry<AnvilHammerItem> ANVIL_HAMMER = REGISTRATE
        .item("anvil_hammer", AnvilHammerItem::new)
        .properties(properties -> properties.durability(35))
        .tag(ItemTags.MACE_ENCHANTABLE)
        .model((ctx, provider) -> {
        })
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(
            RecipeCategory.TOOLS, ctx.get())
            .pattern("A")
            .pattern("B")
            .pattern("C")
            .define('A', Items.ANVIL)
            .define('B', Items.LIGHTNING_ROD)
            .define('C', Items.IRON_INGOT)
            .unlockedBy(AnvilCraftDatagen.hasItem(Items.ANVIL), RegistrateRecipeProvider.has(Items.ANVIL))
            .unlockedBy(
                AnvilCraftDatagen.hasItem(Items.LIGHTNING_ROD),
                RegistrateRecipeProvider.has(Items.LIGHTNING_ROD))
            .unlockedBy(
                AnvilCraftDatagen.hasItem(Items.IRON_INGOT), RegistrateRecipeProvider.has(Items.IRON_INGOT))
            .save(provider))
        .register();

    public static final ItemEntry<RoyalAnvilHammerItem> ROYAL_ANVIL_HAMMER = REGISTRATE
        .item("royal_anvil_hammer", RoyalAnvilHammerItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItems.ANVIL_HAMMER),
                    Ingredient.of(ModBlocks.ROYAL_STEEL_BLOCK),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModBlocks.ROYAL_STEEL_BLOCK))
                .save(provider, AnvilCraft.of("smithing/royal_anvil_hammer"));
        })
        .tag(ItemTags.MACE_ENCHANTABLE)
        .properties(properties -> properties.durability(150))
        .model((ctx, provider) -> {
        })
        .register();
    public static final ItemEntry<EmberAnvilHammerItem> EMBER_ANVIL_HAMMER = REGISTRATE
        .item("ember_anvil_hammer", EmberAnvilHammerItem::new)
        .recipe((ctx, provider) -> {
            SmithingTransformRecipeBuilder.smithing(
                    Ingredient.of(ModItems.EMBER_METAL_UPGRADE_SMITHING_TEMPLATE),
                    Ingredient.of(ModItems.ROYAL_ANVIL_HAMMER),
                    Ingredient.of(ModBlocks.EMBER_METAL_BLOCK),
                    RecipeCategory.TOOLS,
                    ctx.get())
                .unlocks("hasitem", AnvilCraftDatagen.has(ModBlocks.EMBER_METAL_BLOCK))
                .save(provider, AnvilCraft.of("smithing/ember_anvil_hammer"));
        })
        .tag(ItemTags.MACE_ENCHANTABLE)
        .properties(properties -> properties.durability(2031))
        .model((ctx, provider) -> {
        })
        .register();
    // 升级锻造模板
    public static final ItemEntry<RoyalUpgradeTemplateItem> ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE = REGISTRATE
        .item("royal_steel_upgrade_smithing_template", RoyalUpgradeTemplateItem::new)
        .register();
    public static final ItemEntry<EmberMetalUpgradeTemplateItem> EMBER_METAL_UPGRADE_SMITHING_TEMPLATE = REGISTRATE
        .item("ember_metal_upgrade_smithing_template", EmberMetalUpgradeTemplateItem::new)
        .register();

    public static final ItemEntry<DiskItem> DISK = REGISTRATE
        .item("disk", DiskItem::new)
        .properties(p -> p.stacksTo(1))
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(
            RecipeCategory.TOOLS, ctx.get())
            .pattern("ABA")
            .pattern("ACA")
            .pattern("AAA")
            .define('A', ModItems.HARDEND_RESIN)
            .define('B', Items.IRON_INGOT)
            .define('C', ModItems.MAGNET_INGOT)
            .unlockedBy(
                AnvilCraftDatagen.hasItem(ModItems.HARDEND_RESIN),
                RegistrateRecipeProvider.has(ModItems.HARDEND_RESIN))
            .unlockedBy(
                AnvilCraftDatagen.hasItem(Items.IRON_INGOT), RegistrateRecipeProvider.has(Items.IRON_INGOT))
            .unlockedBy(
                AnvilCraftDatagen.hasItem(ModItems.MAGNET_INGOT),
                RegistrateRecipeProvider.has(ModItems.MAGNET_INGOT))
            .save(provider))
        .register();
    public static final ItemEntry<CrabClawItem> CRAB_CLAW = REGISTRATE
        .item("crab_claw", CrabClawItem::new)
        .model((ctx, provider) -> {
        })
        .register();

    public static final ItemEntry<EmeraldAmuletItem> EMERALD_AMULET = REGISTRATE
            .item("emerald_amulet", EmeraldAmuletItem::new)
            //这里不知道加个什么tag好 .tag(ModItemTags.？？？)
            .register();
    public static final ItemEntry<TopazAmuletItem> TOPAZ_AMULET = REGISTRATE
            .item("topaz_amulet", TopazAmuletItem::new)
            .register();
    public static final ItemEntry<RubyAmuletItem> RUBY_AMULET = REGISTRATE
            .item("ruby_amulet", RubyAmuletItem::new)
            .register();
    public static final ItemEntry<SapphireAmuletItem> SAPPHIRE_AMULET = REGISTRATE
            .item("sapphire_amulet", SapphireAmuletItem::new)
            .register();


    public static final ItemEntry<CapacitorItem> CAPACITOR = REGISTRATE
        .item("capacitor", CapacitorItem::new)
        .model((ctx, provider) -> {
        })
        .tag(ModItemTags.CAPACITOR)
        .register();
    public static final ItemEntry<EmptyCapacitorItem> CAPACITOR_EMPTY = REGISTRATE
        .item("capacitor_empty", EmptyCapacitorItem::new)
        .model((ctx, provider) -> {
        })
        .tag(ModItemTags.CAPACITOR)
        .register();
    public static final ItemEntry<Item> CHOCOLATE = REGISTRATE
        .item("chocolate", properties -> new Item(properties.food(ModFoods.CHOCOLATE)))
        .tag(Tags.Items.FOODS)
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(
            RecipeCategory.FOOD, ctx.get())
            .pattern("ABA")
            .pattern("CDC")
            .pattern("ABA")
            .define('A', ModItems.COCOA_LIQUOR)
            .define('B', ModItems.COCOA_BUTTER)
            .define('C', ModItems.CREAM)
            .define('D', Items.SUGAR)
            .unlockedBy("has_cocoa_liquor", RegistrateRecipeProvider.has(ModItems.COCOA_LIQUOR))
            .unlockedBy("has_cocoa_butter", RegistrateRecipeProvider.has(ModItems.COCOA_BUTTER))
            .unlockedBy("has_cream", RegistrateRecipeProvider.has(ModItems.CREAM))
            .unlockedBy("has_sugar", RegistrateRecipeProvider.has(Items.SUGAR))
            .save(provider))
        .register();
    public static final ItemEntry<Item> CHOCOLATE_BLACK = REGISTRATE
        .item("chocolate_black", p -> new Item(p.food(ModFoods.CHOCOLATE_BLACK)))
        .tag(Tags.Items.FOODS)
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(
            RecipeCategory.FOOD, ctx.get())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("AAA")
            .define('A', ModItems.COCOA_LIQUOR)
            .define('B', ModItems.COCOA_BUTTER)
            .define('C', Items.SUGAR)
            .unlockedBy("has_cocoa_butter", RegistrateRecipeProvider.has(ModItems.COCOA_LIQUOR))
            .unlockedBy("has_cream", RegistrateRecipeProvider.has(ModItems.CREAM))
            .unlockedBy("has_sugar", RegistrateRecipeProvider.has(Items.SUGAR))
            .save(provider))
        .register();
    public static final ItemEntry<Item> CHOCOLATE_WHITE = REGISTRATE
        .item("chocolate_white", p -> new Item(p.food(ModFoods.CHOCOLATE_WHITE)))
        .tag(Tags.Items.FOODS)
        .recipe((ctx, provider) -> ShapedRecipeBuilder.shaped(
            RecipeCategory.FOOD, ctx.get())
            .pattern("AAA")
            .pattern("BCB")
            .pattern("AAA")
            .define('A', ModItems.COCOA_BUTTER)
            .define('B', ModItems.CREAM)
            .define('C', Items.SUGAR)
            .unlockedBy("has_butter", RegistrateRecipeProvider.has(ModItems.COCOA_BUTTER))
            .unlockedBy("has_cream", RegistrateRecipeProvider.has(ModItems.CREAM))
            .unlockedBy("has_sugar", RegistrateRecipeProvider.has(Items.SUGAR))
            .save(provider))
        .register();
    public static final ItemEntry<Item> CREAMY_BREAD_ROLL = REGISTRATE
        .item("creamy_bread_roll", p -> new Item(p.food(ModFoods.CREAMY_BREAD_ROLL)))
        .tag(Tags.Items.FOODS)
        .recipe((ctx, provider) -> ShapelessRecipeBuilder.shapeless(
            RecipeCategory.FOOD, ctx.get())
            .requires(Items.BREAD)
            .requires(Items.SUGAR)
            .requires(ModItems.CREAM)
            .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModItems.CREAM))
            .save(provider))
        .register();
    public static final ItemEntry<Item> BEEF_MUSHROOM_STEW = REGISTRATE
        .item("beef_mushroom_stew", p -> new Item(p.food(ModFoods.BEEF_MUSHROOM_STEW)))
        .properties(properties -> properties.stacksTo(1))
        .tag(Tags.Items.FOODS)
        .register();
    public static final ItemEntry<UtusanItem> UTUSAN =
        REGISTRATE.item("utusan", UtusanItem::new).register();

    public static final ItemEntry<Item> TIN_CAN = REGISTRATE
        .item("tin_can", Item::new)
        .register();
    public static final ItemEntry<CannedFoodItem> CANNED_FOOD = REGISTRATE
        .item("canned_food", p -> new CannedFoodItem(p, TIN_CAN))
        .properties(properties -> properties.stacksTo(16))
        .tag(Tags.Items.FOODS)
        .register();

    public static final ItemEntry<SeedsPackItem> SEEDS_PACK =
        REGISTRATE.item("seeds_pack", SeedsPackItem::new).register();
    public static final ItemEntry<StructureToolItem> STRUCTURE_TOOL = REGISTRATE
        .item("structure_tool", StructureToolItem::new)
        .model((ctx, provider) -> provider.generated(ctx::get, ResourceLocation.parse("item/paper")))
        .properties(properties -> properties.stacksTo(1)
            .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true))
        .register();

    static {
        REGISTRATE.defaultCreativeTab(ModItemGroups.ANVILCRAFT_INGREDIENTS.getKey());
    }

    public static final ItemEntry<Item> CREAM =
        REGISTRATE.item("cream", Item::new).register();
    public static final ItemEntry<Item> FLOUR = REGISTRATE
        .item("flour", Item::new)
        .tag(ModItemTags.FLOUR, ModItemTags.WHEAT_FLOUR)
        .register();
    public static final ItemEntry<Item> DOUGH = REGISTRATE
        .item("dough", Item::new)
        .tag(ModItemTags.DOUGH, ModItemTags.WHEAT_DOUGH)
        .register();
    public static final ItemEntry<Item> COCOA_LIQUOR = REGISTRATE
        .item("cocoa_liquor", Item::new)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ctx.get(), 2)
                .requires(ModItems.COCOA_POWDER)
                .requires(ModItems.COCOA_POWDER)
                .requires(ModItems.COCOA_BUTTER)
                .unlockedBy("has_coco_powder", RegistrateRecipeProvider.has(ModItems.COCOA_POWDER))
                .unlockedBy("has_coco_butter", RegistrateRecipeProvider.has(ModItems.COCOA_BUTTER))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> COCOA_BUTTER =
        REGISTRATE.item("cocoa_butter", Item::new).register();
    public static final ItemEntry<Item> COCOA_POWDER =
        REGISTRATE.item("cocoa_powder", Item::new).register();

    public static final ItemEntry<Item> MAGNET_INGOT = REGISTRATE
        .item("magnet_ingot", Item::new)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.MAGNET_BLOCK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.MAGNET_BLOCK))
                .group(ctx.getId().toString())
                .save(provider, AnvilCraft.of("magnet_ingot_from_block"));
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 8)
                .requires(ModBlocks.HOLLOW_MAGNET_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.HOLLOW_MAGNET_BLOCK))
                .save(provider, AnvilCraft.of("magnet_ingot_from_hollow_block"));
        })
        .register();
    public static final ItemEntry<Item> SPONGE_GEMMULE =
        REGISTRATE.item("sponge_gemmule", Item::new).register();
    // 皇家钢系
    public static final ItemEntry<Item> ROYAL_STEEL_INGOT = REGISTRATE
        .item("royal_steel_ingot", Item::new)
        .tag(ItemTags.BEACON_PAYMENT_ITEMS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.ROYAL_STEEL_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.ROYAL_STEEL_BLOCK))
                .save(provider, AnvilCraft.of("royal_steel_ingot_from_royal_steel_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ROYAL_STEEL_INGOT)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.ROYAL_STEEL_NUGGET)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.ROYAL_STEEL_NUGGET.get()),
                    AnvilCraftDatagen.has(ModItems.ROYAL_STEEL_NUGGET))
                .save(provider, AnvilCraft.of("royal_steel_ingot_from_royal_steel_nugget"));
        })
        .register();
    public static final ItemEntry<Item> ROYAL_STEEL_NUGGET = REGISTRATE
        .item("royal_steel_nugget", Item::new)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItems.ROYAL_STEEL_INGOT)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.ROYAL_STEEL_NUGGET.get()),
                    AnvilCraftDatagen.has(ModItems.ROYAL_STEEL_NUGGET))
                .save(provider);
        })
        .register();

    // 诅咒黄金系
    public static final ItemEntry<CursedItem> CURSED_GOLD_INGOT = REGISTRATE
        .item("cursed_gold_ingot", CursedItem::new)
        .tag(ItemTags.BEACON_PAYMENT_ITEMS,
            ItemTags.PIGLIN_LOVED)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.CURSED_GOLD_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.CURSED_GOLD_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.CURSED_GOLD_BLOCK))
                .save(provider, AnvilCraft.of("cursed_gold_ingot_from_cursed_gold_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.CURSED_GOLD_NUGGET)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.CURSED_GOLD_NUGGET.get()),
                    AnvilCraftDatagen.has(ModItems.CURSED_GOLD_NUGGET))
                .save(provider, AnvilCraft.of("cursed_gold_ingot_from_cursed_gold_nugget"));
        })
        .register();
    public static final ItemEntry<CursedItem> CURSED_GOLD_NUGGET = REGISTRATE
        .item("cursed_gold_nugget", CursedItem::new)
        .tag(ItemTags.PIGLIN_LOVED)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItems.CURSED_GOLD_INGOT)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.CURSED_GOLD_INGOT.get()),
                    AnvilCraftDatagen.has(ModItems.CURSED_GOLD_INGOT))
                .save(provider);
        })
        .register();
    public static final ItemEntry<TopazItem> TOPAZ = REGISTRATE
        .item("topaz", TopazItem::new)
        .tag(ItemTags.BEACON_PAYMENT_ITEMS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.TOPAZ_BLOCK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.TOPAZ_BLOCK))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RUBY = REGISTRATE
        .item("ruby", Item::new)
        .tag(ItemTags.BEACON_PAYMENT_ITEMS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RUBY_BLOCK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.RUBY_BLOCK))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> SAPPHIRE = REGISTRATE
        .item("sapphire", Item::new)
        .tag(ItemTags.BEACON_PAYMENT_ITEMS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.SAPPHIRE_BLOCK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.SAPPHIRE_BLOCK))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RESIN = REGISTRATE
        .item("resin", Item::new)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RESIN_BLOCK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.RESIN_BLOCK))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> AMBER = REGISTRATE
        .item("amber", Item::new)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.AMBER_BLOCK)
                .unlockedBy("hasitem", RegistrateRecipeProvider.has(ModBlocks.AMBER_BLOCK))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> HARDEND_RESIN = REGISTRATE
        .item("hardend_resin", Item::new)
        .register();
    public static final ItemEntry<Item> WOOD_FIBER = REGISTRATE
        .item("wood_fiber", Item::new)
        .register();
    public static final ItemEntry<Item> CIRCUIT_BOARD = REGISTRATE
        .item("circuit_board", Item::new)
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("ABA")
                .pattern("CCC")
                .define('A', Tags.Items.INGOTS_COPPER)
                .define('B', Tags.Items.GEMS_QUARTZ)
                .define('C', ModItems.HARDEND_RESIN)
                .unlockedBy("hasitem", AnvilCraftDatagen.has(Tags.Items.GEMS_QUARTZ))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> PRISMARINE_BLADE = REGISTRATE
        .item("prismarine_blade", Item::new)
        .register();
    public static final ItemEntry<Item> PRISMARINE_CLUSTER =
        REGISTRATE.item("prismarine_cluster", Item::new).register();
    public static final ItemEntry<Item> SEA_HEART_SHELL =
        REGISTRATE.item("sea_heart_shell", Item::new).register();
    public static final ItemEntry<Item> SEA_HEART_SHELL_SHARD =
        REGISTRATE.item("sea_heart_shell_shard", Item::new).register();

    public static final ItemEntry<Item> TUNGSTEN_NUGGET = REGISTRATE
        .item("tungsten_nugget", Item::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .tag(ModItemTags.TUNGSTEN_NUGGETS, ModItemTags.TUNGSTEN_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.TUNGSTEN_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.TUNGSTEN_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.TUNGSTEN_INGOTS))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> TUNGSTEN_INGOT = REGISTRATE
        .item("tungsten_ingot", Item::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .tag(ModItemTags.TUNGSTEN_INGOTS, ModItemTags.TUNGSTEN_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.TUNGSTEN_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.TUNGSTEN_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.TUNGSTEN_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.TUNGSTEN_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.TUNGSTEN_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.TUNGSTEN_NUGGETS))
                .save(provider);
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModItems.RAW_TUNGSTEN),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_TUNGSTEN))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModItems.RAW_TUNGSTEN),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_TUNGSTEN))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModBlocks.DEEPSLATE_TUNGSTEN_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_TUNGSTEN_ORE))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName() + "_from_ore"));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModBlocks.DEEPSLATE_TUNGSTEN_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_TUNGSTEN_ORE))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName() + "_from_ore"));
        })
        .register();
    public static final ItemEntry<Item> TITANIUM_NUGGET = REGISTRATE
        .item("titanium_nugget", Item::new)
        .tag(ModItemTags.TITANIUM_NUGGETS, ModItemTags.TITANIUM_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.TITANIUM_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.TITANIUM_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.TITANIUM_INGOTS))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> TITANIUM_INGOT = REGISTRATE
        .item("titanium_ingot", Item::new)
        .tag(ModItemTags.TITANIUM_INGOTS, ModItemTags.TITANIUM_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.TITANIUM_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.TITANIUM_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.TITANIUM_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.TITANIUM_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.TITANIUM_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.TITANIUM_NUGGETS))
                .save(provider);
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModItems.RAW_TITANIUM),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_TITANIUM))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModItems.RAW_TITANIUM),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_TITANIUM))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModBlocks.DEEPSLATE_TITANIUM_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_TITANIUM_ORE))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName() + "_from_ore"));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModBlocks.DEEPSLATE_TITANIUM_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_TITANIUM_ORE))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName() + "_from_ore"));
        })
        .register();
    public static final ItemEntry<Item> ZINC_NUGGET = REGISTRATE
        .item("zinc_nugget", Item::new)
        .tag(ModItemTags.ZINC_NUGGETS, ModItemTags.ZINC_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.ZINC_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.ZINC_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.ZINC_INGOTS))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> ZINC_INGOT = REGISTRATE
        .item("zinc_ingot", Item::new)
        .tag(ModItemTags.ZINC_INGOTS, ModItemTags.ZINC_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.ZINC_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.ZINC_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.ZINC_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.ZINC_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.ZINC_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.ZINC_NUGGETS))
                .save(provider);
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModItems.RAW_ZINC),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_ZINC))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModItems.RAW_ZINC),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_ZINC))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModBlocks.DEEPSLATE_ZINC_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_ZINC_ORE))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName() + "_from_ore"));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModBlocks.DEEPSLATE_ZINC_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_ZINC_ORE))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName() + "_from_ore"));
        })
        .register();
    public static final ItemEntry<Item> TIN_NUGGET = REGISTRATE
        .item("tin_nugget", Item::new)
        .tag(ModItemTags.TIN_NUGGETS, ModItemTags.TIN_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.TIN_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.TIN_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.TIN_INGOTS))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> TIN_INGOT = REGISTRATE
        .item("tin_ingot", Item::new)
        .tag(ModItemTags.TIN_INGOTS, ModItemTags.TIN_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.TIN_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.TIN_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.TIN_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.TIN_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.TIN_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.TIN_NUGGETS))
                .save(provider);
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModItems.RAW_TIN),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_TIN))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModItems.RAW_TIN),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_TIN))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModBlocks.DEEPSLATE_TIN_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_TIN_ORE))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName() + "_from_ore"));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModBlocks.DEEPSLATE_TIN_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_TIN_ORE))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName() + "_from_ore"));
        })
        .register();
    public static final ItemEntry<Item> LEAD_NUGGET = REGISTRATE
        .item("lead_nugget", Item::new)
        .tag(ModItemTags.LEAD_NUGGETS, ModItemTags.LEAD_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.LEAD_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.LEAD_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.LEAD_INGOTS))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> LEAD_INGOT = REGISTRATE
        .item("lead_ingot", Item::new)
        .tag(ModItemTags.LEAD_INGOTS, ModItemTags.LEAD_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.LEAD_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.LEAD_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.LEAD_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.LEAD_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.LEAD_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.LEAD_NUGGETS))
                .save(provider);
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModItems.RAW_LEAD),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_LEAD))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModItems.RAW_LEAD),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_LEAD))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModBlocks.DEEPSLATE_LEAD_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_LEAD_ORE))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName() + "_from_ore"));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModBlocks.DEEPSLATE_LEAD_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_LEAD_ORE))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName() + "_from_ore"));
        })
        .register();
    public static final ItemEntry<Item> SILVER_NUGGET = REGISTRATE
        .item("silver_nugget", Item::new)
        .tag(ModItemTags.SILVER_NUGGETS, ModItemTags.SILVER_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.SILVER_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.SILVER_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.SILVER_INGOTS))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> SILVER_INGOT = REGISTRATE
        .item("silver_ingot", Item::new)
        .tag(ModItemTags.SILVER_INGOTS, ModItemTags.SILVER_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.SILVER_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.SILVER_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.SILVER_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.SILVER_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.SILVER_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.SILVER_NUGGETS))
                .save(provider);
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModItems.RAW_SILVER),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_SILVER))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModItems.RAW_SILVER),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_SILVER))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName()));

            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModBlocks.DEEPSLATE_SILVER_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_SILVER_ORE))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName() + "_from_ore"));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModBlocks.DEEPSLATE_SILVER_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_SILVER_ORE))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName() + "_from_ore"));
        })
        .register();
    public static final ItemEntry<Item> URANIUM_NUGGET = REGISTRATE
        .item("uranium_nugget", Item::new)
        .tag(ModItemTags.URANIUM_NUGGETS, ModItemTags.URANIUM_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.URANIUM_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.URANIUM_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.URANIUM_INGOTS))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> URANIUM_INGOT = REGISTRATE
        .item("uranium_ingot", Item::new)
        .tag(ModItemTags.URANIUM_INGOTS, ModItemTags.URANIUM_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.URANIUM_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.URANIUM_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.URANIUM_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.URANIUM_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.URANIUM_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.URANIUM_NUGGETS))
                .save(provider);
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModItems.RAW_URANIUM),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_URANIUM))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModItems.RAW_URANIUM),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModItems.RAW_URANIUM))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName()));
            SimpleCookingRecipeBuilder.smelting(
                    Ingredient.of(ModBlocks.DEEPSLATE_URANIUM_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    200)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_URANIUM_ORE))
                .save(provider, AnvilCraft.of("smelting/" + ctx.getName() + "_from_ore"));
            SimpleCookingRecipeBuilder.blasting(
                    Ingredient.of(ModBlocks.DEEPSLATE_URANIUM_ORE),
                    RecipeCategory.MISC,
                    ctx.get(),
                    1,
                    100)
                .group(ctx.getId().toString())
                .unlockedBy("has_item", AnvilCraftDatagen.has(ModBlocks.DEEPSLATE_URANIUM_ORE))
                .save(provider, AnvilCraft.of("blasting/" + ctx.getName() + "_from_ore"));
        })
        .register();
    public static final ItemEntry<Item> COPPER_NUGGET = REGISTRATE
        .item("copper_nugget", Item::new)
        .tag(ModItemTags.COPPER_NUGGETS, ModItemTags.COPPER_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(Ingredient.of(Items.COPPER_INGOT))
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(Items.COPPER_INGOT),
                    RegistrateRecipeProvider.has(Items.COPPER_INGOT))
                .save(provider);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COPPER_INGOT)
                .requires(ctx.get(), 9)
                .unlockedBy(AnvilCraftDatagen.hasItem(ctx.get()), RegistrateRecipeProvider.has(ctx.get()))
                .save(provider, AnvilCraft.of("copper_ingot_from_nugget"));
        })
        .register();

    public static final ItemEntry<Item> BRONZE_INGOT = REGISTRATE
        .item("bronze_ingot", Item::new)
        .tag(ModItemTags.BRONZE_INGOTS, ModItemTags.BRONZE_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.BRONZE_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.BRONZE_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.BRONZE_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.BRONZE_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.BRONZE_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.BRONZE_NUGGETS))
                .save(provider);
        })
        .register();

    public static final ItemEntry<Item> BRONZE_NUGGET = REGISTRATE
        .item("bronze_nugget", Item::new)
        .tag(ModItemTags.BRONZE_NUGGETS, ModItemTags.BRONZE_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.BRONZE_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.BRONZE_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.BRONZE_INGOTS))
                .save(provider);
        })
        .register();

    public static final ItemEntry<Item> BRASS_INGOT = REGISTRATE
        .item("brass_ingot", Item::new)
        .tag(ModItemTags.BRASS_INGOTS, ModItemTags.BRASS_INGOTS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.BRASS_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.BRASS_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.BRASS_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItemTags.BRASS_NUGGETS)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.BRASS_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.BRASS_NUGGETS))
                .save(provider);
        })
        .register();

    public static final ItemEntry<Item> BRASS_NUGGET = REGISTRATE
        .item("brass_nugget", Item::new)
        .tag(ModItemTags.BRASS_NUGGETS, ModItemTags.BRASS_NUGGETS)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItemTags.BRASS_INGOTS)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.BRASS_INGOTS),
                    RegistrateRecipeProvider.has(ModItemTags.BRASS_INGOTS))
                .save(provider);
        })
        .register();

    public static final ItemEntry<Item> AMULET_BOX =
        REGISTRATE.item("amulet_box", Item::new).register();

    public static final ItemEntry<Item> NETHERITE_CRYSTAL_NUCLEUS = REGISTRATE
        .item("netherite_crystal_nucleus", Item::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .recipe((ctx, provider) -> {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("ABA")
                .define('A', ModBlocks.TUNGSTEN_PRESSURE_PLATE)
                .define('B', Items.NETHERITE_SCRAP)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.TUNGSTEN_PRESSURE_PLATE),
                    RegistrateRecipeProvider.has(ModBlocks.TUNGSTEN_PRESSURE_PLATE))
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(Items.NETHERITE_SCRAP),
                    RegistrateRecipeProvider.has(Items.NETHERITE_SCRAP))
                .save(provider);
        })
        .register();

    public static final ItemEntry<Item> LIME_POWDER =
        REGISTRATE.item("lime_powder", Item::new).register();

    public static final ItemEntry<LevitationPowderItem> LEVITATION_POWDER =
        REGISTRATE.item("levitation_powder", LevitationPowderItem::new).register();

    public static final ItemEntry<Item> RAW_ZINC = REGISTRATE
        .item("raw_zinc", Item::new)
        .tag(ModItemTags.RAW_ORES, ModItemTags.RAW_ORES, ModItemTags.RAW_ZINC, ModItemTags.RAW_ZINC)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RAW_ZINC)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.RAW_ZINC), AnvilCraftDatagen.has(ModBlocks.RAW_ZINC))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RAW_TIN = REGISTRATE
        .item("raw_tin", Item::new)
        .tag(ModItemTags.RAW_ORES, ModItemTags.RAW_ORES, ModItemTags.RAW_TIN, ModItemTags.RAW_TIN)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RAW_TIN)
                .unlockedBy(AnvilCraftDatagen.hasItem(ModBlocks.RAW_TIN), AnvilCraftDatagen.has(ModBlocks.RAW_TIN))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RAW_TITANIUM = REGISTRATE
        .item("raw_titanium", Item::new)
        .tag(ModItemTags.RAW_ORES, ModItemTags.RAW_ORES, ModItemTags.RAW_TITANIUM, ModItemTags.RAW_TITANIUM)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RAW_TITANIUM)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.RAW_TITANIUM),
                    AnvilCraftDatagen.has(ModBlocks.RAW_TITANIUM))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RAW_TUNGSTEN = REGISTRATE
        .item("raw_tungsten", Item::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .tag(ModItemTags.RAW_ORES, ModItemTags.RAW_ORES, ModItemTags.RAW_TUNGSTEN, ModItemTags.RAW_TUNGSTEN)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RAW_TUNGSTEN)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.RAW_TUNGSTEN),
                    AnvilCraftDatagen.has(ModBlocks.RAW_TUNGSTEN))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RAW_LEAD = REGISTRATE
        .item("raw_lead", Item::new)
        .tag(ModItemTags.RAW_ORES, ModItemTags.RAW_ORES, ModItemTags.RAW_LEAD, ModItemTags.RAW_LEAD)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RAW_LEAD)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.RAW_LEAD), AnvilCraftDatagen.has(ModBlocks.RAW_LEAD))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RAW_SILVER = REGISTRATE
        .item("raw_silver", Item::new)
        .tag(ModItemTags.RAW_ORES, ModItemTags.RAW_ORES, ModItemTags.RAW_SILVER, ModItemTags.RAW_SILVER)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RAW_SILVER)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.RAW_SILVER),
                    AnvilCraftDatagen.has(ModBlocks.RAW_SILVER))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> RAW_URANIUM = REGISTRATE
        .item("raw_uranium", Item::new)
        .tag(ModItemTags.RAW_ORES, ModItemTags.RAW_ORES, ModItemTags.RAW_URANIUM, ModItemTags.RAW_URANIUM)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.RAW_URANIUM)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.RAW_URANIUM),
                    AnvilCraftDatagen.has(ModBlocks.RAW_URANIUM))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> VOID_MATTER = REGISTRATE
        .item("void_matter", Item::new)
        .tag(ModItemTags.VOID_RESISTANT)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.VOID_MATTER_BLOCK)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.VOID_MATTER_BLOCK),
                    AnvilCraftDatagen.has(ModBlocks.VOID_MATTER_BLOCK))
                .save(provider);
        })
        .register();
    public static final ItemEntry<Item> EARTH_CORE_SHARD = REGISTRATE
        .item("earth_core_shard", Item::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.EARTH_CORE_SHARD_BLOCK)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.EARTH_CORE_SHARD_BLOCK),
                    AnvilCraftDatagen.has(ModBlocks.EARTH_CORE_SHARD_BLOCK))
                .save(provider);
        })
        .register();

    public static final ItemEntry<? extends Item> EMBER_METAL_INGOT = REGISTRATE
        .item("ember_metal_ingot", Item::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.EMBER_METAL_BLOCK)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.EMBER_METAL_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.EMBER_METAL_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.EMBER_METAL_NUGGET)
                .group(ctx.getId().toString())
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItemTags.TUNGSTEN_NUGGETS),
                    RegistrateRecipeProvider.has(ModItemTags.TUNGSTEN_NUGGETS))
                .save(provider);
        })
        .register();

    public static final ItemEntry<? extends Item> EMBER_METAL_NUGGET = REGISTRATE
        .item("ember_metal_nugget", Item::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItems.EMBER_METAL_INGOT)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.EMBER_METAL_INGOT),
                    AnvilCraftDatagen.has(ModItems.EMBER_METAL_INGOT))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_ingot"));
        })
        .register();

    public static final ItemEntry<Item> NEGATIVE_MATTER = REGISTRATE
        .item("negative_matter", Item::new)
        .initialProperties(Item.Properties::new)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModBlocks.NEGATIVE_MATTER_BLOCK)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModBlocks.NEGATIVE_MATTER_BLOCK.asItem()),
                    AnvilCraftDatagen.has(ModBlocks.NEGATIVE_MATTER_BLOCK))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_block"));
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.NEGATIVE_MATTER_NUGGET)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.NEGATIVE_MATTER_NUGGET),
                    RegistrateRecipeProvider.has(ModItems.NEGATIVE_MATTER_NUGGET))
                .save(provider);
        })
        .register();

    public static final ItemEntry<Item> NEGATIVE_MATTER_NUGGET = REGISTRATE
        .item("negative_matter_nugget", Item::new)
        .initialProperties(Item.Properties::new)
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 9)
                .requires(ModItems.NEGATIVE_MATTER)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.NEGATIVE_MATTER),
                    AnvilCraftDatagen.has(ModItems.NEGATIVE_MATTER))
                .save(provider, AnvilCraft.of(BuiltInRegistries.ITEM.getKey(ctx.get()).getPath() + "_from_ingot"));
        })
        .register();


    public static final ItemEntry<SuperHeavyItem> NEUTRONIUM_INGOT = REGISTRATE
        .item("neutronium_ingot", SuperHeavyItem::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .register();
    public static final ItemEntry<SuperHeavyItem> STABLE_NEUTRONIUM_INGOT = REGISTRATE
        .item("stable_neutronium_ingot", SuperHeavyItem::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .recipe((ctx, provider) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get(), 1)
                .requires(ModItems.NEUTRONIUM_INGOT)
                .requires(ModItems.LEVITATION_POWDER)
                .unlockedBy(
                    AnvilCraftDatagen.hasItem(ModItems.NEUTRONIUM_INGOT),
                    AnvilCraftDatagen.has(ModItems.NEUTRONIUM_INGOT))
                .save(provider);
        })
        .register();
    public static final ItemEntry<SuperHeavyItem> CHARGED_NEUTRONIUM_INGOT = REGISTRATE
        .item("charged_neutronium_ingot", SuperHeavyItem::new)
        .initialProperties(() -> new Item.Properties().fireResistant())
        .register();

    public static final ItemEntry<BucketItem> OIL_BUCKET = REGISTRATE
        .item("oil_bucket", p -> new BucketItem(ModFluids.OIL.get(), p))
        .initialProperties(() -> new Item.Properties().stacksTo(1).craftRemainder(Items.BUCKET))
        .model(ModelProviderUtil::bucket)
        .register();

    public static final Object2ObjectMap<Color, ItemEntry<BucketItem>> CEMENT_BUCKETS = registerAllCementBuckets();

    private static Object2ObjectMap<Color, ItemEntry<BucketItem>> registerAllCementBuckets() {
        Object2ObjectMap<Color, ItemEntry<BucketItem>> map = new Object2ObjectOpenHashMap<>();
        for (Color color : Color.values()) {
            var entry = registerCementBucket(color);
            map.put(color, entry);
        }
        return map;
    }

    private static ItemEntry<BucketItem> registerCementBucket(Color color) {
        return REGISTRATE
            .item("%s_cement_bucket".formatted(color), p -> new BucketItem(ModFluids.SOURCE_CEMENTS.get(color).get(), p))
            .properties(p -> p.stacksTo(1).craftRemainder(Items.BUCKET))
            .model(ModelProviderUtil::bucket)
            .register();
    }

    public static ItemEntry<BucketItem> MELT_GEM_BUCKET = REGISTRATE
        .item("melt_gem_bucket", p -> new BucketItem(ModFluids.MELT_GEM.get(), p))
        .properties(p -> p.stacksTo(1).craftRemainder(Items.BUCKET))
        .model(ModelProviderUtil::bucket)
        .register();

    public static void register() {
    }
}
