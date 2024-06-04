package me.blokhin;

import java.util.Objects;

public abstract class Assert {
    public static void isTrue(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    public static <T> T notNull(T object, String message) {
        if (Objects.isNull(object)) throw new IllegalArgumentException(message);
        return object;
    }

    public static void state(final boolean condition, final String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
