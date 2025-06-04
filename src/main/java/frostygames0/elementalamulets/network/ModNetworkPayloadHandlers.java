package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.client.ClientPacketHandler;
import frostygames0.elementalamulets.network.debug.DebugPipeNetworkPayload;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworkPayloadHandlers {
    private ModNetworkPayloadHandlers() {
    }

    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                ClientboundElementStorageMenuPayload.TYPE,
                ClientboundElementStorageMenuPayload.STREAM_CODEC,
                ClientPacketHandler::handleSyncElementStorageWithClientMenu);

        registerDebugInfoPayloads(registrar);
    }

    private static void registerDebugInfoPayloads(PayloadRegistrar registrar) {
        if (FMLLoader.isProduction()) {
            return;
        }

        registrar.playToClient(DebugPipeNetworkPayload.TYPE, DebugPipeNetworkPayload.STREAM_CODEC, ClientPacketHandler::handleCustomDebugInfo);
    }
}
