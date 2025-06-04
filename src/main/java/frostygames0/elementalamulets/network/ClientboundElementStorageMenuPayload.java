package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundElementStorageMenuPayload(int containerId,
                                                   ElementalComposition stored) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundElementStorageMenuPayload> TYPE =
            new CustomPacketPayload.Type<>(ElementalAmulets.id("send_element_storage_to_client_menu"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundElementStorageMenuPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, ClientboundElementStorageMenuPayload::containerId,
                    ElementalComposition.STREAM_CODEC, ClientboundElementStorageMenuPayload::stored,
                    ClientboundElementStorageMenuPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
