package me.blokhin.homework004.command;

import me.blokhin.homework004.Angle;
import me.blokhin.homework004.Vector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandMacroRotateTest {
    @Test
    @DisplayName("Throws when {target} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandMacroRotate(null));
    }

    @Test
    @DisplayName("Rotates rotatable and changes its velocity")
    void rotates() {
        final Angle angle = mock(Angle.class);
        final Angle angularVelocity = mock(Angle.class);
        final Vector velocity = mock(Vector.class);
        final MacroRotatable target = mock(MacroRotatable.class);
        doReturn(angle).when(target).getAngle();
        doReturn(angularVelocity).when(target).getAngularVelocity();
        doReturn(velocity).when(target).getVelocity();

        final Angle angleNext = mock(Angle.class);
        doReturn(angleNext).when(angle).add(angularVelocity);

        final Vector velocityNext = mock(Vector.class);
        doReturn(velocityNext).when(velocity).rotate(angularVelocity);

        final Command command = new CommandMacroRotate(target);
        command.execute();

        ArgumentCaptor<Angle> angleCaptor = ArgumentCaptor.forClass(Angle.class);
        verify(target).setAngle(angleCaptor.capture());
        assertEquals(angleNext, angleCaptor.getValue());

        ArgumentCaptor<Vector> velocityCaptor = ArgumentCaptor.forClass(Vector.class);
        verify(target).setVelocity(velocityCaptor.capture());
        assertEquals(velocityNext, velocityCaptor.getValue());
    }
}
