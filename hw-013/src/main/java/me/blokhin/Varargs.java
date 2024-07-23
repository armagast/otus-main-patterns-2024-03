package me.blokhin;

import static java.lang.System.arraycopy;

public class Varargs {
    public static <A> A[] concat(A x0, A[] ys) {
        final @SuppressWarnings("unchecked") A[] as = (A[]) new Object[ys.length + 1];
        as[0] = x0;
        arraycopy(ys, 0, as, 1, ys.length);
        return as;
    }

    public static <A> A[] concat(A x0, A x1, A[] ys) {
        final @SuppressWarnings("unchecked") A[] as = (A[]) new Object[ys.length + 2];
        as[0] = x0;
        as[1] = x1;
        arraycopy(ys, 0, as, 2, ys.length);
        return as;
    }

    public static <A> A[] concat(A x0, A x1, A x2, A[] ys) {
        final @SuppressWarnings("unchecked") A[] as = (A[]) new Object[ys.length + 3];
        as[0] = x0;
        as[1] = x1;
        as[2] = x2;
        arraycopy(ys, 0, as, 3, ys.length);
        return as;
    }

    public static <A> A[] concat(A[] xs, A[] ys) {
        final @SuppressWarnings("unchecked") A[] as = (A[]) new Object[xs.length + ys.length];
        arraycopy(xs, 0, as, 0, xs.length);
        arraycopy(ys, 0, as, xs.length, ys.length);
        return as;
    }
}
