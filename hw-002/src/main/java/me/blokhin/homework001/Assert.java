package me.blokhin.homework001;

import java.util.Objects;

public abstract class Assert {
    public static void notNull(Object object, String message) {
        if (Objects.isNull(object)) throw new IllegalArgumentException();
    }

    public static void state(final boolean condition, final String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
