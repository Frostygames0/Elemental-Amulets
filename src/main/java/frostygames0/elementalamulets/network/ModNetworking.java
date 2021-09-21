package frostygames0.elementalamulets.network;

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

        INSTANCE.messageBuilder(COpenAmuletBeltGUIPacket.class, nextID())
                .encoder(((cOpenAmuletBeltGUIPacket, packetBuffer) -> {}))
                .decoder(buf -> new COpenAmuletBeltGUIPacket())
                .consumer(COpenAmuletBeltGUIPacket::handle)
                .add();
    }
}
