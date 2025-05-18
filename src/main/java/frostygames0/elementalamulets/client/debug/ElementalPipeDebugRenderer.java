package frostygames0.elementalamulets.client.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import frostygames0.elementalamulets.block.entity.pipe.ElementalPipeBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

public class ElementalPipeDebugRenderer implements BlockEntityRenderer<ElementalPipeBlockEntity> {

    @Override
    public void render(ElementalPipeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        var blockPos = blockEntity.getBlockPos();

//        var camera = Minecraft.getInstance().gameRenderer.getMainCamera();
//        if (camera.isInitialized())
//        {
//            var camPos = camera.getPosition();
//            double d0 = (double)blockPos.getX() - camPos.x;
//            double d1 = (double)blockPos.getY() - camPos.y;
//            double d2 = (double)blockPos.getZ() - camPos.z;
//            double d3 = d0 + 1.0;
//            double d4 = d1 + 1.0;
//            double d5 = d2 + 1.0;
//            ShapeRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.LINES), d0, d1, d2, d3, d4, d5, 1f, 0, 0, 1);
//        }

        var i = 7;
        renderTextOverPos(poseStack, bufferSource, "Elements in each connection", blockPos, i--, 0xFFFF0000);
        for (var direction : Direction.values()) {
            var elementInsideConnection = blockEntity.getElement(direction, false).orElse(blockEntity.getElement(direction, true).orElse(null));
            var text = String.format("%s - %s", direction.getName(), elementInsideConnection != null ? elementInsideConnection.value().name().getString() : "No Flow");
            renderTextOverPos(poseStack, bufferSource, text, blockPos, i--, -1);
        }
    }

    @Override
    public AABB getRenderBoundingBox(ElementalPipeBlockEntity blockEntity) {
        return AABB.INFINITE;
    }

    private static void renderTextOverPos(PoseStack poseStack, MultiBufferSource buffer, String text, BlockPos pos, int layer, int color) {
        double x = pos.getX() + 1;
        double y = pos.getY() + 2 + layer * 0.2;
        double z = pos.getZ() + 1;
        DebugRenderer.renderFloatingText(poseStack, buffer, text, x, y, z, color, 0.02F, true, 0.0F, true);
    }
}
