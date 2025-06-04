package frostygames0.elementalamulets.client;

import com.mojang.logging.LogUtils;
import frostygames0.elementalamulets.client.debug.ModDebugRenderers;
import frostygames0.elementalamulets.inventory.menu.SyncedElementalStorageMenu;
import frostygames0.elementalamulets.network.ClientboundElementStorageMenuPayload;
import frostygames0.elementalamulets.network.debug.IDebugInfoPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public final class ClientPacketHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    private ClientPacketHandler() {
    }

    public static void handleSyncElementStorageWithClientMenu(ClientboundElementStorageMenuPayload message, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (message.containerId() == player.containerMenu.containerId) {
                if (player.containerMenu instanceof SyncedElementalStorageMenu menu) {
                    menu.setStorage(message.stored());
                }
            }
        });
    }

    public static void handleCustomDebugInfo(IDebugInfoPayload<?> payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            var payloadId = payload.getId();
            if (payloadId == null) {
                LOGGER.warn("Failed to handle debug info payload, id is null!");
                return;
            }

            if (!ModDebugRenderers.tryProvideWithDebugInfo(payloadId, payload.getDebugInfo())) {
                LOGGER.warn("Failed to handle debug info payload, failed to provide renderer with info!");
            }
        });
    }
}
