package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Angle;
import me.blokhin.homework004.Assert;

import java.util.Objects;

@Value
public class CommandCheckAngle implements Command {
    HasAngle target;

    public CommandCheckAngle(HasAngle target) {
        Assert.notNull(target, "{target} must not be null");

        this.target = target;
    }

    @Override
    public void execute() {
        final Angle angle = target.getAngle();
        Assert.state(Objects.nonNull(angle), "{angle} must not be null");
    }
}
