package me.blokhin.homework001;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommandMoveTest {
    final Vector position = new Vector(12, 5);
    final Vector velocity = new Vector(-7, 3);

    @Test
    @DisplayName("Moves movable at (12, 5) with velocity (-7, 3) to (5, 8)")
    void test001() {
        final Vector positionNext = new Vector(5, 8);

        final ArgumentCaptor<Vector> positionNextCaptor = ArgumentCaptor.forClass(Vector.class);

        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);
        move.execute();

        verify(movable).setPosition(positionNextCaptor.capture());
        assertEquals(positionNext, positionNextCaptor.getValue());
    }

    @Test
    @DisplayName("Throws when position is null")
    void test002() {
        final Movable movable = mock(Movable.class);
        doReturn(null).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by getPosition")
    void test004() {
        final Movable movable = mock(Movable.class);
        doThrow(new IllegalStateException()).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Throws when velocity is null")
    void test003() {
        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(null).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by getVelocity")
    void test005() {
        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doThrow(new IllegalStateException()).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by setPosition")
    void test006() {
        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();
        doThrow(new IllegalStateException()).when(movable).setPosition(any());

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }
}
