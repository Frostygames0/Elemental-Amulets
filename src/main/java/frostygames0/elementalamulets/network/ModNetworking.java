package frostygames0.elementalamulets.network;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 21.09.2021 22:01
 */
public class ModNetworking {
    private static final String PROTOCOL_NETWORK = "1.0";
    private static int ID = 0;

    public static SimpleChannel INSTANCE;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(modPrefix("main"),
                () -> PROTOCOL_NETWORK,
                PROTOCOL_NETWORK::equals,
                PROTOCOL_NETWORK::equals);

        INSTANCE.messageBuilder(SOpenAmuletBeltGUIPacket.class, nextID())
                .encoder(((cOpenAmuletBeltGUIPacket, packetBuffer) -> {}))
                .decoder(buf -> new SOpenAmuletBeltGUIPacket())
                .consumer(SOpenAmuletBeltGUIPacket::handle)
                .add();
        INSTANCE.messageBuilder(CUpdatePlayerVelocityPacket.class, nextID())
                .encoder(CUpdatePlayerVelocityPacket::toBytes)
                .decoder(CUpdatePlayerVelocityPacket::new)
                .consumer(CUpdatePlayerVelocityPacket::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
