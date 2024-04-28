package me.blokhin.homework004.command;

import lombok.Value;
import me.blokhin.homework004.Assert;
import me.blokhin.homework004.CommandException;
import me.blokhin.homework004.Fuel;

import java.util.Objects;

@Value
public class CommandCheckFuel implements Command {
    FuelCheckable checkable;
    Fuel amount;

    public CommandCheckFuel(FuelCheckable checkable, Fuel amount) {
        Assert.notNull(checkable, "{checkable} must not be null");
        Assert.notNull(amount, "{amount} must not be null");

        this.checkable = checkable;
        this.amount = amount;
    }

    @Override
    public void execute() {
        final Fuel current = checkable.getFuel();
        Assert.state(Objects.nonNull(current), "{current} must not be null");

        if (current.lessThan(amount)) {
            throw new CommandException();
        }
    }
}
