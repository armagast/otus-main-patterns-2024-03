package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;
import me.blokhin.homework004.CommandException;
import me.blokhin.homework004.Fuel;

import java.util.Objects;

@Value
public class CommandCheckFuel implements Command {
    FuelCheckable checkable;

    public CommandCheckFuel(FuelCheckable checkable) {
        Assert.notNull(checkable, "{checkable} must not be null");

        this.checkable = checkable;
    }

    @Override
    public void execute() {
        final Fuel fuel = checkable.getFuel();
        Assert.state(Objects.nonNull(fuel), "{fuel} must not be null");

        final Fuel fuelBurnRate = checkable.getFuelBurnRate();
        Assert.state(Objects.nonNull(fuelBurnRate), "{fuelBurnRate} must not be null");

        if (fuel.lessThan(fuelBurnRate)) {
            throw new CommandException();
        }
    }
}
