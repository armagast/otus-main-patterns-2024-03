package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Angle;
import me.blokhin.homework004.Assert;
import me.blokhin.homework004.Vector;

import java.util.Objects;

@Value
public class CommandChangeVelocity implements Command {
    VelocityChangeable target;

    public CommandChangeVelocity(VelocityChangeable target) {
        Assert.notNull(target, "{target} must not be null");

        this.target = target;
    }

    @Override
    public void execute() {
        final Angle angularVelocity = target.getAngularVelocity();
        Assert.state(Objects.nonNull(angularVelocity), "{angularVelocity} must not be null");

        final Vector velocity = target.getVelocity();
        Assert.state(Objects.nonNull(velocity), "{velocity} must not be null");

        final Vector velocityNext = velocity.rotate(angularVelocity);

        target.setVelocity(velocityNext);
    }
}
