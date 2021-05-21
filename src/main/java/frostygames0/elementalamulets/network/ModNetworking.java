package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworking {
    private static SimpleChannel INSTANCE;
    private static int ID = 0;
    private static final String PROTOCOL_VERSION = "1";

    private static int nextInt() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ElementalAmulets.MOD_ID, "smpl_channel"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals);
        INSTANCE.messageBuilder(AmuletAnimationPacket.class, nextInt())
                .encoder(AmuletAnimationPacket::toBytes)
                .decoder(AmuletAnimationPacket::new)
                .consumer(AmuletAnimationPacket::handle)
                .add();
        INSTANCE.messageBuilder(CombineElementalPacket.class, nextInt())
                .encoder(CombineElementalPacket::toBytes)
                .decoder(CombineElementalPacket::new)
                .consumer(CombineElementalPacket::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayerEntity player) {
        INSTANCE.sendTo(packet, player.connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }

}
