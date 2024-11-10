package dev.dubhe.anvilcraft.client.renderer.laser;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.dubhe.anvilcraft.AnvilCraft;
import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;

public record LaserState(
    BaseLaserBlockEntity blockEntity,
    BlockPos pos,
    float length,
    float offset,
    PoseStack.Pose pose,
    TextureAtlasSprite atlasSprite
) {
    public static LaserState create(BaseLaserBlockEntity blockEntity, PoseStack poseStack){
        if (blockEntity.getLevel() == null) return null;
        if (blockEntity.irradiateBlockPos == null) return null;
        final TextureAtlasSprite sprite = Minecraft.getInstance()
            .getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
            .apply(AnvilCraft.of("block/laser"));
        poseStack.pushPose();
        poseStack.translate(0.5f, 0.5f, 0.5);
        float length = (float) (blockEntity
            .irradiateBlockPos
            .getCenter()
            .distanceTo(blockEntity.getBlockPos().getCenter()) - 0.5);
        poseStack.mulPose(blockEntity.getDirection().getRotation());
        LaserState laserState = new LaserState(
            blockEntity,
            blockEntity.getBlockPos(),
            length,
            blockEntity.laserOffset(),
            poseStack.last(),
            sprite
        );
        poseStack.popPose();
        return laserState;
    }
}
