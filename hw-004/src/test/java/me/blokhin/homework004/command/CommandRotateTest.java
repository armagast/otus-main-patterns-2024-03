package me.blokhin.homework004.command;

import me.blokhin.homework004.Angle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommandRotateTest {
    @Test
    @DisplayName("Throws when rotatable is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandRotate(null));
    }

    @Test
    @DisplayName("Rotates rotatable")
    void rotates() {
        final Angle angle = mock(Angle.class);
        final Angle angularVelocity = mock(Angle.class);
        final Angle angleNext = mock(Angle.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(angle).when(rotatable).getAngle();
        doReturn(angularVelocity).when(rotatable).getAngularVelocity();
        doReturn(angleNext).when(angle).add(angularVelocity);

        final CommandRotate rotate = new CommandRotate(rotatable);
        rotate.execute();

        ArgumentCaptor<Angle> angleCaptor = ArgumentCaptor.forClass(Angle.class);

        verify(rotatable).setAngle(angleCaptor.capture());
        assertEquals(angleNext, angleCaptor.getValue());
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Angle::add")
    void passesThroughThrownByAngleAdd() {
        final Angle angle = mock(Angle.class);
        final Angle angularVelocity = mock(Angle.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(angle).when(rotatable).getAngle();
        doReturn(angularVelocity).when(rotatable).getAngularVelocity();
        doThrow(new IllegalArgumentException()).when(angle).add(any());

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalArgumentException.class, rotate::execute);
    }

    @Test
    @DisplayName("Throws when angle is null")
    void throwsWhenAngleIsNull() {
        final Angle angularVelocity = mock(Angle.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(null).when(rotatable).getAngle();
        doReturn(angularVelocity).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by getAngle")
    void passesThroughThrownByGetAngle() {
        final Angle angularVelocity = mock(Angle.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doThrow(new IllegalStateException()).when(rotatable).getAngle();
        doReturn(angularVelocity).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Throws when angular velocity is null")
    void throwsWhenAngularVelocityIsNull() {
        final Angle angle = mock(Angle.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(angle).when(rotatable).getAngle();
        doReturn(null).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by getAngularVelocity")
    void passesThroughThrownByGetAngularVelocity() {
        final Angle angle = mock(Angle.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(angle).when(rotatable).getAngle();
        doThrow(new IllegalStateException()).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by setAngle")
    void passesThroughThrownBySetAngle() {
        final Angle angle = mock(Angle.class);
        final Angle angularVelocity = mock(Angle.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(angle).when(rotatable).getAngle();
        doReturn(angularVelocity).when(rotatable).getAngularVelocity();
        doThrow(new IllegalStateException()).when(rotatable).setAngle(any());

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }
}
