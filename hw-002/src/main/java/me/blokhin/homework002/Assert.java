package me.blokhin.homework002;

import java.util.Objects;

public abstract class Assert {
    public static void isTrue(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    public static void notNull(Object object, String message) {
        if (Objects.isNull(object)) throw new IllegalArgumentException(message);
    }

    public static void state(final boolean condition, final String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
