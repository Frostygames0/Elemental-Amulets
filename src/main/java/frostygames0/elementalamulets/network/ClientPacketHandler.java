package frostygames0.elementalamulets.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * @author Frostygames0
 * @date 24.01.2022 19:36
 */
public class ClientPacketHandler {
    static void handleUpdatePlayerVelocity(CUpdatePlayerVelocityPacket msg) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.setDeltaMovement(msg.x(), msg.y(), msg.z());
        }
    }


}
