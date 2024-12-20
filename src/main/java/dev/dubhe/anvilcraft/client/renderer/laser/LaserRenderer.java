package dev.dubhe.anvilcraft.client.renderer.laser;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.dubhe.anvilcraft.api.LaserStateAccess;
import dev.dubhe.anvilcraft.client.init.ModRenderTypes;
import dev.dubhe.anvilcraft.client.renderer.RenderState;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LaserRenderer {
    public static final RenderType[] SUPPORTED_RENDERTYPES = new RenderType[]{
        RenderType.solid(),
        ModRenderTypes.LASER
    };

    public static final RenderType[] BLOOM_RENDERTYPES = new RenderType[]{
        ModRenderTypes.LASER
    };
    private static final MemoryUtil.MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);

    private final Queue<Runnable> pendingUploads = new ConcurrentLinkedDeque<>();
    private final Queue<Runnable> compileQueue = new ConcurrentLinkedDeque<>();
    @Getter
    private static LaserRenderer instance;

    private final Map<ChunkPos, Map<RenderType, VertexBuffer>> buffers = new HashMap<>();
    private Map<ChunkPos, Map<RenderType, CompileResult>> compileResultMap = new HashMap<>();

    @SuppressWarnings("unused")
    private final ClientLevel level;
    private final Set<LaserStateAccess> laserBlockEntities = new HashSet<>();
    private final Minecraft minecraft = Minecraft.getInstance();
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

    public void runTasks() {
        while (!compileQueue.isEmpty() && valid) {
            compileQueue.poll().run();
        }
        while (!pendingUploads.isEmpty() && valid) {
            pendingUploads.poll().run();
        }
    }

    private Map<RenderType, VertexBuffer> getBufferForChunk(ChunkPos chunkPos) {
        if (buffers.containsKey(chunkPos)) {
            return buffers.get(chunkPos);
        }
        Map<RenderType, VertexBuffer> vb = Arrays.stream(SUPPORTED_RENDERTYPES)
            .collect(Collectors.toMap(
                Function.identity(),
                it -> new VertexBuffer(VertexBuffer.Usage.STATIC)
            ));
        buffers.put(chunkPos, vb);
        return vb;
    }

    public void releaseBuffers() {
        buffers.values().forEach(it -> it.values().forEach(VertexBuffer::close));
        compileResultMap.values().forEach(it -> it.values().forEach(CompileResult::free));
        valid = false;
    }

    public void blockRemoved(LaserStateAccess laserStateAccess) {
        if (lastRebuildTask != null) {
            lastRebuildTask.cancel();
        }
        laserBlockEntities.remove(laserStateAccess);
        compileQueue.add(new RebuildTask());
    }

    public void requireRecompile(LaserStateAccess baseLaserBlockEntity) {
        if (lastRebuildTask != null) {
            lastRebuildTask.cancel();
        }
        if (baseLaserBlockEntity.removed()) {
            laserBlockEntities.remove(baseLaserBlockEntity);
            laserBlockEntities.removeIf(LaserStateAccess::removed);
            compileQueue.add(new RebuildTask());
            return;
        }
        laserBlockEntities.add(baseLaserBlockEntity);
        laserBlockEntities.removeIf(LaserStateAccess::removed);
        compileQueue.add(new RebuildTask());
    }

    public void renderBloomed(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
        renderInternal(frustumMatrix, projectionMatrix, BLOOM_RENDERTYPES, RenderState::bloomStage);
    }

    public void render(Matrix4f frustumMatrix, Matrix4f projectionMatrix) {
        renderInternal(frustumMatrix, projectionMatrix, SUPPORTED_RENDERTYPES, RenderState::levelStage);
    }

    private void renderInternal(
        Matrix4f frustumMatrix,
        Matrix4f projectionMatrix,
        RenderType[] renderTypes,
        Runnable stateSwitcher
    ){
        RenderSystem.enableBlend();
        if (isEmpty) return;
        Window window = Minecraft.getInstance().getWindow();
        Vec3 cameraPosition = minecraft.gameRenderer.getMainCamera().getPosition();
        for (Map.Entry<ChunkPos, Map<RenderType, VertexBuffer>> chunkPosMapEntry : buffers.entrySet()) {
            ChunkPos chunkPos = chunkPosMapEntry.getKey();
            int renderDistance = Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
            if (cameraPosition.distanceTo(new Vec3(chunkPos.x * 16, cameraPosition.y, chunkPos.z * 16)) > renderDistance) {
                continue;
            }
            Map<RenderType, VertexBuffer> bufferMap = chunkPosMapEntry.getValue();
            Map<RenderType, CompileResult> compileResultMap = this.compileResultMap.get(chunkPos);
            if (compileResultMap == null) continue;
            for (RenderType renderType : renderTypes) {
                VertexBuffer vb = bufferMap.get(renderType);
                stateSwitcher.run();
                renderLayer(renderType, vb, frustumMatrix, projectionMatrix, cameraPosition, window, compileResultMap);
            }
        }
    }

    private void renderLayer(
        RenderType renderType,
        VertexBuffer vertexBuffer,
        Matrix4f frustumMatrix,
        Matrix4f projectionMatrix,
        Vec3 cameraPosition,
        Window window,
        Map<RenderType, CompileResult> compileResultMap
    ) {
        CompileResult compileResult = compileResultMap.get(renderType);
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
        RenderSystem.drawElements(GL15.GL_TRIANGLES, compileResult.indexCount, vertexBuffer.sequentialIndices.type().asGLType);
        VertexBuffer.unbind();
        if (uniform != null) {
            uniform.set(0.0F, 0.0F, 0.0F);
        }
        renderType.clearRenderState();
    }

    public void clear() {
    }

    private class RebuildTask implements Runnable {
        private boolean cancelled = false;

        @Override
        public void run() {
            lastRebuildTask = this;
            Map<ChunkPos, Map<RenderType, CompileResult>> compileResultMap = new HashMap<>();
            PoseStack poseStack = new PoseStack();
            LaserRenderer.this.isEmpty = true;
            Map<ChunkPos, List<LaserStateAccess>> groupedLaserStates = new HashMap<>();
            for (LaserStateAccess laserBlockEntity : laserBlockEntities) {
                groupedLaserStates.computeIfAbsent(new ChunkPos(laserBlockEntity.getBlockPos()), it -> Lists.newArrayList())
                    .add(laserBlockEntity);
            }
            for (Map.Entry<ChunkPos, List<LaserStateAccess>> chunkPosListEntry : groupedLaserStates.entrySet()) {
                ChunkPos chunkPos = chunkPosListEntry.getKey();
                List<LaserStateAccess> group = chunkPosListEntry.getValue();
                for (RenderType renderType : SUPPORTED_RENDERTYPES) {
                    if (cancelled) return;
                    Tesselator tesselator = Tesselator.getInstance();
                    BufferBuilder bufferBuilder = tesselator.begin(renderType.mode, renderType.format);
                    long ptr = tesselator.buffer.pointer;
                    int offsetBeforeCompile = tesselator.buffer.writeOffset;
                    for (LaserStateAccess laserBlockEntity : new ArrayList<>(group)) {
                        if (cancelled) return;
                        poseStack.pushPose();
                        BlockPos pos = laserBlockEntity.getBlockPos();
                        poseStack.translate(
                            pos.getX(),
                            pos.getY(),
                            pos.getZ()
                        );
                        LaserState laserState = LaserState.create(laserBlockEntity, poseStack);
                        if (laserState != null && laserState.laserLevel() > 0) {
                            float width = LaserCompiler.laserWidth(laserState);
                            LaserCompiler.compileStage(
                                laserState,
                                bufferBuilder,
                                renderType,
                                width
                            );
                        }
                        poseStack.popPose();
                    }
                    if (bufferBuilder.vertices > 0) {
                        LaserRenderer.this.isEmpty = false;
                    }
                    int compiledVertices = bufferBuilder.vertices * bufferBuilder.format.getVertexSize();
                    long allocated = ALLOCATOR.malloc(compiledVertices);
                    MemoryUtil.memCopy(ptr + offsetBeforeCompile, allocated, compiledVertices);
                    MeshData mesh = bufferBuilder.build();
                    if (mesh != null) {
                        mesh.close();
                    }
                    CompileResult compileResult = new CompileResult(
                        renderType,
                        bufferBuilder.vertices,
                        bufferBuilder.format.getVertexSize(),
                        allocated,
                        renderType.mode.indexCount(bufferBuilder.vertices)
                    );
                    compileResultMap.computeIfAbsent(chunkPos, it -> new HashMap<>())
                        .put(renderType, compileResult);
                }
            }

            LaserRenderer.this.compileResultMap
                .values()
                .forEach(it -> it.values().forEach(CompileResult::free));

            LaserRenderer.this.compileResultMap = compileResultMap;
            compileResultMap.forEach((chunkPos, map) ->
                map.forEach((renderType, compileResult) ->
                    pendingUploads.add(() -> {
                        VertexBuffer vb = getBufferForChunk(chunkPos).get(renderType);
                        compileResult.upload(vb);
                    })
                )
            );
            lastRebuildTask = null;
        }

        void cancel() {
            cancelled = true;
        }
    }

    @EqualsAndHashCode
    static final class CompileResult {
        final RenderType renderType;
        final int vertexCount;
        final int vertexSize;
        final long vertexBufferPtr;
        final int indexCount;
        boolean freed = false;

        CompileResult(
            RenderType renderType,
            int vertexCount,
            int vertexSize,
            long vertexBufferPtr,
            int indexCount
        ) {
            this.renderType = renderType;
            this.vertexCount = vertexCount;
            this.vertexSize = vertexSize;
            this.vertexBufferPtr = vertexBufferPtr;
            this.indexCount = indexCount;
        }

        void upload(VertexBuffer vertexBuffer) {
            if (freed) return;
            VertexFormat.Mode mode = renderType.mode;
            vertexBuffer.bind();
            if (vertexBuffer.format != null) {
                vertexBuffer.format.clearBufferState();
            }
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer.vertexBufferId);
            renderType.format.setupBufferState();
            vertexBuffer.format = renderType.format;
            GL15C.nglBufferData(GL15.GL_ARRAY_BUFFER, (long) vertexCount * vertexSize, vertexBufferPtr, GL15.GL_STATIC_DRAW);
            RenderSystem.AutoStorageIndexBuffer indexBuffer = RenderSystem.getSequentialBuffer(mode);
            if (indexBuffer != vertexBuffer.sequentialIndices || !indexBuffer.hasStorage(indexCount)) {
                indexBuffer.bind(indexCount);
            }
            vertexBuffer.sequentialIndices = indexBuffer;
            VertexBuffer.unbind();
        }

        void free() {
            if (freed) return;
            ALLOCATOR.free(vertexBufferPtr);
            freed = true;
        }
    }
}
