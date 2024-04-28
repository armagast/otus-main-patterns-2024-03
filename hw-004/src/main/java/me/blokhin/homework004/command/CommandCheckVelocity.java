package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;
import me.blokhin.homework004.Vector;

import java.util.Objects;

@Value
public class CommandCheckVelocity implements Command {
    HasVelocity target;

    public CommandCheckVelocity(HasVelocity target) {
        Assert.notNull(target, "{target} must not be null");

        this.target = target;
    }

    @Override
    public void execute() {
        final Vector velocity = target.getVelocity();
        Assert.state(Objects.nonNull(velocity), "{velocity} must not be null");
    }
}
