package me.blokhin;

public interface ExceptionHandler {
    void handle(Command command, Exception exception);
}
