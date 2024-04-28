package me.blokhin.homework003.exceptionhandler;

import me.blokhin.homework003.Assert;
import me.blokhin.homework003.command.Command;
import me.blokhin.homework003.command.CommandLogException;
import me.blokhin.homework003.command.Loggable;

import java.util.Queue;

public class ExceptionHandlerLogException implements ExceptionHandler {
    private final Queue<Command> queue;
    private final Loggable loggable;

    public ExceptionHandlerLogException(Queue<Command> queue, Loggable loggable) {
        Assert.notNull(queue, "{queue} must not be null");
        Assert.notNull(loggable, "{loggable} must not be null");

        this.queue = queue;
        this.loggable = loggable;
    }

    @Override
    public void handle(Command command, Exception exception) {
        Assert.notNull(command, "{command} must not be null");
        Assert.notNull(exception, "{exception} must not be null");

        queue.add(new CommandLogException(loggable, command, exception));
    }
}
