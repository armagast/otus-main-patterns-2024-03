package me.blokhin;

import java.util.Optional;

public interface DependencyResolver {
    Optional<DependencySupplier> resolve(String name);
}
