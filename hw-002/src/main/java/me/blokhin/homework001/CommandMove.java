package me.blokhin.homework001;

import java.util.Objects;

public class CommandMove {
    private final Movable movable;

    public CommandMove(Movable movable) {
        Assert.notNull(movable, "{movable} must not be null");

        this.movable = movable;
    }

    public void execute() {
        final Vector position = movable.getPosition();
        Assert.state(Objects.nonNull(position), "{position} must not be null");

        final Vector velocity = movable.getVelocity();
        Assert.state(Objects.nonNull(velocity), "{velocity} must not be null");

        final Vector positionNext = movable.getPosition().add(movable.getVelocity());
        movable.setPosition(positionNext);
    }
}
