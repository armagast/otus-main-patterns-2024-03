package me.blokhin.homework004.command;

import me.blokhin.homework004.Fuel;

public interface FuelBurnable {
    Fuel getFuel();
    void setFuel(Fuel fuel);

    Fuel getFuelBurnRate();
}
