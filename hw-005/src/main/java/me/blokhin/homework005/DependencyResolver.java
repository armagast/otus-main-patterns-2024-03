package me.blokhin.homework005;

import java.util.Optional;

public interface DependencyResolver {
    Optional<DependencySupplier> resolve(String name);
}
