package frostygames0.elementalamulets.util.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import com.google.common.graph.*;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SuppressWarnings("UnstableApiUsage")
public abstract class Network<TNet extends Network<TNet, TNode, TEdgeContext>, TNode extends INetworkNode<TNet, TNode, TEdgeContext>, TEdgeContext> {
    final MutableValueGraph<TNode, TEdgeContext> graph =
            ValueGraphBuilder
                    .directed()
                    .allowsSelfLoops(false)
                    .nodeOrder(ElementOrder.stable())
                    .incidentEdgeOrder(ElementOrder.stable())
                    .build();


    boolean isDiscarded;

    protected Network() {
    }

    protected Network(TNode node) {
        this(List.of(node), List.of());
    }

    protected Network(List<TNode> nodes, List<Edge<TNet, TNode, TEdgeContext>> edges) {
        Preconditions.checkArgument(!nodes.isEmpty(), "Cannot create a network with no nodes.");
        Preconditions.checkArgument(nodes.stream().noneMatch(INetworkNode::isValid), "Some nodes are already in networks.");

        if (nodes.size() == 1) {
            Preconditions.checkArgument(edges.isEmpty(), "A single node cannot have any edges.");

            var node = nodes.getFirst();
            graph.addNode(node);
            node.setNetwork(self());
            onNodeAdded(node);

            return;
        }

        Preconditions.checkArgument(!edges.isEmpty(), "No edges in network with multiple nodes!");
        Preconditions.checkArgument(
                edges.stream().allMatch(e -> nodes.contains(e.source()) && nodes.contains(e.target())),
                "Some edges reference nodes that were not included in the node list!");

        for (var node : nodes) {
            graph.addNode(node);
            node.setNetwork(self());
            onNodeAdded(node);
        }

        for (var edge : edges) {
            var source = edge.source();
            var target = edge.target();
            var context = edge.context();

            graph.putEdgeValue(source, target, context);
            onNodesConnected(source, target, context);
        }
    }

    protected Network(List<TNode> nodes, IndexEdgeDataList<TEdgeContext> edges) {
        this(nodes, edges.list().stream()
                .map(data ->
                        new Edge<>(
                                nodes.get(data.targetIndex()),
                                nodes.get(data.sourceIndex()),
                                data.context()
                        )
                ).toList());
    }

    private void addNodeIfNotInNetwork(TNode node, @Nullable Consumer<TNet> onNetworkDiscarded) {
        if (node.isValid()) {
            tryMergeWith(node.getNetwork(), onNetworkDiscarded);
        } else {
            if (graph.addNode(node)) {
                node.setNetwork(self());
                onNodeAdded(node);
            }
        }
    }

    protected void onNodeAdded(TNode node) {
    }

    public final void connect(TNode node, TNode toConnect, TEdgeContext context, @Nullable Consumer<TNet> onNetworkDiscarded) {
        ensureNotDiscarded();
        ensureNodeIsValid(node);

        Preconditions.checkArgument(node != toConnect, "Cannot connect a node to itself.");

        addNodeIfNotInNetwork(toConnect, onNetworkDiscarded);

        graph.putEdgeValue(node, toConnect, context);
        onNodesConnected(node, toConnect, context);
    }

    protected void onNodesConnected(TNode source, TNode target, TEdgeContext context) {
    }

    public final void remove(TNode node, @Nullable Consumer<TNet> onNetworkCreated, @Nullable Consumer<TNet> onNetworkEmptied) {
        ensureNotDiscarded();
        ensureNodeIsValid(node);

        if (graph.removeNode(node)) {
            node.setNetwork(null);
            if (!graph.nodes().isEmpty()) {
                splitIfNecessary(onNetworkCreated);
            } else if (onNetworkEmptied != null) {
                onNetworkEmptied.accept(self());
            }

            onNodeRemoved(node);
        }
    }

    protected void onNodeRemoved(TNode node) {
    }

    private void tryMergeWith(TNet otherNetwork, @Nullable Consumer<TNet> onNetworkDiscarded) {
        if (otherNetwork == this) {
            return;
        }

        var otherGraph = otherNetwork.graph;
        for (var node : otherGraph.nodes()) {
            graph.addNode(node);
            node.setNetwork(self());
        }

        for (var edge : otherGraph.edges()) {
            graph.putEdgeValue(edge, otherGraph.edgeValue(edge).orElseThrow());
        }

        onMerged(otherNetwork);

        otherNetwork.isDiscarded = true;
        if (onNetworkDiscarded != null) {
            onNetworkDiscarded.accept(otherNetwork);
        }
    }

    protected void onMerged(TNet other) {
    }

    private void splitIfNecessary(@Nullable Consumer<TNet> onNetworkSplit) {
        if (graph.nodes().isEmpty()) {
            return;
        }

        var remaining = new HashSet<>(graph.nodes());
        var seen = new HashSet<TNode>();
        var toVisit = Queues.<TNode>newArrayDeque();

        var firstNode = graph.nodes().iterator().next();

        toVisit.add(firstNode);
        seen.add(firstNode);
        remaining.remove(firstNode);

        while (!toVisit.isEmpty()) {
            var node = toVisit.poll();
            for (var neighborNode : graph.adjacentNodes(node)) {
                if (seen.contains(neighborNode)) {
                    continue;
                }

                seen.add(neighborNode);
                toVisit.add(neighborNode);
                remaining.remove(neighborNode);
            }
        }

        if (remaining.isEmpty()) {
            return;
        }

        var newGraphs = new HashSet<TNet>();
        while (!remaining.isEmpty()) {
            var newNetwork = createEmpty();

            firstNode = remaining.iterator().next();
            toVisit.add(firstNode);
            seen.add(firstNode);
            remaining.remove(firstNode);

            while (!toVisit.isEmpty()) {
                var node = toVisit.poll();
                for (var neighbor : graph.adjacentNodes(node)) {
                    if (!seen.contains(neighbor)) {
                        seen.add(neighbor);
                        toVisit.add(neighbor);
                        remaining.remove(neighbor);
                    }
                }

                // Add node and its edges to the new graph.
                newNetwork.graph.addNode(node);
                graph.incidentEdges(node).forEach(edge ->
                        newNetwork.graph.putEdgeValue(edge, graph.edgeValue(edge).orElseThrow()));

                graph.removeNode(node);
                node.setNetwork(newNetwork);
            }

            newGraphs.add(newNetwork);
            if (onNetworkSplit != null) {
                onNetworkSplit.accept(newNetwork);
            }
        }

        onNetworksSplit(newGraphs);
    }

    protected void onNetworksSplit(Set<TNet> networks) {
    }

    private record VisitationTarget<TNet extends Network<TNet, TNode, TEdgeContext>, TNode extends INetworkNode<TNet, TNode, TEdgeContext>, TEdgeContext>(TNode node, TEdgeContext context, int distance) {}

    public void traverse(TNode startNode, @Nullable TEdgeContext startContext, TriFunction<TNode, @Nullable TEdgeContext, Integer, Boolean> function) {
        ensureNotDiscarded();
        ensureNodeIsValid(startNode);

        var toVisit = new ArrayDeque<VisitationTarget<TNet, TNode, TEdgeContext>>();
        var seen = new HashSet<TNode>();

        seen.add(startNode);
        toVisit.add(new VisitationTarget<>(startNode, startContext, 0));

        while (!toVisit.isEmpty()) {
            var target = toVisit.poll();

            var node = target.node();
            var context = target.context();
            var distance = target.distance();

            if (!function.apply(node, context, distance)) {
                continue;
            }

            for (var adjacentNode : graph.adjacentNodes(node)) {
                if (seen.contains(adjacentNode)) {
                    continue;
                }

                seen.add(adjacentNode);

                var edgeContext = graph.edgeValue(node, adjacentNode).orElseThrow();
                toVisit.add(new VisitationTarget<>(adjacentNode, edgeContext, distance + 1));
            }
        }
    }

    public final int nodeCount() {
        ensureNotDiscarded();
        return graph.nodes().size();
    }

    public final boolean isEmpty() {
        ensureNotDiscarded();
        return graph.nodes().isEmpty();
    }

    public final boolean contains(TNode node) {
        ensureNotDiscarded();
        return graph.nodes().contains(node);
    }

    public final Set<TNode> nodes() {
        ensureNotDiscarded();
        return graph.nodes();
    }

    public final Set<TNode> neighbors(TNode node) {
        ensureNotDiscarded();
        return graph.adjacentNodes(node);
    }

    public final Optional<TEdgeContext> edgeContext(TNode source, TNode target) {
        ensureNotDiscarded();
        return graph.edgeValue(source, target);
    }

    public final Stream<Edge<TNet, TNode, TEdgeContext>> edges() {
        ensureNotDiscarded();
        return graph.edges().stream().map(pair -> Edge.fromEndpointPair(graph, pair));
    }

    private List<TNode> nodesListForSerialization() {
        return List.copyOf(graph.nodes());
    }

    private IndexEdgeDataList<TEdgeContext> edgesDataForSerialization() {
        var nodes = nodesListForSerialization();
        var list = new ArrayList<IndexEdgeData<TEdgeContext>>();

        graph.edges().forEach(edge -> {
            list.add(new IndexEdgeData<>(nodes.indexOf(edge.nodeU()), nodes.indexOf(edge.nodeV()), graph.edgeValue(edge).orElseThrow()));
        });

        return new IndexEdgeDataList<>(list);
    }

    @SuppressWarnings("unchecked")
    protected TNet self() {
        return (TNet) this;
    }

    protected abstract TNet createEmpty();

    protected final void ensureNotDiscarded() {
        Preconditions.checkState(!isDiscarded, "Cannot use a discarded network.");
    }

    protected final void ensureNodeIsValid(TNode node) {
        Preconditions.checkArgument(node.isValid(), "Node is not valid.");
        Preconditions.checkArgument(contains(node), "Node does not belong to this network.");
    }

    @Override
    public String toString() {
        return "Network{" +
                "graph=" + graph +
                ", isDiscarded=" + isDiscarded +
                '}';
    }

    public static <TNet extends Network<TNet, TNode, TEdgeContext>, TNode extends INetworkNode<TNet, TNode, TEdgeContext>, TEdgeContext> Products.P2<RecordCodecBuilder.Mu<TNet>, List<TNode>, IndexEdgeDataList<TEdgeContext>> codec(RecordCodecBuilder.Instance<TNet> instance, Codec<TNode> nodeCodec, Codec<IndexEdgeDataList<TEdgeContext>> edgeDataCodec) {
        return instance.group(
                nodeCodec.listOf().fieldOf("nodes").forGetter(Network::nodesListForSerialization),
                edgeDataCodec.fieldOf("edges").forGetter(Network::edgesDataForSerialization)
        );
    }

    public record Edge<TNet extends Network<TNet, TNode, TEdgeContext>, TNode extends INetworkNode<TNet, TNode, TEdgeContext>, TEdgeContext>(
            TNode source, TNode target, TEdgeContext context) {
        private static <TNet extends Network<TNet, TNode, TEdgeContext>, TNode extends INetworkNode<TNet, TNode, TEdgeContext>, TEdgeContext> Edge<TNet, TNode, TEdgeContext> fromEndpointPair(ValueGraph<TNode, TEdgeContext> graph, EndpointPair<TNode> pair) {
            return new Edge<>(pair.source(), pair.target(), graph.edgeValue(pair).orElseThrow());
        }
    }

    public record IndexEdgeData<TEdgeContext>(int sourceIndex, int targetIndex, TEdgeContext context) {
        public static <TEdgeContext> Codec<IndexEdgeData<TEdgeContext>> codec(Codec<TEdgeContext> contextCodec) {
            return RecordCodecBuilder.create(instance ->
                    instance.group(
                            Codec.INT.fieldOf("sourceIndex").forGetter(IndexEdgeData::sourceIndex),
                            Codec.INT.fieldOf("targetIndex").forGetter(IndexEdgeData::targetIndex),
                            contextCodec.fieldOf("context").forGetter(IndexEdgeData::context)
                    ).apply(instance, IndexEdgeData::new));
        }
    }

    public record IndexEdgeDataList<TEdgeContext>(List<IndexEdgeData<TEdgeContext>> list) {
        public static <TEdgeContext> Codec<IndexEdgeDataList<TEdgeContext>> codec(Codec<TEdgeContext> contextCodec) {
            return IndexEdgeData.codec(contextCodec).listOf().xmap(IndexEdgeDataList::new, IndexEdgeDataList::list);
        }
    }
}
