package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ClientboundElementStorageMenuPayload(int containerId,
                                                   ElementalComposition stored) implements CustomPacketPayload {
    public static final ResourceLocation ID = ElementalAmulets.id("element_storage");
    public static final CustomPacketPayload.Type<ClientboundElementStorageMenuPayload> TYPE =
            new CustomPacketPayload.Type<>(ID);

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
