package frostygames0.elementalamulets.pipes;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Queues;
import com.google.common.collect.SetMultimap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import frostygames0.elementalamulets.util.graph.Network;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PipeNetwork extends Network<PipeNetwork, PipeNode, Direction> {
    public static final Codec<PipeNetwork> CODEC = RecordCodecBuilder.create(instance ->
            codec(instance, PipeNode.CODEC, IndexEdgeDataList.codec(Direction.CODEC)).apply(instance, PipeNetwork::new));

    protected PipeNetwork() {}

    public PipeNetwork(List<PipeNode> nodes, IndexEdgeDataList<Direction> edges) {
        super(nodes, edges);
    }

    public PipeNetwork(PipeNode node) {
        super(node);
    }

    @Override
    protected PipeNetwork createEmpty() {
        return new PipeNetwork();
    }

    @Override
    protected void onNodeAdded(PipeNode node) {

    }

    @Override
    protected void onNodeRemoved(PipeNode node) {

    }

    private record NodeToVisit(PipeNode node, Direction direction, int distanceFromStart) {
    }

    public Optional<PipeNode> neighborByDirection(PipeNode node, Direction direction) {
        return neighbors(node).stream().filter(n -> edgeContext(node, n).filter(value -> value == direction).isPresent()).findFirst();
    }

    public void spreadPenis(PipeNode startNode, Direction direction) {
        ensureNodeIsValid(startNode);
        var level = startNode.pipe.getLevel();

        var nodes = new HashSet<PipeNode>();
        var visited = new HashSet<PipeNode>();
        findTest(startNode, direction, visited, nodes);

        for (var node : nodes) {
            level.setBlockAndUpdate(node.getWorldPos().above(), Blocks.COBBLESTONE.defaultBlockState());
        }
    }

    public boolean findTest(PipeNode startNode, Direction traversalDirection, Set<PipeNode> visited, Set<PipeNode> foundNodes) {
        ensureNodeIsValid(startNode);

        var level = startNode.pipe.getLevel();

        visited.add(startNode);

        boolean isAtleastOneSuccess = false;
        for (var d : Direction.values()) {
            if (traversalDirection.getOpposite() == d) {
                continue;
            }

//            if (startNode.pipe.getConnectionType(d) == PipeConnection.Type.DISCONNECTED) {
//                continue;
//            }

            var neighborOptional = neighborByDirection(startNode, d);
            if (neighborOptional.isEmpty()) {
                isAtleastOneSuccess = true;
                continue;
            }

            var neighbor = neighborOptional.get();
            if (visited.contains(neighbor)) {
                continue;
            }

            visited.add(neighbor);
            if (findTest(neighbor, d, visited, foundNodes)) {
                foundNodes.add(neighbor);
                isAtleastOneSuccess = true;
            }
        }

        if (isAtleastOneSuccess) {
            foundNodes.add(startNode);
        }

        return isAtleastOneSuccess;
    }

    public void spreadPressureV2(Level level, PipeNode startNode, Direction spreadDirection, float pressure, int maxDistance) {
        ensureNotDiscarded();
        ensureNodeIsValid(startNode);

        level.setBlockAndUpdate(startNode.getWorldPos().above(), Blocks.GOLD_BLOCK.defaultBlockState());

        var neighborOptional = neighborByDirection(startNode, spreadDirection);

        if (neighborOptional.isEmpty()) {
            return;
        }

        var neighbor = neighborOptional.get();
        var directionFromStart = edgeContext(startNode, neighbor).orElseThrow();

        traverse(neighbor, directionFromStart, (node, direction, distance) -> {
            // Do not traverse in direction of the start
            if (node == startNode) {
                return false;
            }

            if (!node.isLoaded()) {
                return false;
            }

            if (distance >= maxDistance) {
                return false;
            }

            var inwardDirection = direction.getOpposite();
            for (var dir : Direction.values()) {
                node.setPressure(dir, pressure, dir == inwardDirection);
            }

            level.setBlockAndUpdate(node.getWorldPos().above(), Blocks.IRON_BLOCK.defaultBlockState());
            return true;
        });
    }

    public void spreadPressure(Level level, PipeNode startNode, Direction spreadDirection, float pressure, int maxDistance) {
        ensureNotDiscarded();

        level.setBlockAndUpdate(startNode.getWorldPos().above(), Blocks.GOLD_BLOCK.defaultBlockState());

        var firstNeighborOptional = neighborByDirection(startNode, spreadDirection);

        if (firstNeighborOptional.isEmpty()) {
            return;
        }

        var firstNeighbor = firstNeighborOptional.get();

        var seen = new HashSet<PipeNode>();
        var toVisit = Queues.<NodeToVisit>newArrayDeque();

        seen.add(startNode);
        seen.add(firstNeighbor);

        toVisit.add(new NodeToVisit(firstNeighbor, spreadDirection, 0));

        while (!toVisit.isEmpty()) {
            var pair = toVisit.poll();

            var node = pair.node();
            if (!node.isLoaded()) {
                continue;
            }

            var direction = pair.direction();
            var distanceFromStart = pair.distanceFromStart();

            if (distanceFromStart >= maxDistance) {
                continue;
            }

            var inwardDirection = direction.getOpposite();
            for (var dir : Direction.values()) {
                node.setPressure(dir, 16, dir == inwardDirection);
            }

            for (var neighbor : neighbors(node)) {
                if (seen.contains(neighbor)) {
                    continue;
                }

                seen.add(neighbor);
                toVisit.add(new NodeToVisit(neighbor, edgeContext(node, neighbor).orElseThrow(), distanceFromStart + 1));
            }
        }
    }

    public Set<PipeNode> successorsByDirection(PipeNode pipeNode, Direction facingDirection) {
        return neighbors(pipeNode)
                .stream()
                .filter(neighbor -> edgeContext(pipeNode, neighbor).orElseThrow() != facingDirection.getOpposite())
                .collect(Collectors.toUnmodifiableSet());
    }
}
