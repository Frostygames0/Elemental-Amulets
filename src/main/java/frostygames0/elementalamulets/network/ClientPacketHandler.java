package frostygames0.elementalamulets.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;

/**
 * @author Frostygames0
 * @date 24.01.2022 19:36
 */
public class ClientPacketHandler {
    static void handleUpdatePlayerVelocity(CUpdatePlayerVelocityPacket msg) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.setDeltaMovement(msg.x, msg.y, msg.z);
        }
    }


}
