package me.blokhin.homework004;

import java.util.Collection;
import java.util.Objects;

public abstract class Assert {
    public static void isTrue(final boolean condition, final String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    public static void noNullElements(final Collection<?> collection, final String message) {
        if (collection != null) {
            for (Object element : collection) {
                if (element == null) {
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    public static void notNull(final Object object, final String message) {
        if (Objects.isNull(object)) throw new IllegalArgumentException(message);
    }

    public static void state(final boolean condition, final String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
