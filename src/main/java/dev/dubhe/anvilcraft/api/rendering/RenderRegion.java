package dev.dubhe.anvilcraft.api.rendering;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.dubhe.anvilcraft.client.renderer.RenderState;
import lombok.EqualsAndHashCode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.system.MemoryUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.dubhe.anvilcraft.api.rendering.CacheableBERenderingPipeline.BLOOM_RENDERTYPES;
import static dev.dubhe.anvilcraft.api.rendering.CacheableBERenderingPipeline.SUPPORTED_RENDERTYPES;

public class RenderRegion {
    private static final MemoryUtil.MemoryAllocator ALLOCATOR = MemoryUtil.getAllocator(false);
    private final ChunkPos chunkPos;
    private final Map<RenderType, VertexBuffer> buffers = Arrays.stream(SUPPORTED_RENDERTYPES)
        .collect(Collectors.toMap(
            Function.identity(),
            it -> new VertexBuffer(VertexBuffer.Usage.STATIC)
        ));
    private Map<RenderType, CompileResult> compileResultMap = new HashMap<>();
    private final List<CacheableBlockEntity> blockEntityList = new ArrayList<>();
    private final CacheableBERenderingPipeline pipeline;
    private final Minecraft minecraft = Minecraft.getInstance();
    private RebuildTask lastRebuildTask;

    private boolean isEmpty = true;

    public RenderRegion(ChunkPos chunkPos, CacheableBERenderingPipeline pipeline) {
        this.chunkPos = chunkPos;
        this.pipeline = pipeline;
    }

    public void update(CacheableBlockEntity be) {
        if (lastRebuildTask != null) {
            lastRebuildTask.cancel();
        }
        blockEntityList.removeIf(CacheableBlockEntity::isRemoved);
        if (be.isRemoved()) {
            blockEntityList.remove(be);
            pipeline.submitCompileTask(new RebuildTask());
            return;
        }
        blockEntityList.add(be);
        pipeline.submitCompileTask(new RebuildTask());
    }

    public void blockRemoved(CacheableBlockEntity be) {
        if (lastRebuildTask != null) {
            lastRebuildTask.cancel();
        }
        blockEntityList.remove(be);
        blockEntityList.removeIf(CacheableBlockEntity::isRemoved);
        pipeline.submitCompileTask(new RebuildTask());
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
    ) {
        if (isEmpty) return;
        RenderSystem.enableBlend();
        Window window = Minecraft.getInstance().getWindow();
        Vec3 cameraPosition = minecraft.gameRenderer.getMainCamera().getPosition();
        int renderDistance = Minecraft.getInstance().options.getEffectiveRenderDistance() * 16;
        if (cameraPosition.distanceTo(new Vec3(chunkPos.x * 16, cameraPosition.y, chunkPos.z * 16)) > renderDistance) {
            return;
        }
        for (RenderType renderType : renderTypes) {
            VertexBuffer vb = buffers.get(renderType);
            stateSwitcher.run();
            renderLayer(renderType, vb, frustumMatrix, projectionMatrix, cameraPosition, window, compileResultMap);
        }
    }

    public void releaseBuffers() {
        buffers.values().forEach(VertexBuffer::close);
        compileResultMap.values().forEach(CompileResult::free);
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

    private class RebuildTask implements Runnable {
        private boolean cancelled = false;

        @Override
        public void run() {
            lastRebuildTask = this;
            Map<RenderType, CompileResult> compileResultMap = new HashMap<>();
            PoseStack poseStack = new PoseStack();
            RenderRegion.this.isEmpty = true;

            for (RenderType renderType : SUPPORTED_RENDERTYPES) {
                if (cancelled) return;
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferBuilder = tesselator.begin(renderType.mode, renderType.format);
                long ptr = tesselator.buffer.pointer;
                int offsetBeforeCompile = tesselator.buffer.writeOffset;
                for (CacheableBlockEntity cacheableBlockEntity : blockEntityList) {
                    @SuppressWarnings("ALL")
                    CacheableBlockEntityRenderer renderer = cacheableBlockEntity.getRenderer();
                    if (renderer == null) continue;
                    poseStack.pushPose();
                    BlockPos pos = cacheableBlockEntity.getBlockPos();
                    poseStack.translate(
                        pos.getX(),
                        pos.getY(),
                        pos.getZ()
                    );
                    renderer.compileRenderType(
                        cacheableBlockEntity,
                        renderType,
                        bufferBuilder,
                        poseStack
                    );
                    poseStack.popPose();
                }
                if (bufferBuilder.vertices > 0) {
                    RenderRegion.this.isEmpty = false;
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
                compileResultMap.put(renderType, compileResult);
            }
            RenderRegion.this.compileResultMap.values().forEach(CompileResult::free);
            RenderRegion.this.compileResultMap = compileResultMap;
            compileResultMap.forEach((renderType, compileResult) ->
                pipeline.submitUploadTask(() -> {
                        VertexBuffer vb = buffers.get(renderType);
                        compileResult.upload(vb);
                    }
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
