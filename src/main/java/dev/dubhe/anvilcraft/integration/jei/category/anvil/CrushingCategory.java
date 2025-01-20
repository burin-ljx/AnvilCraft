package dev.dubhe.anvilcraft.integration.jei.category.anvil;

import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.integration.jei.AnvilCraftJeiPlugin;
import dev.dubhe.anvilcraft.integration.jei.drawable.DrawableBlockStateIcon;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import dev.dubhe.anvilcraft.integration.jei.util.TextureConstants;
import dev.dubhe.anvilcraft.recipe.anvil.CrushingRecipe;
import dev.dubhe.anvilcraft.util.RenderHelper;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CrushingCategory implements IRecipeCategory<RecipeHolder<CrushingRecipe>> {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final Lazy<IDrawable> background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final Component title;
    private final ITickTimer timer;

    private final IDrawable arrowIn;
    private final IDrawable arrowOut;

    public CrushingCategory(IGuiHelper helper) {
        background = net.neoforged.neoforge.common.util.Lazy.of(() -> helper.createBlankDrawable(WIDTH, HEIGHT));
        icon = new DrawableBlockStateIcon(
                Blocks.ANVIL.defaultBlockState(), ModBlocks.CRUSHING_PLATFORM.getDefaultState());
        slot = helper.getSlotDrawable();
        title = Component.translatable("gui.anvilcraft.category.crushing");
        timer = helper.createTickTimer(30, 60, true);

        arrowIn = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 31, 16, 8);
        arrowOut = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 40, 16, 10);
    }

    @Override
    public RecipeType<RecipeHolder<CrushingRecipe>> getRecipeType() {
        return AnvilCraftJeiPlugin.CRUSHING;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background.get();
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CrushingRecipe> recipe, IFocusGroup focuses) {
        CrushingRecipe crushingRecipe = recipe.value();
        JeiSlotUtil.addInputSlots(builder, crushingRecipe.mergedIngredients);
        JeiSlotUtil.addOutputSlots(builder, crushingRecipe.results);
    }

    @Override
    public void draw(RecipeHolder<CrushingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        CrushingRecipe crushingRecipe = recipe.value();
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);
        RenderHelper.renderBlock(
                guiGraphics,
                Blocks.ANVIL.defaultBlockState(),
                81,
                22 + anvilYOffset,
                20,
                12,
                RenderHelper.SINGLE_BLOCK);
        RenderHelper.renderBlock(
                guiGraphics, ModBlocks.CRUSHING_PLATFORM.getDefaultState(), 81, 40, 0, 12, RenderHelper.SINGLE_BLOCK);

        arrowIn.draw(guiGraphics, 54, 32);
        arrowOut.draw(guiGraphics, 92, 31);

        JeiSlotUtil.drawInputSlots(guiGraphics, slot, crushingRecipe.mergedIngredients.size());
        JeiSlotUtil.drawOutputSlots(guiGraphics, slot, crushingRecipe.results.size());
    }

    public static void registerRecipe(IRecipeRegistration registration) {
        registration.addRecipes(
                AnvilCraftJeiPlugin.CRUSHING,
                JeiRecipeUtil.getRecipeHoldersFromType(ModRecipeTypes.CRUSHING_TYPE.get()));
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), AnvilCraftJeiPlugin.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ROYAL_ANVIL), AnvilCraftJeiPlugin.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.EMBER_ANVIL), AnvilCraftJeiPlugin.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GIANT_ANVIL), AnvilCraftJeiPlugin.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SPECTRAL_ANVIL), AnvilCraftJeiPlugin.CRUSHING);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRUSHING_PLATFORM), AnvilCraftJeiPlugin.CRUSHING);
    }
}
