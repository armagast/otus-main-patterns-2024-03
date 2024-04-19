package me.blokhin.homework001;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CommandRotateTest {
    @Test
    @DisplayName("Throws when rotatable is null")
    void assertsArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandRotate(null));
    }

    @Test
    @DisplayName("Rotates rotatable with angle 3, divisor 8 and angular velocity 6 to angle 1")
    void rotates() {
        final ArgumentCaptor<Integer> angleNextCaptor = ArgumentCaptor.forClass(Integer.class);

        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(8).when(rotatable).getAngleDivisor();
        doReturn(3).when(rotatable).getAngle();
        doReturn(6).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);
        rotate.execute();

        verify(rotatable).setAngle(angleNextCaptor.capture());
        assertEquals(1, angleNextCaptor.getValue());
    }

    @Test
    @DisplayName("Throws when divisor is non positive")
    void assertsDivisor() {
        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(0).when(rotatable).getAngleDivisor();
        doReturn(3).when(rotatable).getAngle();
        doReturn(6).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);
        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by getAngleDivisor")
    void passesThroughThrownByGetAngleDivisor() {
        final Rotatable rotatable = mock(Rotatable.class);
        doThrow(new IllegalStateException()).when(rotatable).getAngleDivisor();
        doReturn(3).when(rotatable).getAngle();
        doReturn(6).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by getAngle")
    void passesThroughThrownByGetAngle() {
        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(8).when(rotatable).getAngleDivisor();
        doThrow(new IllegalStateException()).when(rotatable).getAngle();
        doReturn(6).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by getAngularVelocity")
    void passesThroughThrownByGetAngularVelocity() {
        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(8).when(rotatable).getAngleDivisor();
        doReturn(3).when(rotatable).getAngle();
        doThrow(new IllegalStateException()).when(rotatable).getAngularVelocity();

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

    @Test
    @DisplayName("Does not intercept exception thrown by setAngle")
    void passesThroughThrownBySetAngle() {
        final Rotatable rotatable = mock(Rotatable.class);
        doReturn(8).when(rotatable).getAngleDivisor();
        doReturn(3).when(rotatable).getAngle();
        doReturn(6).when(rotatable).getAngularVelocity();
        doThrow(new IllegalStateException()).when(rotatable).setAngle(anyInt());

        final CommandRotate rotate = new CommandRotate(rotatable);

        assertThrows(IllegalStateException.class, rotate::execute);
    }

}
