package frostygames0.elementalamulets.util;

public class MutableTriple<A, B, C> {
    private A first;
    private B second;
    private C third;

    public MutableTriple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <A, B, C> MutableTriple<A, B, C> of(A first, B second, C third) {
        return new MutableTriple<>(first, second, third);
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

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }
}
