package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;
import me.blokhin.homework004.Vector;

import java.util.Objects;

@Value
public class CommandMove {
    Movable movable;

    public CommandMove(Movable movable) {
        Assert.notNull(movable, "{movable} must not be null");

        this.movable = movable;
    }

    public void execute() {
        final Vector position = movable.getPosition();
        Assert.state(Objects.nonNull(position), "{position} must not be null");

        final Vector velocity = movable.getVelocity();
        Assert.state(Objects.nonNull(velocity), "{velocity} must not be null");

        final Vector positionNext = movable.getPosition().add(velocity);
        movable.setPosition(positionNext);
    }
}
