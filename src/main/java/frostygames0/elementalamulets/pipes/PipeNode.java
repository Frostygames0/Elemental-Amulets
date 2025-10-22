package frostygames0.elementalamulets.pipes;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.util.graph.INetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class PipeNode implements INetworkNode<PipeNetwork, PipeNode, Direction> {
    public static final Codec<PipeNode> CODEC = BlockPos.CODEC.xmap(PipeNode::new, PipeNode::getWorldPos);

    private final BlockPos worldPos;

    @Nullable
    private PipeNetwork network;

    @Nullable
    NewPipeBlockEntity pipe;

    public PipeNode(BlockPos worldPos) {
        this.worldPos = worldPos;
    }

    public void attach(NewPipeBlockEntity pipe) {
        this.pipe = pipe;
    }

    public void detach() {
        pipe = null;
    }

    public void connectInBothDirections(PipeNode other, Direction direction, Consumer<PipeNetwork> onNetworkDiscarded) {
        connectTo(other, direction, onNetworkDiscarded);
        other.connectTo(self(), direction.getOpposite(), onNetworkDiscarded);
    }

    public BlockState getBlockState() {
        ensureValid();
        return pipe.getBlockState();
    }

    public Holder<ElementType> getElement(Direction side) {
        ensureValid();
        return pipe.getFlowElement(side, PipeConnection.RelativeDirection.OUTWARD);
    }

    public float getPressure(Direction side, boolean inward) {
        ensureValid();
        return 0;//pipe.getPressure(side, inward);
    }

    public void addPressure(Direction side, float pressure, boolean inward) {
        ensureValid();
        //pipe.addPressure(side, pressure, inward);
    }

    public void setPressure(Direction side, float pressure, boolean inward) {
        ensureValid();
        //pipe.setPressure(side, pressure, inward);
    }

    public void setNetwork(PipeNetwork network) {
        this.network = network;
    }

    public PipeNetwork getNetwork() {
        return Objects.requireNonNull(network, "The pipe node is invalid!");
    }

    public BlockPos getWorldPos() {
        return worldPos;
    }

    public boolean isValid() {
        return network != null;
    }

    public boolean isAttached() {
        return pipe != null;
    }

    public boolean isLoaded() {
        return isValid() && isAttached() && pipe.hasLevel() && pipe.getLevel().isLoaded(worldPos);
    }

    public boolean isTicking() {
        return isLoaded() && pipe.getLevel().shouldTickBlocksAt(worldPos);
    }

    private void ensureValid() {
        Preconditions.checkState(isValid(), "Pipe node is not connected to a network.");
        Preconditions.checkState(isAttached(), "Pipe node is detached.");
        Preconditions.checkState(pipe.hasLevel(), "Pipe block entity is not attached to a level");
        Preconditions.checkState(pipe.getLevel().isLoaded(worldPos), "Pipe block entity is not loaded in a loaded chunk");
        Preconditions.checkState(isLoaded(), "Pipe node is not loaded - more specific error unavailable.");
    }
}
