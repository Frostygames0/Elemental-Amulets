package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record SyncElementStorageWithClientMenu(int containerId,
                                               ElementalComposition stored) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncElementStorageWithClientMenu> TYPE =
            new CustomPacketPayload.Type<>(ElementalAmulets.id("sync_element_storage_with_client_menu"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncElementStorageWithClientMenu> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SyncElementStorageWithClientMenu::containerId,
                    ElementalComposition.STREAM_CODEC, SyncElementStorageWithClientMenu::stored,
                    SyncElementStorageWithClientMenu::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
