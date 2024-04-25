package me.blokhin.homework003;

import lombok.Value;

@Value
public class CommandLogException implements Command {
    Loggable loggable;
    Command command;
    Exception exception;

    public CommandLogException(Loggable loggable, Command command, Exception exception) {
        Assert.notNull(loggable, "{loggable} must not be null");
        Assert.notNull(command, "{command} must not be null");
        Assert.notNull(exception, "{exception} must not be null");

        this.loggable = loggable;
        this.command = command;
        this.exception = exception;
    }

    @Override
    public void execute() {
        loggable.log(command, exception);
    }
}
