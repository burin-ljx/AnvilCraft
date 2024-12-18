package dev.dubhe.anvilcraft.integration.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.integration.jei.AnvilCraftJeiPlugin;
import dev.dubhe.anvilcraft.integration.jei.recipe.EndPortalConversionRecipe;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import dev.dubhe.anvilcraft.integration.jei.util.TextureConstants;
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
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EndPortalConversionCategory implements IRecipeCategory<EndPortalConversionRecipe> {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final Lazy<IDrawable> background;
    private final IDrawable slot;
    private final IDrawable preRenderedEndPortal;
    private final Component title;

    private final IDrawable arrowIn;
    private final IDrawable arrowOut;

    public EndPortalConversionCategory(IGuiHelper helper) {
        background = Lazy.of(() -> helper.createBlankDrawable(WIDTH, HEIGHT));
        slot = helper.getSlotDrawable();
        preRenderedEndPortal = helper.drawableBuilder(TextureConstants.PRE_RENDERED_END_PORTAL,
            0, 0, 500, 312).setTextureSize(500, 312).build();
        title = Component.translatable("gui.anvilcraft.category.end_portal_conversion");

        arrowIn = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 31, 16, 8);
        arrowOut = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 40, 16, 10);
    }

    @Override
    public RecipeType<EndPortalConversionRecipe> getRecipeType() {
        return AnvilCraftJeiPlugin.END_PORTAL_CONVERSION;
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
        return null;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder, EndPortalConversionRecipe recipe, IFocusGroup focuses) {
        JeiSlotUtil.addInputSlots(builder, recipe.ingredients);
        JeiSlotUtil.addOutputSlots(builder, recipe.results);
    }

    @Override
    public void draw(
        EndPortalConversionRecipe recipe,
        IRecipeSlotsView recipeSlotsView,
        GuiGraphics guiGraphics,
        double mouseX,
        double mouseY) {

//        RenderHelper.renderBlock(
//            guiGraphics,
//            Blocks.END_PORTAL.defaultBlockState(),
//            81,
//            40,
//            10,
//            12,
//            RenderHelper.SINGLE_BLOCK);
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.scale(0.1f, 0.1f, 1.0f);
        preRenderedEndPortal.draw(guiGraphics, 570, 350);
        pose.popPose();
        arrowIn.draw(guiGraphics, 54, 32);
        arrowOut.draw(guiGraphics, 92, 31);

        JeiSlotUtil.drawInputSlots(guiGraphics, slot, recipe.ingredients.size());
        JeiSlotUtil.drawOutputSlots(guiGraphics, slot, recipe.results.size());
    }

    public static void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
            AnvilCraftJeiPlugin.END_PORTAL_CONVERSION,
            EndPortalConversionRecipe.getAllRecipes());
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(Blocks.END_PORTAL_FRAME), AnvilCraftJeiPlugin.END_PORTAL_CONVERSION);
    }
}
