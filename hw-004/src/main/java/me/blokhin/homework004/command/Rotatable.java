package me.blokhin.homework004.command;

import me.blokhin.homework004.Angle;

public interface Rotatable {
    Angle getAngle();
    void setAngle(Angle angle);

    Angle getAngularVelocity();
}
