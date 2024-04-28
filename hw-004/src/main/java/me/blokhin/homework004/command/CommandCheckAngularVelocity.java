package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Angle;
import me.blokhin.homework004.Assert;

import java.util.Objects;

@Value
public class CommandCheckAngularVelocity implements Command {
    HasAngularVelocity target;

    public CommandCheckAngularVelocity(HasAngularVelocity target) {
        Assert.notNull(target, "{target} must not be null");

        this.target = target;
    }

    @Override
    public void execute() {
        final Angle angularVelocity = target.getAngularVelocity();
        Assert.state(Objects.nonNull(angularVelocity), "{angularVelocity} must not be null");
    }
}
