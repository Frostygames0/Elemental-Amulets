package frostygames0.elementalamulets.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;


import java.util.function.Supplier;

/**
 * @author Frostygames0
 * @date 29.09.2021 21:52
 */
public class CUpdatePlayerVelocityPacket {

    private final double x;
    private final double y;
    private final double z;

    public CUpdatePlayerVelocityPacket(PacketBuffer buf) {
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    public CUpdatePlayerVelocityPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
    }

    public void handle(Supplier<NetworkEvent.Context> sup) {
        NetworkEvent.Context ctx = sup.get();
        ctx.enqueueWork(() -> Minecraft.getInstance().player.setDeltaMovement(x, y, z));
        ctx.setPacketHandled(true);
    }
}
