package me.blokhin.homework003.exceptionhandler;

import me.blokhin.homework003.command.Command;

public interface ExceptionHandler {
    void handle(Command command, Exception exception);
}
