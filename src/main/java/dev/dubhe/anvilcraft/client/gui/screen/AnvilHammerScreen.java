package dev.dubhe.anvilcraft.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import dev.dubhe.anvilcraft.api.hammer.IHasHammerEffect;
import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import dev.dubhe.anvilcraft.network.HammerChangeBlockPacket;
import dev.dubhe.anvilcraft.util.MathUtil;
import dev.dubhe.anvilcraft.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Vector2f;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class AnvilHammerScreen extends Screen implements IHasHammerEffect {
    public static final int RADIUS = 80;
    public static final int DELAY = 80;//ms
    public static final int ANIMATION_T = 300;//ms
    public static final float ZOOM_SELECTED = 15f;
    public static final float ZOOM_UNSELECTED = 13.5f;
    public static final int BACKGROUND_ALPHA = 0x55;
    public static final int BACKGROUND_SELECTED = 0x5500ff00;
    public static final int BACKGROUND_SELECTED_NO_ALPHA = 0x00ff00;
    public static final int BACKGROUND_UNSELECTED = 0x55ffffff;
    public static final int BACKGROUND_UNSELECTED_NO_ALPHA = 0xffffff;
    private static final MethodHandle PROPERTY_TOSTRING;

    static {
        MethodType mt = MethodType.methodType(
            String.class,
            Comparable.class
        );
        try {
            PROPERTY_TOSTRING = MethodHandles.lookup()
                .findVirtual(
                    Property.class,
                    "getName",
                    mt
                );
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final Minecraft minecraft = Minecraft.getInstance();
    private final Logger logger = LogUtils.getLogger();
    private final BlockPos targetBlockPos;
    private final Property<?> property;
    private final List<BlockState> possibleStates;

    private BlockState currentBlockState;
    private final Map<Vector2f, SelectionItem> itemMap = new HashMap<>();
    private long displayTime = System.currentTimeMillis();
    private boolean animationStarted = false;

    public AnvilHammerScreen(BlockPos targetBlockPos, BlockState initialBlockState, Property<?> property, List<BlockState> possibleStates) {
        super(Component.translatable("screen.anvilcraft.anvil_hammer.title"));
        this.targetBlockPos = targetBlockPos;
        this.currentBlockState = initialBlockState.cycle(property);
        this.property = property;
        this.possibleStates = possibleStates;
    }

    @Override
    protected void init() {
        itemMap.clear();
        float centerX = this.width / 2f;
        float centerY = this.height / 2f;
        Vector2f vector2f = new Vector2f(0, 1);
        float degreeEachRotation = 360f / possibleStates.size();
        for (int i = 0; i < possibleStates.size(); i++) {
            BlockState state = possibleStates.get(i);
            Vector2f rotated = MathUtil.rotationDegrees(vector2f, -degreeEachRotation * i)
                .mul(RADIUS)
                .add(centerX, centerY);
            Rect2i detectionRect = new Rect2i(
                (int) (Math.floor(rotated.x) - 20),
                (int) (Math.floor(rotated.y) - 20),
                40,
                40
            );
            try {
                itemMap.put(rotated,
                    new SelectionItem(
                        rotated,
                        detectionRect,
                        state,
                        Component.literal(
                            "%s: %s".formatted(
                                property.getName(),
                                PROPERTY_TOSTRING.invokeWithArguments(
                                    property,
                                    state.getValue(property)
                                )
                            )
                        )
                    )
                );
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        itemMap.values().stream()
            .filter(it -> it.detectionRect.contains((int) Math.floor(mouseX), (int) Math.floor(mouseY)))
            .findFirst()
            .ifPresent(it -> currentBlockState = it.state);
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!shouldRender()) {
            return;
        }
        if (!animationStarted) {
            animationStarted = true;
            displayTime = System.currentTimeMillis();
        }
        float delta = displayTime + ANIMATION_T - System.currentTimeMillis();

        if (delta > 0) {
            float centerX = this.width / 2f;
            float centerY = this.height / 2f;
            float progress = 1 - (delta / ANIMATION_T);
            progress = (float) (-Math.pow(progress, 2) + 2 * progress);
            if (progress == 0) return;
            int transparency = ((int) (progress * BACKGROUND_ALPHA)) << 24;
            for (SelectionItem value : itemMap.values()) {
                Vector2f center = new Vector2f(
                    (value.center.x - centerX) / RADIUS,
                    (value.center.y - centerY) / RADIUS
                ).mul(RADIUS * progress)
                    .add(centerX, centerY);
                float x = center.x;
                float y = center.y;
                Rect2i detectionRect = value.detectionRect;
                boolean selected = value.state == currentBlockState;
                guiGraphics.fill(
                    (int) (x - 20),
                    (int) (y - 20),
                    (int) (x - 20) + detectionRect.getWidth(),
                    (int) (y - 20) + detectionRect.getHeight(),
                    selected
                        ? transparency | BACKGROUND_SELECTED_NO_ALPHA
                        : transparency | BACKGROUND_UNSELECTED_NO_ALPHA
                );
                RenderHelper.renderBlock(
                    guiGraphics,
                    value.state,
                    x,
                    y - 4f,
                    100,
                    selected ? ZOOM_SELECTED : ZOOM_UNSELECTED,
                    RenderHelper.SINGLE_BLOCK
                );
                int textAlpha = (int) (progress * 0xff) << 24;
                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();
                float offsetX = 0.1f * this.width;
                float offsetY = 0.1f * this.height;
                poseStack.translate(offsetX, offsetY, 0);
                poseStack.scale(0.8f, 0.8f, 0.8f);
                float adjustedX = (x - offsetX) / 0.8f;
                float adjustedY = (y - offsetY - 30) / 0.8f;
                guiGraphics.drawCenteredString(
                    minecraft.font,
                    value.description,
                    (int) adjustedX,
                    (int) adjustedY,
                    textAlpha | 0xfdfdfd
                );
                poseStack.popPose();
            }
            return;
        }
        for (SelectionItem value : itemMap.values()) {
            float x = value.center.x;
            float y = value.center.y;
            Rect2i detectionRect = value.detectionRect;
            boolean selected = value.state == currentBlockState;
            guiGraphics.fill(
                detectionRect.getX(),
                detectionRect.getY(),
                detectionRect.getX() + detectionRect.getWidth(),
                detectionRect.getY() + detectionRect.getHeight(),
                selected ? BACKGROUND_SELECTED : BACKGROUND_UNSELECTED
            );
            RenderHelper.renderBlock(
                guiGraphics,
                value.state,
                x,
                y - 4f,
                100,
                selected ? ZOOM_SELECTED : ZOOM_UNSELECTED,
                RenderHelper.SINGLE_BLOCK
            );
            PoseStack poseStack = guiGraphics.pose();
            poseStack.pushPose();
            float offsetX = 0.1f * this.width;
            float offsetY = 0.1f * this.height;
            poseStack.translate(offsetX, offsetY, 0);
            poseStack.scale(0.8f, 0.8f, 0.8f);
            float adjustedX = (x - offsetX) / 0.8f;
            float adjustedY = (y - offsetY - 30) / 0.8f;
            guiGraphics.drawCenteredString(
                minecraft.font,
                value.description,
                (int) adjustedX,
                (int) adjustedY,
                0xfffdfdfd
            );
            poseStack.popPose();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void removed() {
        Minecraft.getInstance().level.setBlock(
            targetBlockPos,
            currentBlockState,
            Block.UPDATE_CLIENTS,
            0
        );
        PacketDistributor.sendToServer(
            new HammerChangeBlockPacket(
                targetBlockPos,
                currentBlockState
            )
        );
        super.removed();
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        minecraft.setScreen(null);
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldRender() {
        if (animationStarted) return true;
        return (displayTime + DELAY) <= System.currentTimeMillis();
    }

    @Override
    public BlockPos renderingBlockPos() {
        return targetBlockPos;
    }

    @Override
    public BlockState renderingBlockState() {
        return currentBlockState;
    }

    @Override
    public RenderType renderType() {
        return ModRenderTypes.TRANSLUCENT_COLORED_OVERLAY;
    }

    private record SelectionItem(
        Vector2f center,
        Rect2i detectionRect,
        BlockState state,
        Component description
    ) {
    }
}
