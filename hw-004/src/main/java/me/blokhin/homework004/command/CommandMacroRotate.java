package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;

import java.util.Arrays;

@Value
public class CommandMacroRotate implements Command {
    Command internal;

    public CommandMacroRotate(MacroRotatable target) {
        Assert.notNull(target, "{target} must not be null");

        this.internal = new MacroCommand(Arrays.asList(
                new CommandRotate(target),
                new CommandChangeVelocity(target)
        ));
    }

    @Override
    public void execute() {
        internal.execute();
    }
}
