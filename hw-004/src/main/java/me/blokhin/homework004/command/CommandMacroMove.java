package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;

import java.util.Arrays;

@Value
public class CommandMacroMove implements Command {
    Command internal;

    public CommandMacroMove(final MacroMovable movable) {
        Assert.notNull(movable, "{movable} must not be null");

        internal = new MacroCommand(Arrays.asList(
                new CommandCheckFuel(movable),
                new CommandMove(movable),
                new CommandBurnFuel(movable)
        ));
    }

    @Override
    public void execute() {
        internal.execute();
    }
}
