package me.blokhin.homework004.command;

import me.blokhin.homework004.CommandException;
import me.blokhin.homework004.Fuel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommandCheckFuelTest {
    @Test
    @DisplayName("Throws when {checkable} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandCheckFuel(null));
    }

    @Test
    @DisplayName("Does not throw when current fuel is greater or equal fuelBurnRate")
    void checkFuelGreaterOrEqualAmount() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        doReturn(fuel).when(checkable).getFuel();
        doReturn(fuelBurnRate).when(checkable).getFuelBurnRate();

        doReturn(false).when(fuel).lessThan(fuelBurnRate);

        final Command command = new CommandCheckFuel(checkable);

        assertDoesNotThrow(command::execute);
    }

    @Test
    @DisplayName("Throws CommandException when current fuel is less than fuelBurnRate")
    void checkFuelLessThanAmount() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        doReturn(fuel).when(checkable).getFuel();
        doReturn(fuelBurnRate).when(checkable).getFuelBurnRate();

        doReturn(true).when(fuel).lessThan(fuelBurnRate);

        final Command command = new CommandCheckFuel(checkable);

        assertThrows(CommandException.class, command::execute);
    }

    @Test
    @DisplayName("Throws when fuel is null")
    void throwsWhenFuelIsNull() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        doReturn(fuelBurnRate).when(checkable).getFuelBurnRate();

        doReturn(null).when(checkable).getFuel();

        final Command command = new CommandCheckFuel(checkable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by FuelCheckable::getFuel")
    void passesThroughThrownByFuelCheckableGetFuel() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        doReturn(fuelBurnRate).when(checkable).getFuelBurnRate();

        doThrow(new IllegalStateException()).when(checkable).getFuel();

        final Command command = new CommandCheckFuel(checkable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Throws when fuelBurnRate is null")
    void throwsWhenFuelBurnRateIsNull() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel fuel = mock(Fuel.class);
        doReturn(fuel).when(checkable).getFuel();

        doReturn(null).when(checkable).getFuelBurnRate();

        final Command command = new CommandCheckFuel(checkable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by FuelCheckable::getFuelBurnRate")
    void passesThroughThrownByFuelCheckableGetFuelBurnRate() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel fuel = mock(Fuel.class);
        doReturn(fuel).when(checkable).getFuel();

        doThrow(new IllegalStateException()).when(checkable).getFuelBurnRate();

        final Command command = new CommandCheckFuel(checkable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Fuel::lessThan")
    void passesThroughThrownByFuelLessThan() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        doReturn(fuel).when(checkable).getFuel();

        doThrow(new IllegalStateException()).when(fuel).lessThan(fuelBurnRate);

        final Command command = new CommandCheckFuel(checkable);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
