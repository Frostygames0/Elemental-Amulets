package frostygames0.elementalamulets.pipes;

import com.mojang.logging.LogUtils;
import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.initialization.ModBlockEntities;
import frostygames0.elementalamulets.initialization.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

public class NewPipeBlockEntity extends BlockEntity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Direction[] DIRECTIONS = Direction.values();

    private final Map<Direction, PipeConnection> connectionsMap = new EnumMap<>(Direction.class);

    @Nullable
    private PipeNode node;

    private TickPhase tickPhase;

    public enum TickPhase {
        WAIT_FOR_PUMPS,
        FLIP_FLOWS,
        RUN_NORMALLY
    }

    public NewPipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.NEW_PIPE.get(), pos, blockState);
    }

    public static void serverTick(ServerLevel level, BlockPos pos, BlockState state, NewPipeBlockEntity blockEntity) {
        var connectionsMap = blockEntity.connectionsMap;
        if (connectionsMap.isEmpty()) {
            return;
        }

        var connections = connectionsMap.values();
        var phase = blockEntity.tickPhase;

        if (blockEntity.tickPhase == TickPhase.WAIT_FOR_PUMPS) {
            blockEntity.tickPhase = TickPhase.FLIP_FLOWS;
            return;
        }

        PipeConnection singleSource = null;
        Holder<ElementType> elementInInwardPipes = null;
        Holder<ElementType> collidingElement = null;

        for (var connection : connections) {
            if (connection.getFlowDirection() != PipeConnection.RelativeDirection.INWARD) {
                continue;
            }

            var element = connection.getFlowElement();
            if (elementInInwardPipes != null && element != elementInInwardPipes) {
                collidingElement = element;
                // COLLISION
                break;
            }

            if (elementInInwardPipes == null) {
                singleSource = connection;
            }

            elementInInwardPipes = connection.getFlowElement();
        }

        tickConnectionFlows(level, blockEntity);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, NewPipeBlockEntity blockEntity) {
        tickConnectionFlows(level, blockEntity);
    }

    private static void tickConnectionFlows(Level level, NewPipeBlockEntity blockEntity) {
        var connections = blockEntity.connectionsMap.values();

        for (var connection : connections) {
            connection.tickFlow(level);
        }
    }

    private void setNode(PipeNode node) {
        this.node = node;
        this.node.attach(this);
    }

    private void retrieveNodeOrCreateNetwork() {
        if (level instanceof ServerLevel serverLevel) {
            PipeNode node;

            node = PipeNetworks.retrieveUnloadedNode(serverLevel, worldPosition);
            if (node == null) {
                node = PipeNetworks.startNewNetwork(serverLevel, worldPosition);
            }

            setNode(node);
        }
    }

    private void unloadNode() {
        if (level instanceof ServerLevel serverLevel) {
            if (node != null) {
                PipeNetworks.unloadNode(serverLevel, node);
                node = null;
            }
        }
    }

    public void onPipeRemoved() {
        if (node != null && level instanceof ServerLevel serverLevel) {
            node.removeItselfFromNetwork(
                    network -> PipeNetworks.onNetworkCreated(serverLevel, network),
                    network -> PipeNetworks.onNetworkDiscarded(serverLevel, network)
            );
        }
    }

    private boolean isNodeNotValidOrNull() {
        return node == null || !node.isValid();
    }

    public void tick() {
        if (connectionsMap.isEmpty()) {
            return;
        }

        if (level.isClientSide) {
            return;
        }

        var connections = connectionsMap.values();

        PipeConnection singleSource = null;
        Holder<ElementType> elementInInwardPipes = null;
        Holder<ElementType> collidingElement = null;

        for (var connection : connections) {
            if (connection.getFlowDirection() != PipeConnection.RelativeDirection.INWARD) {
                continue;
            }

            var element = connection.getFlowElement();
            if (elementInInwardPipes != null && element != elementInInwardPipes) {
                collidingElement = element;
                // COLLISION
                break;
            }

            if (elementInInwardPipes == null) {
                singleSource = connection;
            }

            elementInInwardPipes = connection.getFlowElement();
        }

        for (var connection : connections) {
            // TICK CONNECTION WITH elementInInwardPipes
        }
    }

    private void tickFlippingFlow() {
        if (!level.isClientSide()) {
            boolean sendUpdate = false;

            for (PipeConnection connection : connectionsMap.values()) {
                sendUpdate = connection.tryFlipFlowsIfPressureReversed();
                connection.updateFlowSource(level, this);
            }

            if (sendUpdate) {
                //markDirtyAndSync();
            }
        }
    }

    public Holder<ElementType> getFlowElement(Direction direction, PipeConnection.RelativeDirection relativeDirection) {
        if (!connectionsMap.containsKey(direction)) {
            return null;
        }

        var connection = connectionsMap.get(direction);
        if (connection.getFlowDirection() != relativeDirection) {
            return null;
        }

        return connection.getFlowElement();
    }

    protected boolean canFlowTowards(Direction side) {
        return NewPipeBlock.isConnected(getBlockState(), side);
    }

    public void updateConnections() {
        for (var side : DIRECTIONS) {
            if (canFlowTowards(side)) {
                createConnection(side);
            } else {
                removeConnection(side);
            }
        }

        setChanged();
    }

    private void createConnection(Direction side) {
        var connection = connectionsMap.computeIfAbsent(side, PipeConnection::new);

        var relativeBlockPos = worldPosition.relative(side);
        var relativeBlockEntity = level.getBlockEntity(relativeBlockPos);

        var oppositeSide = side.getOpposite();

        if (relativeBlockEntity instanceof NewPipeBlockEntity anotherPipe) {
            if (level instanceof ServerLevel serverLevel) {
                var anotherNode = anotherPipe.node;

                if (isNodeNotValidOrNull() || anotherPipe.isNodeNotValidOrNull()) {
                    throw new IllegalStateException(String.format("Pipe's or its neighbor's nodes are invalid or pipes are not attached to the network! [%s]", side));
                }

                var network = node.getNetwork();
                network.connect(node, anotherNode, side, n -> PipeNetworks.onNetworkDiscarded(serverLevel, n));
                network.connect(anotherNode, node, oppositeSide, n -> PipeNetworks.onNetworkDiscarded(serverLevel, n));
            }

            connection.setConnectionType(ConnectionType.OTHER_PIPE);

            LOGGER.debug("[{}, {}] Connection is a pipe, connected networks and set the flow source!", worldPosition.toShortString(), side);
            return;
        }

        // If it's block with element storage
        var neighborCap = level.getCapability(ModCapabilities.ELEMENT_STORAGE_BLOCK, relativeBlockPos, oppositeSide);
        if (neighborCap != null) {
            connection.setConnectionType(ConnectionType.CAPABILITY_BLOCK);
            LOGGER.debug("[{}, {}] Connection is a block with Element Storage CAP", worldPosition.toShortString(), side);
            return;
        }

        connection.setConnectionType(isBlockFaceOpen(level, relativeBlockPos, side) ? ConnectionType.OPEN : ConnectionType.BLOCKED);
        LOGGER.debug("[{}, {}] Connection is either open block or blocked!", worldPosition.toShortString(), side);
    }

    private void removeConnection(Direction side) {
        var connection = connectionsMap.get(side);
        if (connection == null) {
            return;
        }

        connectionsMap.remove(side);
    }

    public static boolean isBlockFaceOpen(Level level, BlockPos pos, Direction side) {
        var connectedState = level.getBlockState(pos);

        if (Block.isFaceFull(connectedState.getCollisionShape(level, pos), side)) {
            return false;
        }

       return connectedState.canBeReplaced() && connectedState.getDestroySpeed(level, pos) != -1 || connectedState.hasProperty(BlockStateProperties.WATERLOGGED);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        if (!connectionsMap.isEmpty()) {
            var connectionsTag = new CompoundTag();
            tag.put("Connections", connectionsTag);

            var registryOps = registries.createSerializationContext(NbtOps.INSTANCE);

            for (var entry : connectionsMap.entrySet()) {
                var direction = entry.getKey();
                var connection = entry.getValue();

                connection.codec()
                        .encodeStart(registryOps, connection)
                        .resultOrPartial()
                        .ifPresent(tag1 -> connectionsTag.put(direction.getName(), tag1));
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        var connectionsTag = tag.getCompound("Connections");
        if (!connectionsTag.isEmpty()) {
            var registryOps = registries.createSerializationContext(NbtOps.INSTANCE);

            for (var direction : DIRECTIONS) {
                var connectionTag = connectionsTag.get(direction.getName());
                if (connectionTag != null) {
                    connectionsMap.getOrDefault(direction, new PipeConnection(direction))
                            .codec()
                            .parse(registryOps, connectionTag)
                            .resultOrPartial()
                            .ifPresent(connection -> connectionsMap.replace(direction, connection));
                }
            }
        }
    }

    @Override
    public void onChunkUnloaded() {
        unloadNode();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        LOGGER.debug("HELP IM BEING REMOVED!");
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        retrieveNodeOrCreateNetwork();
    }
}
