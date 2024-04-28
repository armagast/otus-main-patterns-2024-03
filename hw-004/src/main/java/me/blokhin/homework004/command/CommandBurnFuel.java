package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;
import me.blokhin.homework004.Fuel;

import java.util.Objects;

@Value
public class CommandBurnFuel implements Command {
    FuelBurnable burnable;

    public CommandBurnFuel(final FuelBurnable burnable) {
        Assert.notNull(burnable, "{burnable} must not be null");

        this.burnable = burnable;
    }

    @Override
    public void execute() {
        final Fuel fuel = burnable.getFuel();
        Assert.state(Objects.nonNull(fuel), "{fuel} must not be null");

        final Fuel fuelBurnRate = burnable.getFuelBurnRate();
        Assert.state(Objects.nonNull(fuelBurnRate), "{fuelBurnRate} must not be null");

        final Fuel fuelNext = fuel.burn(fuelBurnRate);

        burnable.setFuel(fuelNext);
    }
}
