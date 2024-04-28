package me.blokhin.homework004.command;

import me.blokhin.homework004.Angle;
import me.blokhin.homework004.Assert;

import java.util.Objects;

public class CommandRotate {
    private final Rotatable rotatable;

    public CommandRotate(Rotatable rotatable) {
        Assert.notNull(rotatable, "{rotatable} must not be null");

        this.rotatable = rotatable;
    }

    public void execute() {
        final Angle angle = rotatable.getAngle();
        Assert.state(Objects.nonNull(angle), "{angle} must not be null");

        final Angle angularVelocity = rotatable.getAngularVelocity();
        Assert.state(Objects.nonNull(angularVelocity), "{angularVelocity} must not be null");

        final Angle angleNext = angle.add(angularVelocity);
        rotatable.setAngle(angleNext);
    }
}
