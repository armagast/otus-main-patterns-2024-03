package me.blokhin.homework001;

public abstract class Assert {
    public static void state(final boolean condition, final String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
