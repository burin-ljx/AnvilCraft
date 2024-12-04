package dev.dubhe.anvilcraft.client.gui.screen;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.dubhe.anvilcraft.api.hammer.IHasHammerEffect;
import dev.dubhe.anvilcraft.api.input.IMouseHandlerExtension;
import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import dev.dubhe.anvilcraft.client.init.ModShaders;
import dev.dubhe.anvilcraft.network.HammerChangeBlockPacket;
import dev.dubhe.anvilcraft.util.MathUtil;
import dev.dubhe.anvilcraft.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class AnvilHammerScreen extends Screen implements IHasHammerEffect {
    public static final int RADIUS = 80;
    public static final int DELAY = 80;//ms
    public static final int ANIMATION_T = 300;//ms
    public static final int CLOSING_ANIMATION_T = 150;//ms
    public static final int SELECTION_TRANSFORMATION = 80;
    public static final float ZOOM = 13.5f;
    public static final int IGNORE_CURSOR_MOVE_LENGTH = 15;

    private static final MethodHandle PROPERTY_TOSTRING;

    private static final int RING_COLOR = 0x88000000;
    private static final int RING_INNER_DIAMETER = 55;
    private static final int RING_OUTER_DIAMETER = 105;

    private static final int SELECTION_EFFECT_ALPHA = 0xdd;
    private static final int SELECTION_EFFECT_COLOR = 0xffFFFF00;
    private static final int SELECTION_EFFECT_RADIUS = 20;

    private static final float TEXT_SCALE = 1f;
    private static final int TEXT_COLOR = 0xfdfdfd;

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
    private final BlockPos targetBlockPos;
    private final Property<?> property;
    private final List<BlockState> possibleStates;

    private BlockState currentBlockState;
    private SelectionItem currentSelection;
    private final List<SelectionItem> items = new ArrayList<>();
    private long displayTime = System.currentTimeMillis();
    private boolean animationStarted = false;
    private boolean closingAnimationStarted = false;

    public AnvilHammerScreen(BlockPos targetBlockPos, BlockState initialBlockState, Property<?> property, List<BlockState> possibleStates) {
        super(Component.translatable("screen.anvilcraft.anvil_hammer.title"));
        this.targetBlockPos = targetBlockPos;
        this.currentBlockState = initialBlockState.cycle(property);
        this.property = property;
        this.possibleStates = possibleStates;
    }

    @Override
    protected void init() {
        items.clear();
        float centerX = this.width / 2f;
        float centerY = this.height / 2f;
        Vector2f vector2f = new Vector2f(0, 1);
        float degreeEachRotation = 360f / possibleStates.size();
        for (int i = 0; i < possibleStates.size(); i++) {
            BlockState state = possibleStates.get(i);
            float rotation = degreeEachRotation * i;
            Vector2f rotated = MathUtil.rotationDegrees(vector2f, rotation)
                .mul(-1, 1)
                .mul(RADIUS)
                .add(centerX, centerY);
            try {
                float detectionStart = ((rotation - degreeEachRotation / 2f) * MathUtil.DEGREE_CONVERT + (float) Math.PI);
                float detectionEnd = ((rotation + degreeEachRotation / 2f) * MathUtil.DEGREE_CONVERT + (float) Math.PI);
                detectionStart = detectionStart % (float) (Math.PI * 2);
                detectionEnd = detectionEnd % (float) (Math.PI * 2);
                items.add(
                    new SelectionItem(
                        rotated,
                        detectionStart,
                        detectionEnd,
                        state,
                        Component.literal(
                            "%s".formatted(
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
        this.currentSelection = items.stream()
            .filter(it -> it.state == currentBlockState)
            .findFirst()
            .orElseThrow();
        this.currentSelection.selected = true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (closingAnimationStarted) return true;
        float screenCenterX = width / 2f;
        float screenCenterY = height / 2f;
        Vector2f cursorVec2 = new Vector2f(
            (float) mouseX - screenCenterX,
            (float) mouseY - screenCenterY
        );
        if (cursorVec2.length() < IGNORE_CURSOR_MOVE_LENGTH) {
            return true;
        }
        Vector2f rotationStart = new Vector2f(0, 1);
        cursorVec2.normalize();
        double rot = Math.acos(rotationStart.dot(cursorVec2) / (rotationStart.length() * cursorVec2.length()));
        double rotation = cursorVec2.x < 0 ? Math.PI - rot : Math.PI + rot;
        items.stream()
            .filter(it -> {
                if (it.detectionAngleStart > it.detectionAngleEnd) {
                    return rotation >= it.detectionAngleStart;
                }
                return rotation >= it.detectionAngleStart && rotation <= it.detectionAngleEnd;
            })
            .findFirst()
            .ifPresent(it -> {
                SelectionItem oldSelection = currentSelection;
                currentSelection = it;
                if (!currentSelection.equals(oldSelection)) {
                    if (oldSelection != null) {
                        oldSelection.transformation = TransformationType.EXTINGUISH;
                        oldSelection.transformationStartTime = System.currentTimeMillis();
                    }
                    currentSelection.transformation = TransformationType.LIT;
                    currentSelection.transformationStartTime = System.currentTimeMillis();
                }
                if (oldSelection != null) {
                    oldSelection.selected = false;
                }
                currentSelection.selected = true;
                currentBlockState = it.state;
            });
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    public void renderClosingAnimation(GuiGraphics guiGraphics, int mouseX, int mouseY, float particalTick) {
        if (!closingAnimationStarted) return;
        float delta = displayTime + CLOSING_ANIMATION_T - System.currentTimeMillis();
        float centerX = this.width / 2f;
        float centerY = this.height / 2f;
        float progress = delta / CLOSING_ANIMATION_T;
        if (progress >= 1 || progress <= 0) {
            minecraft.setScreen(null);
        }
        renderProgressAnimation(guiGraphics, progress, centerX, centerY);
    }

    private void renderProgressAnimation(GuiGraphics guiGraphics, float progress, float centerX, float centerY) {
        progress = (float) (-Math.pow(progress, 2) + 2 * progress);
        if (progress == 0) return;
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        renderRing(
            guiGraphics,
            this.width / 2f,
            this.height / 2f,
            RING_COLOR,
            RING_INNER_DIAMETER * progress,
            RING_OUTER_DIAMETER * progress
        );
        poseStack.popPose();
        for (SelectionItem value : items) {
            Vector2f center = new Vector2f(
                (value.center.x - centerX) / RADIUS,
                (value.center.y - centerY) / RADIUS
            ).mul(RADIUS * progress)
                .add(centerX, centerY);
            float x = center.x;
            float y = center.y;
            if (value.shouldRenderTransformation()) {
                float selectionTrProgress = switch (value.transformation) {
                    case LIT -> value.getTransformationProgress();
                    case EXTINGUISH -> 1 - value.getTransformationProgress();
                };
                if (selectionTrProgress != 0) {
                    int alpha = ((int) (0xff * selectionTrProgress)) << 24;
                    int color = alpha | 0xFFFF00;
                    renderSelectionEffect(
                        guiGraphics,
                        x,
                        y,
                        color,
                        SELECTION_EFFECT_RADIUS
                    );
                }
            } else {
                if (value.selected) {
                    renderSelectionEffect(
                        guiGraphics,
                        x,
                        y,
                        SELECTION_EFFECT_COLOR,
                        SELECTION_EFFECT_RADIUS
                    );
                }
            }
        }
        for (SelectionItem value : items) {
            Vector2f center = new Vector2f(
                (value.center.x - centerX) / RADIUS,
                (value.center.y - centerY) / RADIUS
            ).mul(RADIUS * progress)
                .add(centerX, centerY);
            float x = center.x;
            float y = center.y;
            RenderHelper.renderBlock(
                guiGraphics,
                value.state,
                x,
                y - 4f,
                100,
                ZOOM,
                RenderHelper.SINGLE_BLOCK
            );
            int textAlpha = (int) (progress * 0xff) << 24;
            poseStack.pushPose();
            float coordinateScale = 0.7f;
            float offsetX = 0.1f * this.width;
            float offsetY = 0.1f * this.height;
            float adjustedX = (x - offsetX) / coordinateScale;
            float adjustedY = (y - offsetY - 20) / coordinateScale;
            poseStack.translate(offsetX, offsetY, 0);
            poseStack.scale(coordinateScale, coordinateScale, coordinateScale);
            poseStack.translate(adjustedX, adjustedY, 0);
            poseStack.scale(TEXT_SCALE / coordinateScale, TEXT_SCALE / coordinateScale, TEXT_SCALE / coordinateScale);
            guiGraphics.drawCenteredString(
                minecraft.font,
                value.description,
                0,
                0,
                textAlpha | 0xfdfdfd
            );
            poseStack.popPose();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        float centerX = this.width / 2f;
        float centerY = this.height / 2f;
        renderClosingAnimation(guiGraphics, mouseX, mouseY, partialTick);
        if (!shouldRender()) {
            return;
        }
        if (closingAnimationStarted) return;
        if (!animationStarted) {
            animationStarted = true;
            displayTime = System.currentTimeMillis();
        }
        PoseStack poseStack = guiGraphics.pose();
        float delta = displayTime + ANIMATION_T - System.currentTimeMillis();
        if (delta > 0) {
            float progress = 1 - (delta / ANIMATION_T);
            progress = (float) (-Math.pow(progress, 2) + 2 * progress);
            if (progress == 0) return;
            renderProgressAnimation(guiGraphics, progress, centerX, centerY);
            return;
        }
        renderRing(
            guiGraphics,
            this.width / 2f,
            this.height / 2f,
            RING_COLOR,
            RING_INNER_DIAMETER,
            RING_OUTER_DIAMETER
        );
        //renderSelection(guiGraphics);
        for (SelectionItem value : items) {
            float x = value.center.x;
            float y = value.center.y;
            if (value.shouldRenderTransformation()) {
                float selectionTrProgress = switch (value.transformation) {
                    case LIT -> value.getTransformationProgress();
                    case EXTINGUISH -> 1 - value.getTransformationProgress();
                };
                if (selectionTrProgress != 0) {
                    int alpha = ((int) (0xff * selectionTrProgress)) << 24;
                    int color = alpha | 0xFFFF00;
                    renderSelectionEffect(
                        guiGraphics,
                        x,
                        y,
                        color,
                        SELECTION_EFFECT_RADIUS
                    );
                }
            } else {
                if (value.selected) {
                    renderSelectionEffect(
                        guiGraphics,
                        x,
                        y,
                        SELECTION_EFFECT_COLOR,
                        SELECTION_EFFECT_RADIUS
                    );
                }
            }
        }
        for (SelectionItem value : items) {
            float x = value.center.x;
            float y = value.center.y;
            RenderHelper.renderBlock(
                guiGraphics,
                value.state,
                x,
                y - 4f,
                -100,
                ZOOM,
                RenderHelper.SINGLE_BLOCK
            );
            poseStack.pushPose();
            float coordinateScale = 0.7f;
            float offsetX = 0.1f * this.width;
            float offsetY = 0.1f * this.height;
            float adjustedX = (x - offsetX) / coordinateScale;
            float adjustedY = (y - offsetY - 20) / coordinateScale;

            poseStack.translate(offsetX, offsetY, 0);
            poseStack.scale(coordinateScale, coordinateScale, coordinateScale);
            poseStack.translate(adjustedX, adjustedY, 0);
            poseStack.scale(TEXT_SCALE / coordinateScale, TEXT_SCALE / coordinateScale, TEXT_SCALE / coordinateScale);
            guiGraphics.drawCenteredString(
                minecraft.font,
                value.description,
                0,
                0,
                (0xff << 24) | TEXT_COLOR
            );
            poseStack.popPose();
        }
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
    }

    private static void renderSelectionEffect(
        GuiGraphics guiGraphics,
        float centerX,
        float centerY,
        int color,
        float radius
    ) {
        PoseStack poseStack = guiGraphics.pose();
        Matrix4f matrix4f = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(
            VertexFormat.Mode.QUADS,
            DefaultVertexFormat.POSITION_COLOR
        );
        float x1 = centerX - radius - 5;
        float y1 = centerY - radius - 5;
        float x2 = centerX + radius + 5;
        float y2 = centerY + radius + 5;
        bufferBuilder.addVertex(matrix4f, x1, y1, -200).setColor(color);
        bufferBuilder.addVertex(matrix4f, x1, y2, -200).setColor(color);
        bufferBuilder.addVertex(matrix4f, x2, y2, -200).setColor(color);
        bufferBuilder.addVertex(matrix4f, x2, y1, -200).setColor(color);

        Window window = Minecraft.getInstance().getWindow();
        float guiScale = (float) window.getGuiScale();
        RenderSystem.setShader(ModShaders::getSelectionShader);

        ModShaders.getSelectionShader()
            .safeGetUniform("Center")
            .set(centerX * guiScale, centerY * guiScale);
        ModShaders.getSelectionShader()
            .safeGetUniform("FramebufferSize")
            .set((float) window.getWidth(), (float) window.getHeight());
        ModShaders.getSelectionShader()
            .safeGetUniform("Radius")
            .set(radius * guiScale);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        BufferUploader.drawWithShader(bufferBuilder.build());
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
    public void tick() {
        if (closingAnimationStarted) {
            minecraft.handleKeybinds();
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (shouldRender() && !closingAnimationStarted) {
            IMouseHandlerExtension.of(minecraft.mouseHandler).anvilCraft$grabMouseWithScreen();
            displayTime = System.currentTimeMillis();
            closingAnimationStarted = true;
        } else {
            minecraft.setScreen(null);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldRender() {
        if (animationStarted) return true;
        return (displayTime + DELAY) <= System.currentTimeMillis();
    }

    private static void renderRing(
        GuiGraphics guiGraphics,
        float centerX,
        float centerY,
        int color,
        float innerDiameter,
        float outerDiameter
    ) {
        PoseStack poseStack = guiGraphics.pose();
        Matrix4f matrix4f = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.begin(
            VertexFormat.Mode.QUADS,
            DefaultVertexFormat.POSITION_COLOR
        );
        float x1 = centerX - outerDiameter - 5;
        float y1 = centerY - outerDiameter - 5;
        float x2 = centerX + outerDiameter + 5;
        float y2 = centerY + outerDiameter + 5;
        bufferBuilder.addVertex(matrix4f, x1, y1, -300).setColor(color);
        bufferBuilder.addVertex(matrix4f, x1, y2, -300).setColor(color);
        bufferBuilder.addVertex(matrix4f, x2, y2, -300).setColor(color);
        bufferBuilder.addVertex(matrix4f, x2, y1, -300).setColor(color);

        Window window = Minecraft.getInstance().getWindow();
        float guiScale = (float) window.getGuiScale();
        RenderSystem.setShader(ModShaders::getRingShader);

        ModShaders.getRingShader()
            .safeGetUniform("Center")
            .set(centerX * guiScale, centerY * guiScale);
        ModShaders.getRingShader()
            .safeGetUniform("InnerDiameter")
            .set(innerDiameter * guiScale);
        ModShaders.getRingShader()
            .safeGetUniform("OuterDiameter")
            .set(outerDiameter * guiScale);

        RenderSystem.setShaderColor(1, 1, 1, 1);
        BufferUploader.drawWithShader(bufferBuilder.build());
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

    private static final class SelectionItem {
        private final Vector2f center;
        private final float detectionAngleStart;
        private final float detectionAngleEnd;
        private final BlockState state;
        private final Component description;
        private TransformationType transformation = TransformationType.LIT;
        private long transformationStartTime = 0;
        private boolean selected = false;

        private SelectionItem(
            Vector2f center,
            float detectionAngleStart,
            float detectionAngleEnd,
            BlockState state,
            Component description
        ) {
            this.center = center;
            this.detectionAngleStart = detectionAngleStart;
            this.detectionAngleEnd = detectionAngleEnd;
            this.state = state;
            this.description = description;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (SelectionItem) obj;
            return Objects.equals(this.center, that.center) &&
                Float.floatToIntBits(this.detectionAngleStart) == Float.floatToIntBits(that.detectionAngleStart) &&
                Float.floatToIntBits(this.detectionAngleEnd) == Float.floatToIntBits(that.detectionAngleEnd) &&
                Objects.equals(this.state, that.state) &&
                Objects.equals(this.description, that.description);
        }

        @Override
        public int hashCode() {
            return Objects.hash(center, detectionAngleStart, detectionAngleEnd, state, description);
        }

        public boolean shouldRenderTransformation() {
            if (transformationStartTime == 0) return false;
            return System.currentTimeMillis() - transformationStartTime <= SELECTION_TRANSFORMATION;
        }

        public float getTransformationProgress() {
            if (!shouldRenderTransformation()) return 1;
            return Math.clamp(
                (System.currentTimeMillis() - transformationStartTime) / (float) SELECTION_TRANSFORMATION,
                0f,
                1f
            );
        }
    }

    enum TransformationType {
        LIT, EXTINGUISH
    }
}
