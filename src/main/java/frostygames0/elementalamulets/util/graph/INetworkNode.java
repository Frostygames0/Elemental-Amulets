package frostygames0.elementalamulets.util.graph;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public interface INetworkNode<TNet extends Network<TNet, TNode, TEdgeContext>, TNode extends INetworkNode<TNet, TNode, TEdgeContext>, TEdgeContext> {
    boolean isValid();

    @NotNull
    TNet getNetwork();

    void setNetwork(TNet network);

    default void connectTo(TNode toConnect, TEdgeContext context, @Nullable Consumer<TNet> onNetworkDiscarded) {
        getNetwork().connect(self(), toConnect, context, onNetworkDiscarded);
    }

    default void removeItselfFromNetwork(@Nullable Consumer<TNet> onNetworkCreated, @Nullable Consumer<TNet> onNetworkEmptied) {
        getNetwork().remove(self(), onNetworkCreated, onNetworkEmptied);
    }

    default TNode self() {
        return (TNode) this;
    }
}
