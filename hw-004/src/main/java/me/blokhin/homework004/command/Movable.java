package me.blokhin.homework004.command;

import me.blokhin.homework004.Vector;

public interface Movable {
    Vector getPosition();
    void setPosition(Vector position);

    Vector getVelocity();
}
