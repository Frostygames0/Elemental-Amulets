package frostygames0.elementalamulets.client.renderer.block.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.entity.SimpleStorageBlockEntity;
import frostygames0.elementalamulets.client.renderer.ModRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleStorageBlockEntityRenderer implements BlockEntityRenderer<SimpleStorageBlockEntity> {
    @SuppressWarnings("deprecation")
    private static final Material BAR_OVERLAY_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ElementalAmulets.id("block/direction_test"));

    private static final Direction[] DIRECTIONS = Direction.values();

    private final BlockRenderDispatcher dispatcher;

    public SimpleStorageBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        dispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public int getViewDistance() {
        return 10000;
    }

    @Override
    public void render(SimpleStorageBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        @Nullable var level = blockEntity.getLevel();

        var blockPos = blockEntity.getBlockPos();
        var blockState = blockEntity.getBlockState();

        var buffer = BAR_OVERLAY_MATERIAL.buffer(bufferSource, ModRenderTypes::entityTranslucentZOffset2);

        var mutableBlockPos = blockPos.mutable();
        for (var face : DIRECTIONS) {
            mutableBlockPos.setWithOffset(blockPos, face);

            if (level == null || Block.shouldRenderFace(level, blockPos, blockState, level.getBlockState(mutableBlockPos), face)) {
                var light = getLightColorOrFallback(level, blockState, mutableBlockPos);
                var color = ARGB.color(127, 255, 0, 0);
                renderTextureOnFullBlockFace2(poseStack, buffer, BlockPos.ZERO, face, 0, 0, 1, 1, 16, 16, 0, 1, 0, 1, color, light, packedOverlay);
            }
        }
    }

    private static int getLightColorOrFallback(@Nullable BlockAndTintGetter level, BlockState blockState, BlockPos blockPos) {
        if (level == null) {
            return LightTexture.FULL_BRIGHT;
        }

        return LevelRenderer.getLightColor(level, blockState, blockPos);
    }

    private static void renderTexture(PoseStack poseStack,
                                      VertexConsumer buffer,
                                      BlockPos blockPos,
                                      Direction face,
                                      int x, int y,
                                      float uOffset,
                                      float vOffset,
                                      int uWidth,
                                      int vHeight,
                                      int width,
                                      int height,
                                      int textureWidth,
                                      int textureHeight, int color, int packedLight) {
//        renderTextureOnFullBlockFace(poseStack, buffer, blockPos, face, width, height,
//                (uOffset + 0.0F) / (float) textureWidth,
//                (uOffset + (float) width) / (float) textureWidth,
//                (vOffset + 0.0F) / (float) textureHeight,
//                (vOffset + (float)height) / (float)textureHeight, color, packedLight, OverlayTexture.NO_OVERLAY);

        renderTextureOnFullBlockFace2(poseStack, buffer, blockPos, face,
                x / 16f, y / 16f,
                (x + uWidth) / 16f, (y + vHeight) / 16f,
                width, height,
                (uOffset + 0.0F) / (float) textureWidth,
                (uOffset + (float) width) / (float) textureWidth,
                (vOffset + 0.0F) / (float) textureHeight,
                (vOffset + (float) height) / (float) textureHeight, color, packedLight, OverlayTexture.NO_OVERLAY
        );
    }

    private static void renderTextureOnFullBlockFace2(PoseStack poseStack,
                                                      VertexConsumer buffer,
                                                      BlockPos blockPos,
                                                      Direction face,
                                                      float x1, float y1,
                                                      float x2, float y2,
                                                      int width, int height,
                                                      float minU, float maxU,
                                                      float minV, float maxV,
                                                      int color,
                                                      int packedLight,
                                                      int packedOverlay) {

        var pose = poseStack.last();

        float a1 = blockPos.getX();
        float b1 = blockPos.getY();
        float c1 = blockPos.getZ();

        float a2 = a1;
        float b2 = b1;
        float c2 = c1;

        final var zOffest = 0f;

        switch (face) {
            case DOWN:
                a1 += x1;
                c1 += y1;

                a2 += x2;
                c2 += y2;

                b1 -= zOffest;

                addVertex(buffer, pose, a1, b1, c1, color, maxU, maxV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, a2, b1, c1, color, maxU, minV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, a2, b1, c2, color, minU, minV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, a1, b1, c2, color, minU, maxV, packedLight, packedOverlay, 0, -1, 0);
                break;
            case UP:
                a1 += x1;
                c1 += y1;

                a2 += x2;
                c2 += y2;
                b2 += 1 + zOffest;

                addVertex(buffer, pose, a1, b2, c1, color, minU, minV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, a1, b2, c2, color, minU, maxV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, a2, b2, c2, color, maxU, maxV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, a2, b2, c1, color, maxU, minV, packedLight, packedOverlay, 0, 1, 0);
                break;
            case NORTH:
                a1 += x1;
                b1 += y1;
                c1 -= zOffest;

                a2 += x2;
                b2 += y2;

                addVertex(buffer, pose, a1, b1, c1, color, maxU, maxV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, a1, b2, c1, color, maxU, minV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, a2, b2, c1, color, minU, minV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, a2, b1, c1, color, minU, maxV, packedLight, packedOverlay, 0, 0, -1);
                break;
            case SOUTH:
                a1 += x1;
                b1 += y1;

                a2 += x2;
                b2 += y2;
                c2 += 1 + zOffest;

                addVertex(buffer, pose, a1, b1, c2, color, minU, maxV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, a2, b1, c2, color, maxU, maxV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, a2, b2, c2, color, maxU, minV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, a1, b2, c2, color, minU, minV, packedLight, packedOverlay, 0, 0, 1);
                break;
            case WEST:
                b1 += x1;
                c1 += y1;

                a1 -= zOffest;
                b2 += x2;
                c2 += y2;

                addVertex(buffer, pose, a1, b1, c1, color, minU, maxV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, a1, b1, c2, color, maxU, maxV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, a1, b2, c2, color, maxU, minV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, a1, b2, c1, color, minU, minV, packedLight, packedOverlay, -1, 0, 0);
                break;
            case EAST:
                b1 += x1;
                c1 += y1;

                a2 += 1 + zOffest;
                b2 += x2;
                c2 += y2;

                addVertex(buffer, pose, a2, b1, c2, color, maxU, maxV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, a2, b1, c1, color, maxU, minV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, a2, b2, c1, color, minU, minV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, a2, b2, c2, color, minU, maxV, packedLight, packedOverlay, 1, 0, 0);
                break;
        }
    }

    private static void renderTextureOnFullBlockFaceNEW(PoseStack poseStack,
                                                        VertexConsumer buffer,
                                                        BlockPos blockPos,
                                                        Direction face,
                                                        float x1, float y1,
                                                        float x2, float y2,
                                                        int width, int height,
                                                        float minU, float maxU,
                                                        float minV, float maxV,
                                                        int color,
                                                        int packedLight,
                                                        int packedOverlay) {

        var pose = poseStack.last();

        float a1 = blockPos.getX();
        float b1 = blockPos.getY();
        float c1 = blockPos.getZ();

        float a2 = a1;
        float b2 = b1;
        float c2 = c1;

        final var zOffest = 0.0001f;

        switch (face) {
            case DOWN:
                a1 += x1;
                c1 += y1;

                a2 += x2;
                c2 += y2;

                b1 -= zOffest;

                addVertex(buffer, pose, a1, b1, c1, color, maxU, maxV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, a2, b1, c1, color, maxU, minV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, a2, b1, c2, color, minU, minV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, a1, b1, c2, color, minU, maxV, packedLight, packedOverlay, 0, -1, 0);
                break;
            case UP:
                a1 += x1;
                c1 += y1;

                a2 += x2;
                c2 += y2;
                b2 += 1 + zOffest;

                addVertex(buffer, pose, a1, b2, c1, color, minU, minV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, a1, b2, c2, color, minU, maxV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, a2, b2, c2, color, maxU, maxV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, a2, b2, c1, color, maxU, minV, packedLight, packedOverlay, 0, 1, 0);
                break;
            case NORTH:
                a1 += x1;
                b1 += y1;
                c1 -= zOffest;

                a2 += x2;
                b2 += y2;

                addVertex(buffer, pose, a1, b1, c1, color, maxU, maxV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, a1, b2, c1, color, maxU, minV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, a2, b2, c1, color, minU, minV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, a2, b1, c1, color, minU, maxV, packedLight, packedOverlay, 0, 0, -1);
                break;
            case SOUTH:
                a1 += x1;
                b1 += y1;

                a2 += x2;
                b2 += y2;
                c2 += 1 + zOffest;

                addVertex(buffer, pose, a1, b1, c2, color, minU, maxV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, a2, b1, c2, color, maxU, maxV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, a2, b2, c2, color, maxU, minV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, a1, b2, c2, color, minU, minV, packedLight, packedOverlay, 0, 0, 1);
                break;
            case WEST:
                b1 += x1;
                c1 += y1;

                a1 -= zOffest;
                b2 += x2;
                c2 += y2;

                addVertex(buffer, pose, a1, b1, c1, color, minU, maxV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, a1, b1, c2, color, maxU, maxV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, a1, b2, c2, color, maxU, minV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, a1, b2, c1, color, minU, minV, packedLight, packedOverlay, -1, 0, 0);
                break;
            case EAST:
                b1 += x1;
                c1 += y1;

                a2 += 1 + zOffest;
                b2 += x2;
                c2 += y2;

                addVertex(buffer, pose, a2, b1, c2, color, maxU, maxV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, a2, b1, c1, color, maxU, minV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, a2, b2, c1, color, minU, minV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, a2, b2, c2, color, minU, maxV, packedLight, packedOverlay, 1, 0, 0);
                break;
        }
    }

    // TODO Refactor it later
    private static void renderTextureOnFullBlockFace(PoseStack poseStack,
                                                     VertexConsumer buffer,
                                                     BlockPos blockPos,
                                                     Direction face,
                                                     int width, int height,
                                                     float minU, float maxU,
                                                     float minV, float maxV,
                                                     int color,
                                                     int packedLight,
                                                     int packedOverlay) {

        var pose = poseStack.last();

        float x1 = blockPos.getX();
        float y1 = blockPos.getY();
        float z1 = blockPos.getZ();

        float x2 = x1 + 1;
        float y2 = y1 + 1;
        float z2 = z1 + 1;

        final var offsetFromEdgeX = ((16f - width) / 2) / 16f;
        final var offsetFromEdgeY = ((16f - height) / 2) / 16f;
        final var zOffest = 0.0001f;

        switch (face) {
            case DOWN:
                x1 += offsetFromEdgeX;
                z1 += offsetFromEdgeY;

                x2 -= offsetFromEdgeX;
                z2 -= offsetFromEdgeY;
                y1 -= zOffest;

                addVertex(buffer, pose, x1, y1, z1, 0xFFAAAAAA, maxU, maxV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, x2, y1, z1, color, maxU, minV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, x2, y1, z2, color, minU, minV, packedLight, packedOverlay, 0, -1, 0);
                addVertex(buffer, pose, x1, y1, z2, color, minU, maxV, packedLight, packedOverlay, 0, -1, 0);
                break;
            case UP:
                x1 += offsetFromEdgeX;
                z1 += offsetFromEdgeY;

                x2 -= offsetFromEdgeX;
                z2 -= offsetFromEdgeY;
                y2 += zOffest;

                addVertex(buffer, pose, x1, y2, z1, color, minU, minV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, x1, y2, z2, color, minU, maxV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, x2, y2, z2, color, maxU, maxV, packedLight, packedOverlay, 0, 1, 0);
                addVertex(buffer, pose, x2, y2, z1, color, maxU, minV, packedLight, packedOverlay, 0, 1, 0);
                break;
            case NORTH:
                x1 += offsetFromEdgeX;
                y1 += offsetFromEdgeY;
                z1 -= zOffest;

                x2 -= offsetFromEdgeX;
                y2 -= offsetFromEdgeY;

                addVertex(buffer, pose, x1, y1, z1, color, maxU, maxV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, x1, y2, z1, color, maxU, minV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, x2, y2, z1, color, minU, minV, packedLight, packedOverlay, 0, 0, -1);
                addVertex(buffer, pose, x2, y1, z1, color, minU, maxV, packedLight, packedOverlay, 0, 0, -1);
                break;
            case SOUTH:
                x1 += offsetFromEdgeX;
                y1 += offsetFromEdgeY;

                x2 -= offsetFromEdgeX;
                y2 -= offsetFromEdgeY;
                z2 += zOffest;

                addVertex(buffer, pose, x1, y1, z2, color, minU, maxV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, x2, y1, z2, color, maxU, maxV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, x2, y2, z2, color, maxU, minV, packedLight, packedOverlay, 0, 0, 1);
                addVertex(buffer, pose, x1, y2, z2, color, minU, minV, packedLight, packedOverlay, 0, 0, 1);
                break;
            case WEST:
                y1 += offsetFromEdgeY;
                z1 += offsetFromEdgeX;

                x1 -= zOffest;
                y2 -= offsetFromEdgeY;
                z2 -= offsetFromEdgeX;

                addVertex(buffer, pose, x1, y1, z1, color, minU, maxV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, x1, y1, z2, color, maxU, maxV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, x1, y2, z2, color, maxU, minV, packedLight, packedOverlay, -1, 0, 0);
                addVertex(buffer, pose, x1, y2, z1, color, minU, minV, packedLight, packedOverlay, -1, 0, 0);
                break;
            case EAST:
                y1 += offsetFromEdgeY;
                z1 += offsetFromEdgeX;

                x2 += zOffest;
                y2 -= offsetFromEdgeY;
                z2 -= offsetFromEdgeX;

                addVertex(buffer, pose, x2, y1, z1, color, maxU, maxV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, x2, y2, z1, color, maxU, minV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, x2, y2, z2, color, minU, minV, packedLight, packedOverlay, 1, 0, 0);
                addVertex(buffer, pose, x2, y1, z2, color, minU, maxV, packedLight, packedOverlay, 1, 0, 0);
                break;
        }
    }

    private static void addVertex(VertexConsumer vertexConsumer,
                                  PoseStack.Pose pose,
                                  float x,
                                  float y,
                                  float z,
                                  int color,
                                  float u,
                                  float v,
                                  int packedLight,
                                  int packedOverlay,
                                  float normalX,
                                  float normalY,
                                  float normalZ) {
        vertexConsumer.addVertex(pose, x, y, z)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(packedOverlay)
                .setLight(packedLight)
                .setNormal(pose, normalX, normalY, normalZ);
    }
}
