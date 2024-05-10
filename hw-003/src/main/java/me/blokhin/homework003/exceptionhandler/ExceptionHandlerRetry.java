package me.blokhin.homework003.exceptionhandler;

import me.blokhin.homework003.Assert;
import me.blokhin.homework003.command.Command;
import me.blokhin.homework003.command.CommandLogException;
import me.blokhin.homework003.command.CommandRetry;
import me.blokhin.homework003.command.Loggable;

import java.util.Queue;

public class ExceptionHandlerRetry implements ExceptionHandler {
    private final Queue<Command> queue;

    public ExceptionHandlerRetry(final Queue<Command> queue) {
        Assert.notNull(queue, "{queue} must not be null");

        this.queue = queue;
    }

    @Override
    public void handle(final Command command, final Exception exception) {
        Assert.notNull(command, "{command} must not be null");
        Assert.notNull(exception, "{exception} must not be null");

        queue.add(new CommandRetry(command));
    }
}
