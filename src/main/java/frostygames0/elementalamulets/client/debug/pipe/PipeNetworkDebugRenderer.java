package frostygames0.elementalamulets.client.debug.pipe;

import com.mojang.blaze3d.vertex.PoseStack;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.debug.ModDebugRenderers;
import frostygames0.elementalamulets.network.debug.DebugPipeNetworkPayload;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

import java.util.HashMap;
import java.util.Map;

public class PipeNetworkDebugRenderer implements ModDebugRenderers.IRendererWithInfo<DebugPipeNetworkPayload.PipeNetworkInfo> {
    public static final ResourceLocation ID = ElementalAmulets.id("debug/pipe_network");

    private final Map<BlockPos, DebugPipeNetworkPayload.PipeNetworkInfo> networks = new HashMap<>();

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPos, BlockPos cameraBlockPos) {
        for (var info : networks.values()) {
            var startingPos = info.startingPos();

            if (!startingPos.closerThan(cameraBlockPos, 10)) {
                continue;
            }

            var shape = Shapes.block().move(startingPos.getX(), startingPos.getY(), startingPos.getZ());

            DebugRenderer.renderVoxelShape(poseStack,
                    bufferSource.getBuffer(RenderType.LINES),
                    shape,
                    -cameraPos.x, -cameraPos.y, -cameraPos.z, 1f, 1f, 0f, 1f, false);

            for (var popa : info.targets()) {
                var shape2 = Shapes.block().move(popa.getX(), popa.getY(), popa.getZ());

                DebugRenderer.renderVoxelShape(poseStack,
                        bufferSource.getBuffer(RenderType.LINES),
                        shape2,
                        -cameraPos.x, -cameraPos.y, -cameraPos.z, 0f, 1f, 0f, 1f, false);
            }
        }
    }

    @Override
    public void provideInfo(DebugPipeNetworkPayload.PipeNetworkInfo debugInfo) {
        if (debugInfo.removal()) {
            networks.remove(debugInfo.startingPos());
            return;
        }

        networks.put(debugInfo.startingPos(), debugInfo);
    }

    @Override
    public void reset() {
        networks.clear();
    }
}
