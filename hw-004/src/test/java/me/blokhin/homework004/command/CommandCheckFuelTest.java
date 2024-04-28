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
    @DisplayName("Throws when {checkable} or {amount} is null")
    void assertsCtorArgs() {
        FuelCheckable checkable = mock(FuelCheckable.class);
        Fuel amount = mock(Fuel.class);

        assertThrows(IllegalArgumentException.class, () -> new CommandCheckFuel(null, amount));
        assertThrows(IllegalArgumentException.class, () -> new CommandCheckFuel(checkable, null));
    }

    @Test
    @DisplayName("Does not throw when current fuel is greater or equal amount")
    void checkFuelGreaterOrEqualAmount() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel current = mock(Fuel.class);
        doReturn(current).when(checkable).getFuel();

        final Fuel amount = mock(Fuel.class);

        doReturn(false).when(current).lessThan(amount);

        final Command command = new CommandCheckFuel(checkable, amount);

        assertDoesNotThrow(command::execute);
    }

    @Test
    @DisplayName("Throws CommandException when current fuel is less than amount")
    void checkFuelLessThanAmount() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel current = mock(Fuel.class);
        doReturn(current).when(checkable).getFuel();

        final Fuel amount = mock(Fuel.class);

        doReturn(true).when(current).lessThan(amount);

        final Command command = new CommandCheckFuel(checkable, amount);

        assertThrows(CommandException.class, command::execute);
    }

    @Test
    @DisplayName("Throws when fuel is null")
    void throwsThenFuelIsNull() {
        final Fuel amount = mock(Fuel.class);
        final FuelCheckable checkable = mock(FuelCheckable.class);

        doReturn(null).when(checkable).getFuel();

        final Command command = new CommandCheckFuel(checkable, amount);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Fuel::lessThan")
    void passesThroughThrownByFuelLessThan() {
        final FuelCheckable checkable = mock(FuelCheckable.class);
        final Fuel current = mock(Fuel.class);
        doReturn(current).when(checkable).getFuel();

        final Fuel amount = mock(Fuel.class);

        doThrow(new IllegalStateException()).when(current).lessThan(amount);

        final Command command = new CommandCheckFuel(checkable, amount);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
