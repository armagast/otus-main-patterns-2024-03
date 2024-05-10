package me.blokhin.homework003.command;

import lombok.Value;
import me.blokhin.homework003.Assert;

@Value
public class CommandRetry2ndTime implements Command {
    Command command;

    public CommandRetry2ndTime(Command command) {
        Assert.notNull(command, "{command} must not be null");

        this.command = command;
    }

    @Override
    public void execute() {
        command.execute();
    }
}
