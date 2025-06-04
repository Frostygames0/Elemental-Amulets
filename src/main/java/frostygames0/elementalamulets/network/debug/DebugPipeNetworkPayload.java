package frostygames0.elementalamulets.network.debug;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record DebugPipeNetworkPayload(
        PipeNetworkInfo networkInfo) implements IDebugInfoPayload<DebugPipeNetworkPayload.PipeNetworkInfo> {
    public static final ResourceLocation ID = ElementalAmulets.id("debug/pipe_network");
    public static final Type<DebugPipeNetworkPayload> TYPE = new Type<>(ID);

    public static final StreamCodec<FriendlyByteBuf, DebugPipeNetworkPayload> STREAM_CODEC =
            CustomPacketPayload.codec(DebugPipeNetworkPayload::write, DebugPipeNetworkPayload::new);

    private DebugPipeNetworkPayload(FriendlyByteBuf buffer) {
        this(new PipeNetworkInfo(buffer.readBoolean(), buffer.readBlockPos(), buffer.readList(BlockPos.STREAM_CODEC)));
    }

    private void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(networkInfo.removal);
        buffer.writeBlockPos(networkInfo.startingPos);
        buffer.writeCollection(networkInfo.targets, BlockPos.STREAM_CODEC);
    }

    @Override
    public PipeNetworkInfo getDebugInfo() {
        return networkInfo;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record PipeNetworkInfo(boolean removal, BlockPos startingPos, List<BlockPos> targets) implements IDebugInfo {
    }
}
