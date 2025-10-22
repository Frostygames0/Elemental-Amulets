package frostygames0.elementalamulets.pipes;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Nullable;

enum ConnectionType implements StringRepresentable {
    UNKNOWN("unknown", false),
    CAPABILITY_BLOCK("capability_block", true),
    OTHER_PIPE("pipe", false),
    OPEN("open", true),
    BLOCKED("blocked", false);

    public static final Codec<ConnectionType> CODEC = StringRepresentable.fromValues(ConnectionType::values);

    private final String name;
    private final boolean endpoint;

    ConnectionType(String name, boolean endpoint) {
        this.name = name;
        this.endpoint = endpoint;
    }

    @Nullable
    public FlowSource createFlowSource(BlockPos blockPos, Direction direction) {
        return switch (this) {
            case CAPABILITY_BLOCK -> new FlowSource.BlockCapability(blockPos, direction);
            default -> null;
        };
    }

    public boolean isUnknown() {
        return this == UNKNOWN;
    }

    public boolean isEndpoint() {
        return endpoint;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    interface FlowSourceFactory {
        FlowSourceFactory NONE = (blockPos, direction) -> null;

        @Nullable FlowSource create(BlockPos blockPos, Direction direction);
    }
}
