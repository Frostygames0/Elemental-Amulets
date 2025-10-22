package frostygames0.elementalamulets.client.renderer.debug.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import frostygames0.elementalamulets.block.entity.pipe.BaseElementalPipeBlockEntity;
import frostygames0.elementalamulets.block.entity.pipe.PipeConnection;
import frostygames0.elementalamulets.client.renderer.debug.ModDebugRenderers;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.network.debug.IDebugInfo;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ElementalPipeGeneralDebugRenderer implements ModDebugRenderers.IRendererWithInfo<ElementalPipeGeneralDebugRenderer.PipeDebugInfo> {
    private final List<WeakReference<BaseElementalPipeBlockEntity>> pipes = new ArrayList<>();

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPos, BlockPos cameraBlockPos) {
        forEachPipeSafe(cameraBlockPos, pipe -> {
            var blockPos = pipe.getBlockPos();
            var i = 7;
            renderTextOverPos(poseStack, bufferSource, "Connections", blockPos, i--, 0xFFFF0000);
            for (var direction : Direction.values()) {
                var connection = pipe.getConnection(direction);
                if (connection != null) {
                    renderTextOverPos(poseStack, bufferSource, connection.toString(), blockPos, i--, -1);

                    if (connection.hasPressure()) {
                        renderPressure(poseStack, bufferSource, direction, blockPos, cameraPos, connection.getInboundPressure(), true);
                    }

                    if (connection.hasFlow()) {
                        var color = connection.isFlowComplete() ? 0xFF7bff00 : 0xFFff8000;
                        var colorVector = ARGB.vector3fFromRGB24(color);
                        renderPipeShapeLine(direction, blockPos, colorVector.x, colorVector.y, colorVector.z, 1f, poseStack, cameraPos, bufferSource);
                    }
                }
            }
        });
    }

    private void forEachPipeSafe(BlockPos cameraPos, Consumer<BaseElementalPipeBlockEntity> onPipe) {
        var iterator = pipes.iterator();
        while (iterator.hasNext()) {
            var pipeRef = iterator.next();
            if (pipeRef == null) {
                iterator.remove();
                continue;
            }

            var pipe = pipeRef.get();
            if (pipe == null || pipe.isRemoved()) {
                iterator.remove();
                continue;
            }

            if (!pipe.getBlockPos().closerThan(cameraPos, 30)) {
                continue;
            }

            onPipe.accept(pipe);
        }
    }

    @Override
    public void provideInfo(PipeDebugInfo debugInfo) {
        pipes.add(new WeakReference<>(debugInfo.inner));
    }

    public record PipeDebugInfo(BaseElementalPipeBlockEntity inner) implements IDebugInfo {
    }

    private static void renderTextOverPos(PoseStack poseStack, MultiBufferSource buffer, String text, BlockPos pos, int layer, int color) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + layer * 0.2;
        double z = pos.getZ() + 0.5;
        DebugRenderer.renderFloatingText(poseStack, buffer, text, x, y, z, color, 0.005F, true, 0.0F, true);
    }

    private static void renderPipeShapeLine(Direction direction, BlockPos pos, float r, float g, float b, float a, PoseStack poseStack, Vec3 cameraPos, MultiBufferSource buffer) {
        var shape = getShape(direction).move(pos.getX(), pos.getY(), pos.getZ());

        var vertexConsumer = buffer.getBuffer(RenderType.lines());

        DebugRenderer.renderVoxelShape(poseStack,
                vertexConsumer,
                shape,
                -cameraPos.x, -cameraPos.y, -cameraPos.z,
                r, g, b, a, false);

        poseStack.pushPose();
        var offset = pos.getCenter().subtract(cameraPos);
        poseStack.translate(offset.x, offset.y, offset.z);

        var aboba = shape.bounds().move(-pos.getX(), -pos.getY(), -pos.getZ());
        ShapeRenderer.renderLineBox(poseStack, vertexConsumer, aboba, 0f, 0f, 1f, 1f);
        poseStack.popPose();
    }

    private static void renderPressure(PoseStack poseStack, MultiBufferSource bufferSource, Direction direction, BlockPos pos, Vec3 cameraPos, float pressure, boolean inbound) {
        var vertexConsumer = bufferSource.getBuffer(RenderType.lines());

        poseStack.pushPose();
        poseStack.translate(pos.getCenter().subtract(cameraPos));

        var aabb2 = calculatePipeShape(1 / 8f, direction);

        var alpha = (pressure / PipeConnection.MAX_PRESSURE);
        ShapeRenderer.renderLineBox(poseStack, vertexConsumer, aabb2, 1f, 1f, 1f, 0.5f);
        poseStack.popPose();
    }

    private static AABB calculatePipeShape(float apothem, Direction direction) {
        return new AABB(
                0.5 + Math.min(-apothem, direction.getStepX() * 0.5),
                0.5 + Math.min(-apothem, direction.getStepY() * 0.5),
                0.5 + Math.min(-apothem, direction.getStepZ() * 0.5),
                0.5 + Math.max(apothem, direction.getStepX() * 0.5),
                0.5 + Math.max(apothem, direction.getStepY() * 0.5),
                0.5 + Math.max(apothem, direction.getStepZ() * 0.5));
    }

    private static VoxelShape getShape(Direction direction) {
        return ModBlocks.ELEMENTAL_PIPE.get().getShapeByDirection(direction);
    }
}
