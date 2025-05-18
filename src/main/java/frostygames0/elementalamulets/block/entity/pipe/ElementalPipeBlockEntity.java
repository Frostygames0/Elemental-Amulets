package frostygames0.elementalamulets.block.entity.pipe;

import frostygames0.elementalamulets.block.pipe.ElementalPipeBlock;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class ElementalPipeBlockEntity extends BlockEntity {
    private static final Direction[] DIRECTIONS = Direction.values();

    public enum TickPhase {
        WAIT_FOR_PUMPS,
        FLIP_FLOWS,
        RUN_NORMALLY
    }

    private Map<Direction, PipeConnection> interfaces;
    private TickPhase phase;

    public ElementalPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.ELEMENTAL_PIPE.get(), pos, blockState);
        phase = TickPhase.WAIT_FOR_PUMPS;
    }

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
                connection.manageSource(level, worldPosition);
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
            Holder<Element> availableFlow = null;
            Holder<Element> collidingFlow = null;

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
                // TODO Implement collision
                return;
            }

            var sendUpdate = false;
            for (PipeConnection connection : connections) {
                Holder<Element> internalElement = singleSource != connection ? availableFlow : null;
                sendUpdate |= connection.manageFlows(level, worldPosition, internalElement);
            }

            if (sendUpdate) {
                markDirtyAndSync();
            }
        }

        for (PipeConnection connection : connections) {
            connection.tickFlow(level, worldPosition);
        }
    }

    public void markDirtyAndSync() {
        setChanged();
        syncData();
    }

    public void syncData() {
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), ElementalPipeBlock.UPDATE_IMMEDIATE);
    }

    public Optional<Holder<Element>> getElement(Direction side, boolean inbound) {
        setupConnections();
        if (!interfaces.containsKey(side)) {
            return Optional.empty();
        }

        return interfaces.get(side).getElement(inbound);
    }

    public PipeConnection getConnection(Direction side) {
        return interfaces.get(side);
    }

    private void setupConnections() {
        if (interfaces != null) {
            return;
        }

        interfaces = new IdentityHashMap<>();
        for (Direction d : DIRECTIONS) {
            if (canHaveFlowToward(getBlockState(), d)) {
                interfaces.put(d, new PipeConnection(d));
            }
        }
    }

    public boolean canHaveFlowToward(BlockState state, Direction direction) {
        return ElementalPipeBlock.isPipe(state) && state.getValue(ElementalPipeBlock.PROPERTY_BY_DIRECTION.get(direction));
    }

    public boolean hasAnyPressure() {
        setupConnections();
        for (PipeConnection pipeConnection : interfaces.values()) {
            if (pipeConnection.hasPressure()) {
                return true;
            }
        }
        return false;
    }

    public void addPressure(Direction side, boolean inbound, float pressure) {
        setupConnections();
        if (!interfaces.containsKey(side)) {
            return;
        }

        interfaces.get(side).addPressure(inbound, pressure);
        markDirtyAndSync();
    }

    public void resetConnections() {
        if (interfaces != null) {
            for (Direction d : DIRECTIONS) {
                if (!canHaveFlowToward(getBlockState(), d)) {
                    interfaces.remove(d);
                } else {
                    interfaces.computeIfAbsent(d, PipeConnection::new);
                }
            }
        }

        phase = TickPhase.WAIT_FOR_PUMPS;

        setupConnections();
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
                interfaces.computeIfAbsent(face, PipeConnection::new);
            }
        }

        // Invalid data (missing/outdated). Defer init to runtime
        if (interfaces.isEmpty()) {
            interfaces = null;
            return;
        }

        interfaces.values().forEach(connection -> connection.deserializeNBT(tag, registries));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        var tag = new CompoundTag();
        setupConnections();
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
        setupConnections();
    }

    public enum ConnectionType {
        NONE,
        NORMAL,
        RIM
    }
}
