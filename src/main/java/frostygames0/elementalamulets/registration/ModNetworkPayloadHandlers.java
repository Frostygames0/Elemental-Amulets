package frostygames0.elementalamulets.registration;

import frostygames0.elementalamulets.client.ClientPacketHandler;
import frostygames0.elementalamulets.network.SyncElementStorageWithClientMenu;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworkPayloadHandlers {
    private ModNetworkPayloadHandlers() {
    }

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                SyncElementStorageWithClientMenu.TYPE,
                SyncElementStorageWithClientMenu.STREAM_CODEC,
                ClientPacketHandler::handleSyncElementStorageWithClientMenu);
    }
}
