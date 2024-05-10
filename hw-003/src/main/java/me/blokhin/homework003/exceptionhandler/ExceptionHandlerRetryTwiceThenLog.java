package me.blokhin.homework003.exceptionhandler;

import me.blokhin.homework003.Assert;
import me.blokhin.homework003.command.*;

import java.util.Queue;

public class ExceptionHandlerRetryTwiceThenLog implements ExceptionHandler {
    private final Queue<Command> queue;
    private final Loggable loggable;

    public ExceptionHandlerRetryTwiceThenLog(Queue<Command> queue, Loggable loggable) {
        Assert.notNull(queue, "{queue} must not be null");
        Assert.notNull(loggable, "{loggable} must not be null");

        this.queue = queue;
        this.loggable = loggable;
    }

    @Override
    public void handle(Command command, Exception exception) {
        Assert.notNull(command, "{command} must not be null");
        Assert.notNull(exception, "{exception} must not be null");

        if (command instanceof CommandRetry2ndTime) {
            handleCommandRetry2ndTime((CommandRetry2ndTime) command, exception);
            return;
        }

        if (command instanceof CommandRetry) {
            handleCommandRetry((CommandRetry) command, exception);
            return;
        }

        queue.add(new CommandRetry(command));
    }

    private void handleCommandRetry2ndTime(CommandRetry2ndTime command, Exception exception) {
        queue.add(new CommandLogException(loggable, command.getCommand(), exception));
    }

    private void handleCommandRetry(CommandRetry command, Exception exception) {
        queue.add(new CommandRetry2ndTime(command.getCommand()));
    }
}
