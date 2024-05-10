package me.blokhin.homework004.command;

import me.blokhin.homework004.Vector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommandMoveTest {
    final Vector position = mock(Vector.class); // new Vector(12, 5);
    final Vector velocity = mock(Vector.class); // new Vector((-7), 3);

    @Test
    @DisplayName("Throws when movable is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandMove(null));
    }

    @Test
    @DisplayName("Moves movable at (12, 5) with velocity (-7, 3) to (5, 8)")
    void actuallyMoves() {
        final Vector positionNext = mock(Vector.class); // new Vector(5, 8);

        final ArgumentCaptor<Vector> positionNextCaptor = ArgumentCaptor.forClass(Vector.class);

        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();
        doReturn(positionNext).when(position).add(velocity);

        final CommandMove move = new CommandMove(movable);
        move.execute();

        verify(movable).setPosition(positionNextCaptor.capture());
        assertEquals(positionNext, positionNextCaptor.getValue());
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Vector::add")
    void passesThroughThrownByVectorAdd() {
        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();
        doThrow(new IllegalArgumentException()).when(position).add(any());

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalArgumentException.class, move::execute);
    }

    @Test
    @DisplayName("Throws when position is null")
    void throwsWhenPositionIsNull() {
        final Movable movable = mock(Movable.class);
        doReturn(null).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Movable::getPosition")
    void passesThroughThrownByMovableGetPosition() {
        final Movable movable = mock(Movable.class);
        doThrow(new IllegalStateException()).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Throws when velocity is null")
    void throwsWhenVelocityIsNull() {
        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(null).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Movable::getVelocity")
    void passesThroughThrownByMovableGetVelocity() {
        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doThrow(new IllegalStateException()).when(movable).getVelocity();

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Movable::setPosition")
    void passesThroughThrownByMovableSetVelocity() {
        final Movable movable = mock(Movable.class);
        doReturn(position).when(movable).getPosition();
        doReturn(velocity).when(movable).getVelocity();
        doThrow(new IllegalStateException()).when(movable).setPosition(any());

        final CommandMove move = new CommandMove(movable);

        assertThrows(IllegalStateException.class, move::execute);
    }
}
