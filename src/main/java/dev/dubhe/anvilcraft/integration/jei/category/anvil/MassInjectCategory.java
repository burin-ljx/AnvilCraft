package dev.dubhe.anvilcraft.integration.jei.category.anvil;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.init.ModBlocks;
import dev.dubhe.anvilcraft.init.ModItems;
import dev.dubhe.anvilcraft.init.ModRecipeTypes;
import dev.dubhe.anvilcraft.integration.jei.AnvilCraftJeiPlugin;
import dev.dubhe.anvilcraft.integration.jei.drawable.DrawableBlockStateIcon;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRecipeUtil;
import dev.dubhe.anvilcraft.integration.jei.util.JeiRenderHelper;
import dev.dubhe.anvilcraft.integration.jei.util.JeiSlotUtil;
import dev.dubhe.anvilcraft.integration.jei.util.TextureConstants;
import dev.dubhe.anvilcraft.recipe.anvil.MassInjectRecipe;
import dev.dubhe.anvilcraft.util.RenderHelper;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.util.Lazy;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;

import static dev.dubhe.anvilcraft.block.entity.SpaceOvercompressorBlockEntity.NEUTRONIUM_INGOT_MASS;
import static dev.dubhe.anvilcraft.block.entity.SpaceOvercompressorBlockEntity.DISPLAYED_MASS;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MassInjectCategory implements IRecipeCategory<RecipeHolder<MassInjectRecipe>> {
    public static final int WIDTH = 162;
    public static final int HEIGHT = 64;

    private final String KEY_MASS_VALUE = "gui.anvilcraft.category.mass_inject.mass_value";
    private final String KEY_MASS_NEEDED = "gui.anvilcraft.category.mass_inject.mass_needed";
    private final String KEY_ITEMS_NEEDED = "gui.anvilcraft.category.mass_inject.items_needed";

    private final Lazy<IDrawable> background;
    private final IDrawable icon;
    private final IDrawable slot;
    private final Component title;
    private final ITickTimer timer;

    private final IDrawable arrowIn;
    private final IDrawable arrowOut;

    public MassInjectCategory(IGuiHelper helper) {
        background = Lazy.of(() -> helper.createBlankDrawable(WIDTH, HEIGHT));
        icon = new DrawableBlockStateIcon(Blocks.ANVIL.defaultBlockState(),
            ModBlocks.SPACE_OVERCOMPRESSOR.getDefaultState());
        slot = helper.getSlotDrawable();
        title = Component.translatable("gui.anvilcraft.category.mass_inject");
        timer = helper.createTickTimer(30, 60, true);

        arrowIn = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 31, 16, 8);
        arrowOut = helper.createDrawable(TextureConstants.ANVIL_CRAFT_SPRITES, 0, 40, 16, 10);
    }

    @Override
    public RecipeType<RecipeHolder<MassInjectRecipe>> getRecipeType() {
        return AnvilCraftJeiPlugin.MASS_INJECT;
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
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(
            IRecipeLayoutBuilder builder, RecipeHolder<MassInjectRecipe> recipeHolder, IFocusGroup focuses) {
        MassInjectRecipe recipe = recipeHolder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 21, 24)
            .addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 24)
            .addItemStack(ModItems.NEUTRONIUM_INGOT.asStack())
            .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                tooltip.add(Component.translatable(KEY_MASS_NEEDED, DISPLAYED_MASS).withStyle(ChatFormatting.GOLD));
            });
    }

    @Override
    public void draw(
            RecipeHolder<MassInjectRecipe> recipeHolder,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY) {
        MassInjectRecipe recipe = recipeHolder.value();
        float anvilYOffset = JeiRenderHelper.getAnvilAnimationOffset(timer);
        RenderHelper.renderBlock(
                guiGraphics,
                Blocks.ANVIL.defaultBlockState(),
                81,
                22 + anvilYOffset,
                20,
                12,
                RenderHelper.SINGLE_BLOCK);
        RenderHelper.renderBlock(guiGraphics, ModBlocks.SPACE_OVERCOMPRESSOR.getDefaultState(),
            81, 40, 10, 12, RenderHelper.SINGLE_BLOCK);

        arrowIn.draw(guiGraphics, 54, 32);
        arrowOut.draw(guiGraphics, 92, 31);

        JeiSlotUtil.drawInputSlots(guiGraphics, slot, 1);
        JeiSlotUtil.drawOutputSlots(guiGraphics, slot, 1);

        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.scale(0.8f, 0.8f, 1.0f);
        guiGraphics.drawString(Minecraft.getInstance().font,
            Component.translatable(KEY_MASS_VALUE, recipe.displayMassValue()),
            0, 10, 0xFF000000, false);
        guiGraphics.drawString(Minecraft.getInstance().font,
            Component.translatable(KEY_ITEMS_NEEDED, Math.ceilDiv(NEUTRONIUM_INGOT_MASS, recipe.getMass())),
            0, 70, 0xFF000000, false);
        pose.popPose();
    }

    public static void registerRecipes(IRecipeRegistration registration) {
        List<RecipeHolder<MassInjectRecipe>> recipes =
            JeiRecipeUtil.getRecipeHoldersFromType(ModRecipeTypes.MASS_INJECT_TYPE.get());
        recipes.sort(Comparator.comparingInt(recipe -> recipe.value().getMass()));
        registration.addRecipes(AnvilCraftJeiPlugin.MASS_INJECT, recipes.reversed());
    }

    public static void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(ModBlocks.SPACE_OVERCOMPRESSOR.asStack(), AnvilCraftJeiPlugin.MASS_INJECT);
        registration.addRecipeCatalyst(new ItemStack(Items.ANVIL), AnvilCraftJeiPlugin.MASS_INJECT);
        registration.addRecipeCatalyst(ModBlocks.ROYAL_ANVIL.asStack(), AnvilCraftJeiPlugin.MASS_INJECT);
        registration.addRecipeCatalyst(ModBlocks.EMBER_ANVIL.asStack(), AnvilCraftJeiPlugin.MASS_INJECT);
        registration.addRecipeCatalyst(ModBlocks.GIANT_ANVIL.asStack(), AnvilCraftJeiPlugin.MASS_INJECT);
        registration.addRecipeCatalyst(ModBlocks.SPECTRAL_ANVIL.asStack(), AnvilCraftJeiPlugin.MASS_INJECT);
    }
}
