package dev.dubhe.anvilcraft.client.renderer.laser;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import dev.dubhe.anvilcraft.api.LaserStateAccess;
import dev.dubhe.anvilcraft.client.init.ModRenderTargets;
import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import dev.dubhe.anvilcraft.client.renderer.RenderState;
import dev.dubhe.anvilcraft.util.ThreadFactoryImpl;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LaserRenderer {
    public static final RenderType[] SUPPORTED_RENDERTYPES = new RenderType[]{
        ModRenderTypes.LASER,
        RenderType.solid(),
    };
    private final Queue<Runnable> pendingUploads = new ConcurrentLinkedDeque<>();
    private final Queue<Runnable> compileQueue = new ConcurrentLinkedDeque<>();
    @Getter
    private static LaserRenderer instance;
    private final Map<RenderType, ByteBufferBuilder> byteBuffers = Arrays.stream(SUPPORTED_RENDERTYPES)
        .collect(Collectors.toMap(
            Function.identity(),
            it -> new ByteBufferBuilder(786432)
        ));
    private final Map<RenderType, VertexBuffer> buffers = Arrays.stream(SUPPORTED_RENDERTYPES)
        .collect(Collectors.toMap(
            Function.identity(),
            it -> new VertexBuffer(VertexBuffer.Usage.STATIC)
        ));
    private final Map<RenderType, MeshData.SortState> sortStates = new HashMap<>();
    private final ClientLevel level;
    private Set<LaserStateAccess> laserBlockEntities = new HashSet<>();
    private final Minecraft minecraft = Minecraft.getInstance();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor(new ThreadFactoryImpl());
    private RebuildTask lastRebuildTask = null;
    private boolean isEmpty = true;
    private boolean valid = true;

    public LaserRenderer(ClientLevel level) {
        this.level = level;
    }

    public static void updateLevel(ClientLevel level) {
        if (instance != null) {
            instance.releaseBuffers();
        }
        instance = new LaserRenderer(level);
    }

    public void uploadBuffers() {
        while (!compileQueue.isEmpty()) {
            compileQueue.poll().run();
        }
        while (!pendingUploads.isEmpty()) {
            pendingUploads.poll().run();
        }
    }

    public void releaseBuffers() {
        byteBuffers.values().forEach(ByteBufferBuilder::close);
        buffers.values().forEach(VertexBuffer::close);
        valid = false;
    }

    public void requireRecompile(LaserStateAccess baseLaserBlockEntity) {
        if (lastRebuildTask != null) {
            lastRebuildTask.cancel();
        }
        if (baseLaserBlockEntity.removed()) {
            laserBlockEntities.remove(baseLaserBlockEntity);
            return;
        }
        laserBlockEntities.add(baseLaserBlockEntity);
        laserBlockEntities.removeIf(LaserStateAccess::removed);
        compileQueue.add(new RebuildTask());
    }

    public void render(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
        RenderSystem.enableBlend();
        if (ModRenderTargets.getBloomTarget() != null && RenderState.isBloomEffectEnabled()) {
            ModRenderTargets.getBloomTarget().setClearColor(0, 0, 0, 0);
            ModRenderTargets.getBloomTarget().clear(Minecraft.ON_OSX);
            ModRenderTargets.getBloomTarget().copyDepthFrom(this.minecraft.getMainRenderTarget());
        }
        if (isEmpty) return;
        System.out.println("RENDER!!!");
        Window window = Minecraft.getInstance().getWindow();
        Vec3 cameraPosition = minecraft.gameRenderer.getMainCamera().getPosition();
        for (Map.Entry<RenderType, VertexBuffer> entry : buffers.entrySet()) {
            MeshData.SortState sortState = sortStates.get(entry.getKey());
            if (sortState != null) {
                ByteBufferBuilder.Result result = sortState.buildSortedIndexBuffer(byteBuffers.get(entry.getKey()), createVertexSorting());
                if (result != null) {
                    VertexBuffer vb = entry.getValue();
                    vb.bind();
                    vb.uploadIndexBuffer(result);
                    VertexBuffer.unbind();
                }
            }
            if (entry.getKey() == ModRenderTypes.LASER) {
                RenderState.levelStage();
                renderLayer(entry.getKey(), entry.getValue(), frustumMatrix, projectionMatrix, cameraPosition, window);
                RenderState.bloomStage();
                renderLayer(entry.getKey(), entry.getValue(), frustumMatrix, projectionMatrix, cameraPosition, window);
                continue;
            }
            renderLayer(entry.getKey(), entry.getValue(), frustumMatrix, projectionMatrix, cameraPosition, window);
        }
    }

    private void renderLayer(
        RenderType renderType,
        VertexBuffer vertexBuffer,
        Matrix4f frustumMatrix,
        Matrix4f projectionMatrix,
        Vec3 cameraPosition,
        Window window
    ) {
        if (vertexBuffer.getFormat() == null) {
            System.out.println("WTF");
            return;
        }
        renderType.setupRenderState();
        ShaderInstance shader = RenderSystem.getShader();
        shader.setDefaultUniforms(VertexFormat.Mode.QUADS, frustumMatrix, projectionMatrix, window);
        shader.apply();
        Uniform uniform = shader.CHUNK_OFFSET;
        if (uniform != null) {
            uniform.set(
                (float) -cameraPosition.x,
                (float) -cameraPosition.y,
                (float) -cameraPosition.z
            );
            uniform.upload();
        }
        vertexBuffer.bind();
        vertexBuffer.draw();
        VertexBuffer.unbind();
        if (uniform != null) {
            uniform.set(0.0F, 0.0F, 0.0F);
        }
        renderType.clearRenderState();
    }

    private VertexSorting createVertexSorting() {
        Vec3 cameraPos = minecraft.gameRenderer.getMainCamera().getPosition();
        return VertexSorting.byDistance(cameraPos.toVector3f());
    }

    public void clear() {
        byteBuffers.values().forEach(ByteBufferBuilder::clear);
    }

    private class RebuildTask implements Runnable {
        private boolean cancelled = false;

        @Override
        public void run() {
            lastRebuildTask = this;
            Map<RenderType, BufferBuilder> bufferBuilderMap = new HashMap<>();
            for (RenderType supportedRendertype : SUPPORTED_RENDERTYPES) {
                bufferBuilderMap.put(supportedRendertype, new BufferBuilder(
                    byteBuffers.get(supportedRendertype),
                    VertexFormat.Mode.QUADS,
                    DefaultVertexFormat.BLOCK)
                );
            }
            PoseStack poseStack = new PoseStack();
            LaserRenderer.this.isEmpty = true;
            for (LaserStateAccess laserBlockEntity : new ArrayList<>(laserBlockEntities)) {
                if (cancelled) return;
                poseStack.pushPose();
                BlockPos pos = laserBlockEntity.getBlockPos();
                poseStack.translate(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ()
                );
                LaserState laserState = LaserState.create(laserBlockEntity, poseStack);
                if (laserState == null) continue;
                LaserCompiler.compile(
                    laserState,
                    it -> {
                        if (bufferBuilderMap.containsKey(it)) {
                            return bufferBuilderMap.get(it);
                        }
                        throw new IllegalArgumentException("Unknown RenderType: " + it);
                    }
                );
            }
            bufferBuilderMap.forEach((renderType, bufferBuilder) -> {
                if (bufferBuilder != null) {
                    MeshData meshData = bufferBuilder.build();
                    if (meshData != null) {
                        LaserRenderer.this.isEmpty = false;
                        VertexBuffer vb = buffers.get(renderType);
                        sortStates.put(
                            renderType,
                            meshData.sortQuads(
                                byteBuffers.get(renderType),
                                createVertexSorting()
                            )
                        );

                        if (vb.isInvalid()) {
                            meshData.close();
                        } else {
                            pendingUploads.add(() -> {
                                if (LaserRenderer.this.valid) {
                                    if (vb.isInvalid()) return;
                                    vb.bind();
                                    vb.upload(meshData);
                                    VertexBuffer.unbind();
                                }
                            });
                        }
                    }
                }
            });

            lastRebuildTask = null;
        }

        void cancel() {
            cancelled = true;
        }
    }
}
