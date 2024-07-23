package me.blokhin;

public interface CommandSupplier {
    Command get(String command, Object... args);
}
