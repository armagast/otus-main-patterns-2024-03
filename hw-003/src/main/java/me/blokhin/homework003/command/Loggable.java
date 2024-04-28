package me.blokhin.homework003.command;

public interface Loggable {
    void log(Command command, Exception exception);
}
