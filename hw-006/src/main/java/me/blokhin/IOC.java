package me.blokhin;

import java.util.Objects;
import java.util.Optional;

public class IOC {
    private static DependencyResolver resolver = (final String name) -> {
        if (Objects.equals(name, "IOC.updateResolver")) {
            return Optional.of((args) -> (Command) () -> resolver = (DependencyResolver) args[0]);
        }

        return Optional.empty();
    };

    @SuppressWarnings("unchecked")
    public static <T> T resolve(String name, Object... args) {
        return resolver.resolve(name)
                .map(supplier -> (T) supplier.get(args))
                .orElseThrow(() -> new IllegalStateException(String.format("Dependency {%s} not found", name)));
    }
}
