package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.client.ClientPacketHandler;
import frostygames0.elementalamulets.network.SendElementStorageToClientMenu;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworkPayloadHandlers {
    private ModNetworkPayloadHandlers() {
    }

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                SendElementStorageToClientMenu.TYPE,
                SendElementStorageToClientMenu.STREAM_CODEC,
                ClientPacketHandler::handleSyncElementStorageWithClientMenu);
    }
}
