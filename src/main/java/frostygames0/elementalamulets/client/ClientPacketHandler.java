package frostygames0.elementalamulets.client;

import frostygames0.elementalamulets.inventory.menu.SyncedElementalStorageMenu;
import frostygames0.elementalamulets.network.SendElementStorageToClientMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class ClientPacketHandler {
    private ClientPacketHandler() {
    }

    public static void handleSyncElementStorageWithClientMenu(SendElementStorageToClientMenu message, IPayloadContext context) {
        context.enqueueWork(() -> {
            var player = context.player();
            if (message.containerId() == player.containerMenu.containerId) {
                if (player.containerMenu instanceof SyncedElementalStorageMenu menu) {
                    menu.setStored(message.stored());
                }
            }
        });
    }
}
