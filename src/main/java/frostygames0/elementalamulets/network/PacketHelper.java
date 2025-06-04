package frostygames0.elementalamulets.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Objects;

public class PacketHelper {
    public static void sendToPlayerIfHasPermission(ServerPlayer serverPlayer, int permissionLevel, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        if (serverPlayer.hasPermissions(permissionLevel)) {
            PacketDistributor.sendToPlayer(serverPlayer, payload, payloads);
        }
    }

    public static void sendToPlayersIfHavePermissions(int permissionLevel, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        var server = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer(), "Cannot send clientbound payloads on the client");
        var playerList = server.getPlayerList();

        for (var player : playerList.getPlayers()) {
            sendToPlayerIfHasPermission(player, permissionLevel, payload, payloads);
        }
    }
}
