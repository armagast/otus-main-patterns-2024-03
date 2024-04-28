package me.blokhin.homework004.command;

import me.blokhin.homework004.Angle;
import me.blokhin.homework004.Vector;

public interface VelocityChangeable {
    Angle getAngularVelocity();

    Vector getVelocity();
    void setVelocity(Vector velocity);
}
