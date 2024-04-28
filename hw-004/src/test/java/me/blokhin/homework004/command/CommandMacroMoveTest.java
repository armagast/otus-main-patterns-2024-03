package me.blokhin.homework004.command;

import me.blokhin.homework004.Fuel;
import me.blokhin.homework004.Vector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandMacroMoveTest {
    @Test
    @DisplayName("Throws when {movable} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandMacroMove(null));
    }

    @Test
    @DisplayName("Moves movable while burning fuel")
    void moves() {
        final Vector position = mock(Vector.class);
        final Vector velocity = mock(Vector.class);
        final Fuel fuel = mock(Fuel.class);
        final Fuel fuelBurnRate = mock(Fuel.class);
        final MacroMovable movable = mock(MacroMovable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();
        doReturn(fuel).when(movable).getFuel();
        doReturn(fuelBurnRate).when(movable).getFuelBurnRate();

        final Vector positionNext = mock(Vector.class);
        doReturn(positionNext).when(position).add(velocity);

        final Fuel fuelNext = mock(Fuel.class);
        doReturn(fuelNext).when(fuel).burn(fuelBurnRate);

        Command command = new CommandMacroMove(movable);
        command.execute();

        ArgumentCaptor<Vector> positionCaptor = ArgumentCaptor.forClass(Vector.class);
        verify(movable).setPosition(positionCaptor.capture());
        assertEquals(positionNext, positionCaptor.getValue());

        ArgumentCaptor<Fuel> fuelCaptor = ArgumentCaptor.forClass(Fuel.class);
        verify(movable).setFuel(fuelCaptor.capture());
        assertEquals(fuelNext, fuelCaptor.getValue());
    }
}