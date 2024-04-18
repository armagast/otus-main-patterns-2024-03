package me.blokhin.homework001;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public class CommandMove {
    private final Movable movable;

    void execute() {
        final Vector position = movable.getPosition();
        Assert.state(Objects.nonNull(position), "{position} must not be null");

        final Vector velocity = movable.getVelocity();
        Assert.state(Objects.nonNull(velocity), "{velocity} must not be null");

        final Vector positionNext = movable.getPosition().add(movable.getVelocity());
        movable.setPosition(positionNext);
    }
}
