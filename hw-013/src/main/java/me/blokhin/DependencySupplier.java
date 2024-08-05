package me.blokhin;

public interface DependencySupplier {
    Object get(Object... args);
}
