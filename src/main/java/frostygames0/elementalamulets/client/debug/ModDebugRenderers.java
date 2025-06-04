package frostygames0.elementalamulets.client.debug;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.logging.LogUtils;
import frostygames0.elementalamulets.client.debug.pipe.ElementalPipeDebugRenderer;
import frostygames0.elementalamulets.client.debug.pipe.PipeNetworkDebugRenderer;
import frostygames0.elementalamulets.network.debug.DebugPipeNetworkPayload;
import frostygames0.elementalamulets.network.debug.IDebugInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ModDebugRenderers {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final List<RendererEntry> renderers = new ArrayList<>();
    private static final Map<ResourceLocation, IRendererWithInfo<?>> renderersWithAdditionalInfo = new HashMap<>();

    private static void registerDebugRenderer(String displayName, IRenderer instance) {
        registerDebugRenderer(displayName, instance, null);
    }

    private static void registerDebugRenderer(String displayName, IRenderer instance, @Nullable ResourceLocation payloadId) {
        var entry = new RendererEntry(displayName, instance);
        renderers.add(entry);

        if (payloadId != null && instance instanceof IRendererWithInfo<?> rendererWithInfo) {
            renderersWithAdditionalInfo.put(payloadId, rendererWithInfo);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends IDebugInfo> boolean tryProvideWithDebugInfo(ResourceLocation payloadId, T debugInfo) {
        var rendererInstance = renderersWithAdditionalInfo.get(payloadId);
        if (rendererInstance == null) {
            LOGGER.warn("Failed to handle custom debug info payload with id {}. There is no such debug renderer!", payloadId);
            return false;
        }

        var rendererWithInfo = (IRendererWithInfo<T>) rendererInstance;
        try {
            rendererWithInfo.provideInfo(debugInfo);
        } catch (ClassCastException e) {
            LOGGER.warn("Failed to handle custom debug info payload with id {}. Caused by: {}", payloadId, e.getMessage());
            return false;
        }

        return true;
    }

    public static Iterable<RendererEntry> getRendererEntries() {
        return renderers;
    }

    private static void registerDebugRenderers() {
        var minecraft = Minecraft.getInstance();

        registerDebugRenderer("Elemental Pipe Flow", new ElementalPipeDebugRenderer(minecraft));
        registerDebugRenderer("Pipe Network", new PipeNetworkDebugRenderer(), DebugPipeNetworkPayload.ID);
    }

    public static void register(IEventBus eventBus) {
        if (FMLLoader.isProduction()) {
            return;
        }

        registerDebugRenderers();

        eventBus.addListener(ModDebugRenderers::onRenderLevelStage);
        eventBus.addListener(ModDebugRenderers::onClientPlayerLogin);
        eventBus.addListener(ModDebugRenderers::onClientTick);
    }

    private static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_BLOCK_ENTITIES) {
            return;
        }

        var poseStack = event.getPoseStack();

        var camera = event.getCamera();
        var cameraPos = camera.getPosition();
        var cameraBlockPos = camera.getBlockPosition();

        var bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        for (var entry : renderers) {
            var isRendered = entry.isRendered();
            if (!isRendered) {
                continue;
            }

            entry.instance.render(poseStack, bufferSource, cameraPos, cameraBlockPos);
        }

        bufferSource.endLastBatch();
    }

    private static void onClientPlayerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        renderers.forEach(entry -> entry.instance.reset());
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        var minecraft = Minecraft.getInstance();
        if (InputConstants.isKeyDown(minecraft.getWindow().getWindow(), InputConstants.KEY_RSHIFT)) {
            if (minecraft.screen != null) {
                return;
            }

            var player = minecraft.player;
            if (player == null) {
                return;
            }

            if (!player.hasPermissions(Commands.LEVEL_GAMEMASTERS)) {
                return;
            }

            minecraft.setScreen(new ModDebugRenderersScreen());
        }
    }

    public static class RendererEntry {
        private final String displayName;
        private final IRenderer instance;

        private boolean isRendered;

        private RendererEntry(String displayName, IRenderer instance) {
            this.displayName = displayName;
            this.instance = instance;
        }

        public void setIsRendered(boolean isRendered) {
            this.isRendered = isRendered;
        }

        public boolean isRendered() {
            return isRendered;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public interface IRenderer {
        void render(PoseStack poseStack, MultiBufferSource bufferSource, Vec3 cameraPos, BlockPos cameraBlockPos);

        default void reset() {
        }
    }

    public interface IRendererWithInfo<T extends IDebugInfo> extends IRenderer {
        void provideInfo(T debugInfo);
    }
}
