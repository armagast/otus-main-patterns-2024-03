package me.blokhin.homework003.command;

import lombok.Value;
import me.blokhin.homework003.Assert;

@Value
public class CommandRetry implements Command {
    Command command;

    public CommandRetry(final Command command) {
        Assert.notNull(command, "{command} must not be null");

        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}
