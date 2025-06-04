package frostygames0.elementalamulets.network.debug;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public interface IDebugInfoPayload<T extends IDebugInfo> extends CustomPacketPayload {
    T getDebugInfo();

    default ResourceLocation getId() {
        return type().id();
    }
}
