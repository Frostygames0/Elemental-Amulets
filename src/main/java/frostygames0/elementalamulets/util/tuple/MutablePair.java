package frostygames0.elementalamulets.util.tuple;

import java.util.Objects;

public class MutablePair<A, B> {
    private A first;
    private B second;

    public MutablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> MutablePair<A, B> of(A first, B second) {
        return new MutablePair<>(first, second);
    }

    public static <A, B> MutablePair<A, B> fromMojPair(com.mojang.datafixers.util.Pair<A, B> mojPair) {
        return of(mojPair.getFirst(), mojPair.getSecond());
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public MutablePair<B, A> swap() {
        return of(second, first);
    }

    public com.mojang.datafixers.util.Pair<A, B> toMojPair() {
        return com.mojang.datafixers.util.Pair.of(first, second);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof MutablePair<?, ?> other)) {
            return false;
        }

        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
