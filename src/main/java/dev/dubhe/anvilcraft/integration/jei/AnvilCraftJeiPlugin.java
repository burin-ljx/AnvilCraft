package dev.dubhe.anvilcraft.integration.jei;

import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.client.gui.screen.JewelCraftingScreen;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.integration.jei.category.BeaconConversionCategory;
import dev.dubhe.anvilcraft.integration.jei.category.EndPortalConversionCategory;
import dev.dubhe.anvilcraft.integration.jei.category.JewelCraftingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.VoidDecayCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.BlockCompressCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.BlockCrushCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.BoilingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.BulgingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.CementStainingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.ConcreteCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.CookingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.CrushingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.ItemCompressCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.UnpackCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.ItemInjectCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.MassInjectCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.MeshRecipeCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.SqueezingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.StampingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.SuperHeatingCategory;
import dev.dubhe.anvilcraft.integration.jei.category.anvil.TimeWarpCategory;
import dev.dubhe.anvilcraft.integration.jei.category.extension.CanningFoodExtension;
import dev.dubhe.anvilcraft.integration.jei.category.multiblock.MultiBlockConversionCategory;
import dev.dubhe.anvilcraft.integration.jei.category.multiblock.MultiBlockCraftingCategory;
import dev.dubhe.anvilcraft.integration.jei.recipe.BeaconConversionRecipe;
import dev.dubhe.anvilcraft.integration.jei.recipe.CementStainingRecipe;
import dev.dubhe.anvilcraft.integration.jei.recipe.ColoredConcreteRecipe;
import dev.dubhe.anvilcraft.integration.jei.recipe.EndPortalConversionRecipe;
import dev.dubhe.anvilcraft.integration.jei.recipe.MeshRecipeGroup;
import dev.dubhe.anvilcraft.integration.jei.recipe.VoidDecayRecipe;
import dev.dubhe.anvilcraft.recipe.CanningFoodRecipe;
import dev.dubhe.anvilcraft.recipe.JewelCraftingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.BlockCompressRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.BlockCrushRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.BoilingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.BulgingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.CookingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.CrushingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.ItemCompressRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.UnpackRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.ItemInjectRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.MassInjectRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.SqueezingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.StampingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.SuperHeatingRecipe;
import dev.dubhe.anvilcraft.recipe.anvil.TimeWarpRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockConversionRecipe;
import dev.dubhe.anvilcraft.recipe.multiblock.MultiblockRecipe;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;

import javax.annotation.ParametersAreNonnullByDefault;

@JeiPlugin
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AnvilCraftJeiPlugin implements IModPlugin {

    public static final RecipeType<MeshRecipeGroup> MESH = createRecipeType("mesh", MeshRecipeGroup.class);
    public static final RecipeType<CementStainingRecipe> CEMENT_STAINING =
        createRecipeType("cement_staining", CementStainingRecipe.class);
    public static final RecipeType<ColoredConcreteRecipe> COLORED_CONCRETE =
        createRecipeType("colored_concrete", ColoredConcreteRecipe.class);
    public static final RecipeType<EndPortalConversionRecipe> END_PORTAL_CONVERSION =
        createRecipeType("end_portal_conversion", EndPortalConversionRecipe.class);
    public static final RecipeType<BeaconConversionRecipe> BEACON_CONVERSION =
        createRecipeType("beacon_conversion", BeaconConversionRecipe.class);
    public static final RecipeType<VoidDecayRecipe> VOID_DECAY =
        createRecipeType("void_decay", VoidDecayRecipe.class);

    public static final RecipeType<RecipeHolder<BlockCompressRecipe>> BLOCK_COMPRESS =
        createRecipeHolderType("block_compress");
    public static final RecipeType<RecipeHolder<BlockCrushRecipe>> BLOCK_CRUSH = createRecipeHolderType("block_crush");
    public static final RecipeType<RecipeHolder<ItemInjectRecipe>> ITEM_INJECT = createRecipeHolderType("item_inject");
    public static final RecipeType<RecipeHolder<MassInjectRecipe>> MASS_INJECT = createRecipeHolderType("mass_inject");
    public static final RecipeType<RecipeHolder<ItemCompressRecipe>> ITEM_COMPRESS =
        createRecipeHolderType("item_compress");
    public static final RecipeType<RecipeHolder<UnpackRecipe>> UNPACK = createRecipeHolderType("unpack");
    public static final RecipeType<RecipeHolder<CookingRecipe>> COOKING = createRecipeHolderType("cooking");
    public static final RecipeType<RecipeHolder<BoilingRecipe>> BOILING = createRecipeHolderType("boiling");
    public static final RecipeType<RecipeHolder<StampingRecipe>> STAMPING = createRecipeHolderType("stamping");
    public static final RecipeType<RecipeHolder<SuperHeatingRecipe>> SUPER_HEATING =
        createRecipeHolderType("super_heating");
    public static final RecipeType<RecipeHolder<SqueezingRecipe>> SQUEEZING = createRecipeHolderType("squeezing");
    public static final RecipeType<RecipeHolder<BulgingRecipe>> BULGING = createRecipeHolderType("bulging");
    public static final RecipeType<RecipeHolder<TimeWarpRecipe>> TIME_WARP = createRecipeHolderType("time_warp");

    public static final RecipeType<RecipeHolder<MultiblockRecipe>> MULTI_BLOCK =
        createRecipeHolderType("multiblock");
    public static final RecipeType<RecipeHolder<MultiblockConversionRecipe>> MULTIBLOCK_CONVERSION =
        createRecipeHolderType("multiblock_conversion");

    public static final RecipeType<RecipeHolder<JewelCraftingRecipe>> JEWEL_CRAFTING =
        createRecipeHolderType("jewel_crafting");
    public static final RecipeType<RecipeHolder<CrushingRecipe>> CRUSHING = createRecipeHolderType("crushing");

    @Override
    public ResourceLocation getPluginUid() {
        return AnvilCraft.of("jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        MeshRecipeCategory.registerRecipes(registration);
        BlockCompressCategory.registerRecipes(registration);
        BlockCrushCategory.registerRecipes(registration);
        SqueezingCategory.registerRecipes(registration);
        ItemInjectCategory.registerRecipes(registration);
        MassInjectCategory.registerRecipes(registration);
        ItemCompressCategory.registerRecipes(registration);
        UnpackCategory.registerRecipes(registration);
        CookingCategory.registerRecipes(registration);
        BoilingCategory.registerRecipes(registration);
        StampingCategory.registerRecipes(registration);
        SuperHeatingCategory.registerRecipes(registration);
        CementStainingCategory.registerRecipes(registration);
        ConcreteCategory.registerRecipes(registration);
        BulgingCategory.registerRecipes(registration);
        TimeWarpCategory.registerRecipes(registration);
        MultiBlockCraftingCategory.registerRecipes(registration);
        MultiBlockConversionCategory.registerRecipes(registration);
        JewelCraftingCategory.registerRecipes(registration);
        EndPortalConversionCategory.registerRecipes(registration);
        BeaconConversionCategory.registerRecipes(registration);
        VoidDecayCategory.registerRecipes(registration);
        CrushingCategory.registerRecipe(registration);

        registration.addItemStackInfo(
            new ItemStack(ModItems.GEODE.get()),
            Component.translatable("jei.anvilcraft.info.geode_1"),
            Component.translatable("jei.anvilcraft.info.geode_2"),
            Component.translatable("jei.anvilcraft.info.geode_3"),
            Component.translatable("jei.anvilcraft.info.geode_4"));
        registration.addItemStackInfo(
            new ItemStack(ModItems.ROYAL_STEEL_UPGRADE_SMITHING_TEMPLATE.get()),
            Component.translatable("jei.anvilcraft.info.royal_steel_upgrade_smithing_template_1"),
            Component.translatable("jei.anvilcraft.info.royal_steel_upgrade_smithing_template_2"));
        registration.addItemStackInfo(
            new ItemStack(ModItems.CRAB_CLAW.get()),
            Component.translatable("jei.anvilcraft.info.craw_claw"));
        registration.addItemStackInfo(
            new ItemStack(ModItems.CAPACITOR.get()),
            Component.translatable("jei.anvilcraft.info.capacitor"));
        registration.addItemStackInfo(
            ModBlocks.END_DUST.asStack(),
            Component.translatable("jei.anvilcraft.info.end_dust"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        MeshRecipeCategory.registerRecipeCatalysts(registration);
        BlockCompressCategory.registerRecipeCatalysts(registration);
        BlockCrushCategory.registerRecipeCatalysts(registration);
        SqueezingCategory.registerRecipeCatalysts(registration);
        ItemInjectCategory.registerRecipeCatalysts(registration);
        MassInjectCategory.registerRecipeCatalysts(registration);
        ItemCompressCategory.registerRecipeCatalysts(registration);
        UnpackCategory.registerRecipeCatalysts(registration);
        CookingCategory.registerRecipeCatalysts(registration);
        BoilingCategory.registerRecipeCatalysts(registration);
        StampingCategory.registerRecipeCatalysts(registration);
        SuperHeatingCategory.registerRecipeCatalysts(registration);
        CementStainingCategory.registerRecipeCatalysts(registration);
        ConcreteCategory.registerRecipeCatalysts(registration);
        BulgingCategory.registerRecipeCatalysts(registration);
        TimeWarpCategory.registerRecipeCatalysts(registration);
        MultiBlockCraftingCategory.registerRecipeCatalysts(registration);
        MultiBlockConversionCategory.registerRecipeCatalysts(registration);
        JewelCraftingCategory.registerRecipeCatalysts(registration);
        EndPortalConversionCategory.registerRecipeCatalysts(registration);
        BeaconConversionCategory.registerRecipeCatalysts(registration);
        VoidDecayCategory.registerRecipeCatalysts(registration);
        CrushingCategory.registerRecipeCatalysts(registration);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BATCH_CRAFTER), RecipeTypes.CRAFTING);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROYAL_ANVIL), RecipeTypes.ANVIL);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMBER_ANVIL), RecipeTypes.ANVIL);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GIANT_ANVIL), RecipeTypes.ANVIL);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SPECTRAL_ANVIL), RecipeTypes.ANVIL);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROYAL_SMITHING_TABLE), RecipeTypes.SMITHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMBER_SMITHING_TABLE), RecipeTypes.SMITHING);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(new MeshRecipeCategory(guiHelper));
        registration.addRecipeCategories(new BlockCompressCategory(guiHelper));
        registration.addRecipeCategories(new BlockCrushCategory(guiHelper));
        registration.addRecipeCategories(new SqueezingCategory(guiHelper));
        registration.addRecipeCategories(new ItemInjectCategory(guiHelper));
        registration.addRecipeCategories(new MassInjectCategory(guiHelper));
        registration.addRecipeCategories(new ItemCompressCategory(guiHelper));
        registration.addRecipeCategories(new UnpackCategory(guiHelper));
        registration.addRecipeCategories(new CookingCategory(guiHelper));
        registration.addRecipeCategories(new BoilingCategory(guiHelper));
        registration.addRecipeCategories(new StampingCategory(guiHelper));
        registration.addRecipeCategories(new SuperHeatingCategory(guiHelper));
        registration.addRecipeCategories(new CementStainingCategory(guiHelper));
        registration.addRecipeCategories(new ConcreteCategory(guiHelper));
        registration.addRecipeCategories(new BulgingCategory(guiHelper));
        registration.addRecipeCategories(new TimeWarpCategory(guiHelper));
        registration.addRecipeCategories(new MultiBlockCraftingCategory(guiHelper));
        registration.addRecipeCategories(new MultiBlockConversionCategory(guiHelper));
        registration.addRecipeCategories(new JewelCraftingCategory(guiHelper));
        registration.addRecipeCategories(new EndPortalConversionCategory(guiHelper));
        registration.addRecipeCategories(new BeaconConversionCategory(guiHelper));
        registration.addRecipeCategories(new VoidDecayCategory(guiHelper));
        registration.addRecipeCategories(new CrushingCategory(guiHelper));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(
            JewelCraftingScreen.class,
            100,
            53,
            30,
            13,
            JEWEL_CRAFTING
        );
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addExtension(CanningFoodRecipe.class,
            CanningFoodExtension.INSTANCE);
    }

    public static <T> RecipeType<T> createRecipeType(String name, Class<T> clazz) {
        return new RecipeType<>(AnvilCraft.of(name), clazz);
    }

    public static <R extends Recipe<?>> RecipeType<RecipeHolder<R>> createRecipeHolderType(String name) {
        return RecipeType.createRecipeHolderType(AnvilCraft.of(name));
    }
}
