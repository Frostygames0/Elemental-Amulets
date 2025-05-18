package frostygames0.elementalamulets.client.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.core.BlockPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME)
public class ModDebugRenderer {
    @SubscribeEvent
    private static void onRenderLevelStageEvent(RenderLevelStageEvent event) {
//        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_LEVEL) {
//            var minecraft = Minecraft.getInstance();
//
//            var hitResult = minecraft.hitResult;
//            if (hitResult == null) {
//                return;
//            }
//
//            if (hitResult.getType() == HitResult.Type.BLOCK) {
//                var blockHitResult = (BlockHitResult) hitResult;
//                var blockPos = blockHitResult.getBlockPos();
//
//                var storage = event.getLevel().getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, blockPos, null);
//                if (storage != null) {
//                    var poseStack = event.getPoseStack();
//                    var bufferSource = minecraft.renderBuffers().bufferSource();
//                    renderTextOverPos(poseStack, bufferSource, String.format("Storage - %d", storage.getTotalAmount()), blockPos, 0, -1);
//                }
//            }
//        }
    }

    private static void renderTextOverPos(PoseStack poseStack, MultiBufferSource buffer, String text, BlockPos pos, int layer, int color) {
        double x = pos.getX() + 1;
        double y = pos.getY() + 1 + layer * 0.2;
        double z = pos.getZ() + 1;
        DebugRenderer.renderFloatingText(poseStack, buffer, text, x, y, z, color, 0.02F, true, 0.0F, true);
    }
}
