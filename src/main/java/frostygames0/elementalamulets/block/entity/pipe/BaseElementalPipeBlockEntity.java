package frostygames0.elementalamulets.block.entity.pipe;

import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.pipes.PipeNode;
import frostygames0.elementalamulets.util.BlockFace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseElementalPipeBlockEntity extends BlockEntity {
    private static final Direction[] DIRECTIONS = Direction.values();

    public enum TickPhase {
        WAIT_FOR_PUMPS,
        FLIP_FLOWS,
        RUN_NORMALLY
    }

    protected Map<Direction, PipeConnection> interfaces;
    private TickPhase phase;

    @Nullable
    private PipeNode node;

    protected BaseElementalPipeBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(blockEntityType, pos, blockState);
        phase = TickPhase.WAIT_FOR_PUMPS;
    }

    public abstract boolean canHaveFlowToward(Direction side);

    public void tick() {
        if (interfaces == null) {
            return;
        }

        var isOnServer = !level.isClientSide;

        var connections = interfaces.values();
        PipeConnection singleSource = null;

        if (phase == TickPhase.WAIT_FOR_PUMPS) {
            phase = TickPhase.FLIP_FLOWS;
            return;
        }

        if (isOnServer) {
            boolean sendUpdate = false;
            for (PipeConnection connection : connections) {
                sendUpdate = connection.tryFlipFlowsIfPressureReversed();
                connection.manageSource(level);
            }

            if (sendUpdate) {
                markDirtyAndSync();
            }
        }

        if (phase == TickPhase.FLIP_FLOWS) {
            phase = TickPhase.RUN_NORMALLY;
            return;
        }

        if (isOnServer) {
            Holder<ElementType> availableFlow = null;
            Holder<ElementType> collidingFlow = null;

            for (PipeConnection connection : connections) {
                var elementInFlowOptional = connection.getElement(true);
                if (elementInFlowOptional.isEmpty()) {
                    continue;
                }

                var elementInFlow = elementInFlowOptional.get();

                if (availableFlow == null) {
                    singleSource = connection;
                    availableFlow = elementInFlow;
                    continue;
                }
                if (availableFlow.equals(elementInFlow)) {
                    singleSource = null;
                    availableFlow = elementInFlow;
                    continue;
                }
                collidingFlow = elementInFlow;
                break;
            }

            if (collidingFlow != null) {
                Block.dropResources(getBlockState(), level, worldPosition);
                level.removeBlock(worldPosition, false);
                return;
            }

            var sendUpdate = false;
            for (PipeConnection connection : connections) {
                Holder<ElementType> internalElement = singleSource != connection ? availableFlow : null;
                sendUpdate |= connection.manageFlows(level, internalElement);
            }

            if (sendUpdate) {
                markDirtyAndSync();
            }
        }

        for (PipeConnection connection : connections) {
            connection.tickFlow(level);
        }
    }

    public void markDirtyAndSync() {
        setChanged();
        syncData();
    }

    public void syncData() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().blockChanged(worldPosition);
        }
    }

    public Optional<Holder<ElementType>> getElement(Direction side, boolean inbound) {
        setupConnectionsIfNotSetup();
        if (!interfaces.containsKey(side)) {
            return Optional.empty();
        }

        return interfaces.get(side).getElement(inbound);
    }

    public PipeConnection getConnection(Direction side) {
        return interfaces.get(side);
    }

    private void setupConnectionsIfNotSetup() {
        if (interfaces != null) {
            return;
        }

        interfaces = new IdentityHashMap<>();
        for (Direction d : DIRECTIONS) {
            if (canHaveFlowToward(d)) {
                interfaces.put(d, new PipeConnection(new BlockFace(worldPosition, d)));
            }
        }
    }

    public boolean hasAnyPressure() {
        setupConnectionsIfNotSetup();
        for (PipeConnection pipeConnection : interfaces.values()) {
            if (pipeConnection.hasPressure()) {
                return true;
            }
        }
        return false;
    }

    public void addPressure(Direction side, boolean inbound, float pressure) {
        setupConnectionsIfNotSetup();
        if (!interfaces.containsKey(side)) {
            return;
        }

        interfaces.get(side).addPressure(inbound, pressure);
        markDirtyAndSync();
    }

    public void resetConnections() {
        if (interfaces != null) {
            for (Direction d : DIRECTIONS) {
                if (!canHaveFlowToward(d)) {
                    interfaces.remove(d);
                } else {
                    interfaces.computeIfAbsent(d, (d1) -> new PipeConnection(new BlockFace(worldPosition, d1)));
                }
            }
        }

        phase = TickPhase.WAIT_FOR_PUMPS;

        setupConnectionsIfNotSetup();

        interfaces.values().forEach(PipeConnection::resetConnection);
        markDirtyAndSync();
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        if (interfaces == null) {
            return;
        }

        interfaces.values().forEach(connection -> connection.serializeNBT(tag, registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (interfaces == null) {
            interfaces = new IdentityHashMap<>();
        }

        for (Direction face : DIRECTIONS) {
            if (tag.contains(face.getName())) {
                interfaces.computeIfAbsent(face, (d1) -> new PipeConnection(new BlockFace(worldPosition, d1)));
            } else {
                interfaces.remove(face);
            }
        }

        if (interfaces.isEmpty()) {
            interfaces = null;
            return;
        }

        interfaces.values().forEach(connection -> connection.deserializeNBT(tag, worldPosition, registries));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        var tag = super.getUpdateTag(registries);

        setupConnectionsIfNotSetup();
        saveAdditional(tag, registries);

        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setupConnectionsIfNotSetup();
    }

    public enum ConnectionType {
        NONE,
        NORMAL,
        RIM
    }
}
