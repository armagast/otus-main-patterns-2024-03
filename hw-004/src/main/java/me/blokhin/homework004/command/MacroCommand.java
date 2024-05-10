package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;
import me.blokhin.homework004.CommandException;

import java.util.List;

@Value
public class MacroCommand implements Command {
    List<Command> commands;

    public MacroCommand(final List<Command> commands) {
        Assert.notNull(commands, "{commands} must not be null");
        Assert.noNullElements(commands, "{commands} must not contain null elements");

        this.commands = commands;
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            try {
                command.execute();
            } catch (Exception cause) {
                throw new CommandException(cause);
            }
        }
    }
}
