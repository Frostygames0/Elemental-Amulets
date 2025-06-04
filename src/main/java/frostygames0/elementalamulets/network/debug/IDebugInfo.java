package frostygames0.elementalamulets.network.debug;

import net.minecraft.network.FriendlyByteBuf;

public interface IDebugInfo {
    default void write(FriendlyByteBuf buffer) {
    }
}
