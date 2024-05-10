package me.blokhin.homework004.command;

import me.blokhin.homework004.Fuel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandBurnFuelTest {
    @Test
    @DisplayName("Throws when {burnable} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandBurnFuel(null));
    }

    @Test
    @DisplayName("Burns fuel")
    void burnsFuel() {
        final FuelBurnable burnable = mock(FuelBurnable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        final Fuel fuelNext = mock(Fuel.class);
        doReturn(fuel).when(burnable).getFuel();
        doReturn(fuelBurnRate).when(burnable).getFuelBurnRate();
        doReturn(fuelNext).when(fuel).burn(fuelBurnRate);

        CommandBurnFuel command = new CommandBurnFuel(burnable);

        command.execute();

        ArgumentCaptor<Fuel> captor = ArgumentCaptor.forClass(Fuel.class);
        verify(burnable).setFuel(captor.capture());
        assertEquals(fuelNext, captor.getValue());
    }

    @Test
    @DisplayName("Throws IllegalStateException when fuel is null")
    void throwsWhenFuelIsNull() {
        final FuelBurnable burnable = mock(FuelBurnable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        final Fuel fuelNext = mock(Fuel.class);
        doReturn(null).when(burnable).getFuel();
        doReturn(fuelBurnRate).when(burnable).getFuelBurnRate();
        doReturn(fuelNext).when(fuel).burn(fuelBurnRate);

        CommandBurnFuel command = new CommandBurnFuel(burnable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Passes through exception thrown by FuelBurnable::getFuel")
    void passesThroughThrownByFuelBurnableGetFuel() {
        final FuelBurnable burnable = mock(FuelBurnable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        final Fuel fuelNext = mock(Fuel.class);
        doThrow(new IllegalStateException()).when(burnable).getFuel();
        doReturn(fuelBurnRate).when(burnable).getFuelBurnRate();
        doReturn(fuelNext).when(fuel).burn(fuelBurnRate);

        CommandBurnFuel command = new CommandBurnFuel(burnable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Throws IllegalStateException when fuelBurnRate is null")
    void throwsWhenFuelBurnRateIsNull() {
        final FuelBurnable burnable = mock(FuelBurnable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        final Fuel fuelNext = mock(Fuel.class);
        doReturn(fuel).when(burnable).getFuel();
        doReturn(null).when(burnable).getFuelBurnRate();
        doReturn(fuelNext).when(fuel).burn(fuelBurnRate);

        CommandBurnFuel command = new CommandBurnFuel(burnable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Passes through exception thrown by FuelBurnable::getFuelBurnRate")
    void passesThroughThrownByFuelBurnableGetFuelBurnRate() {
        final FuelBurnable burnable = mock(FuelBurnable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        final Fuel fuelNext = mock(Fuel.class);
        doReturn(fuel).when(burnable).getFuel();
        doThrow(new IllegalStateException()).when(burnable).getFuelBurnRate();
        doReturn(fuelNext).when(fuel).burn(fuelBurnRate);

        CommandBurnFuel command = new CommandBurnFuel(burnable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Passes through exception thrown by FuelBurnable::setFuel")
    void passesThroughThrownByFuelBurnableSetFuel() {
        final FuelBurnable burnable = mock(FuelBurnable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        final Fuel fuelNext = mock(Fuel.class);
        doReturn(fuel).when(burnable).getFuel();
        doReturn(fuelBurnRate).when(burnable).getFuelBurnRate();
        doReturn(fuelNext).when(fuel).burn(fuelBurnRate);
        doThrow(new IllegalStateException()).when(burnable).setFuel(any());

        CommandBurnFuel command = new CommandBurnFuel(burnable);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Passes through exception thrown by Fuel::burn")
    void passesThroughThrownByFuelBurn() {
        final FuelBurnable burnable = mock(FuelBurnable.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        doReturn(fuel).when(burnable).getFuel();
        doReturn(fuelBurnRate).when(burnable).getFuelBurnRate();
        doThrow(new IllegalStateException()).when(fuel).burn(fuelBurnRate);

        CommandBurnFuel command = new CommandBurnFuel(burnable);

        assertThrows(IllegalStateException.class, command::execute);
    }
}