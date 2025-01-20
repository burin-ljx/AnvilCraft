package dev.dubhe.anvilcraft.api.tooltip;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.dubhe.anvilcraft.api.tooltip.impl.AffectRangeProviderImpl;
import dev.dubhe.anvilcraft.api.tooltip.impl.HeliostatsTooltip;
import dev.dubhe.anvilcraft.api.tooltip.impl.HeliostatsTooltipProvider;
import dev.dubhe.anvilcraft.api.tooltip.impl.PowerComponentTooltipProvider;
import dev.dubhe.anvilcraft.api.tooltip.impl.RedstoneComponentTooltipProvider;
import dev.dubhe.anvilcraft.api.tooltip.impl.RubyPrismTooltipProvider;
import dev.dubhe.anvilcraft.api.tooltip.impl.SpaceOvercompressorTooltipProvider;
import dev.dubhe.anvilcraft.api.tooltip.providers.IAffectRangeProvider;
import dev.dubhe.anvilcraft.api.tooltip.providers.IAnvilHammerTooltipProvider;
import dev.dubhe.anvilcraft.api.tooltip.providers.IHandHeldItemTooltipProvider;
import dev.dubhe.anvilcraft.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.dubhe.anvilcraft.api.tooltip.TooltipRenderHelper.renderOutline;
import static dev.dubhe.anvilcraft.api.tooltip.TooltipRenderHelper.renderTooltipWithItemIcon;

public class HudTooltipManager {
    public static final HudTooltipManager INSTANCE = new HudTooltipManager();
    private static final int BACKGROUND_COLOR = 0xCC100010;
    private static final int BORDER_COLOR_TOP = 0x505000ff;
    private static final int BORDER_COLOR_BOTTOM = 0x5028007f;
    private final List<IAnvilHammerTooltipProvider> anvilHammerTooltipProviders = new ArrayList<>();
    private final List<IAffectRangeProvider> affectRangeProviders = new ArrayList<>();
    private final List<IHandHeldItemTooltipProvider> handItemProviders = new ArrayList<>();

    static {
        INSTANCE.registerAnvilHammerTooltip(new PowerComponentTooltipProvider());
        INSTANCE.registerAffectRange(new AffectRangeProviderImpl());
        INSTANCE.registerAnvilHammerTooltip(new RubyPrismTooltipProvider());
        INSTANCE.registerHandHeldItemTooltip(new HeliostatsTooltip());
        INSTANCE.registerAnvilHammerTooltip(new HeliostatsTooltipProvider());
        INSTANCE.registerAnvilHammerTooltip(new SpaceOvercompressorTooltipProvider());
        INSTANCE.registerHandHeldItemTooltip(ModItems.STRUCTURE_TOOL.get());
        INSTANCE.registerAnvilHammerTooltip(new RedstoneComponentTooltipProvider());
    }

    private void registerAffectRange(AffectRangeProviderImpl affectRangeProvider) {
        affectRangeProviders.add(affectRangeProvider);
    }

    private void registerAnvilHammerTooltip(IAnvilHammerTooltipProvider provider) {
        anvilHammerTooltipProviders.add(provider);
    }

    private void registerHandHeldItemTooltip(IHandHeldItemTooltipProvider provider) {
        handItemProviders.add(provider);
    }

    /**
     * 渲染
     */
    public void renderTooltip(
        GuiGraphics guiGraphics,
        Level level,
        BlockPos blockPos,
        float partialTick,
        int screenWidth,
        int screenHeight
    ) {
        final int tooltipPosX = screenWidth / 2 + 10;
        final int tooltipPosY = screenHeight / 2 + 10;
        Font font = Minecraft.getInstance().font;
        IAnvilHammerTooltipProvider currentProvider = determineAnvilHammerTooltipProvider(level, blockPos);
        if (currentProvider == null) return;
        List<Component> tooltip = currentProvider.tooltip(level, blockPos);
        if (tooltip == null || tooltip.isEmpty()) return;
        renderTooltipWithItemIcon(
            guiGraphics,
            font,
            currentProvider.icon(level, blockPos),
            tooltip,
            tooltipPosX,
            tooltipPosY,
            BACKGROUND_COLOR,
            BORDER_COLOR_TOP,
            BORDER_COLOR_BOTTOM);
    }

    /**
     * 渲染手持物品Tooltip
     */
    public void renderHandItemLevelTooltip(
        ItemStack itemStack,
        PoseStack poseStack,
        VertexConsumer consumer,
        double camX,
        double camY,
        double camZ
    ) {
        IHandHeldItemTooltipProvider pv = determineHandHeldItemTooltipProvider(itemStack);
        if (pv == null) return;
        pv.render(poseStack, consumer, itemStack, camX, camY, camZ);
    }

    /**
     * 渲染手持物品Hud Tooltip
     */
    public void renderHandItemHudTooltip(
        GuiGraphics guiGraphics,
        ItemStack itemStack,
        float partialTick,
        int screenWidth,
        int screenHeight
    ) {
        IHandHeldItemTooltipProvider pv = determineHandHeldItemTooltipProvider(itemStack);
        if (pv == null) return;
        pv.renderTooltip(guiGraphics, screenWidth, screenHeight);
    }

    /**
     * 渲染作用范围
     */
    public void renderAffectRange(
        BlockEntity entity,
        PoseStack poseStack,
        VertexConsumer consumer,
        double camX,
        double camY,
        double camZ
    ) {
        IAffectRangeProvider currentProvider = determineAffectRangeProvider(entity);
        if (currentProvider == null) return;
        VoxelShape shape = currentProvider.affectRange(entity);
        if (shape == null) return;
        renderOutline(poseStack, consumer, camX, camY, camZ, BlockPos.ZERO, shape, 0xff00ffcc);
    }

    private IHandHeldItemTooltipProvider determineHandHeldItemTooltipProvider(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return null;
        return handItemProviders.stream()
            .filter(it -> it.accepts(itemStack))
            .min(Comparator.comparingInt(IHandHeldItemTooltipProvider::priority))
            .orElse(null);
    }

    private IAnvilHammerTooltipProvider determineAnvilHammerTooltipProvider(Level level, BlockPos blockPos) {
        return anvilHammerTooltipProviders.stream()
            .filter(it -> it.accepts(level, blockPos))
            .min(Comparator.comparingInt(IAnvilHammerTooltipProvider::priority))
            .orElse(null);
    }

    private IAffectRangeProvider determineAffectRangeProvider(BlockEntity entity) {
        if (entity == null) return null;
        return affectRangeProviders.stream()
            .filter(it -> it.accepts(entity))
            .min(Comparator.comparingInt(IAffectRangeProvider::priority))
            .orElse(null);
    }
}
