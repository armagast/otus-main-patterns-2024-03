package me.blokhin.homework004.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandCheckVelocityTest {
    @Test
    @DisplayName("Throws when {target} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandCheckVelocity(null));
    }

    @Test
    @DisplayName("Throws when angle is null")
    void throwsWhenAngleIsNull() {
        final HasVelocity target = mock(HasVelocity.class);
        doReturn(null).when(target).getVelocity();

        final Command command = new CommandCheckVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Passes through exception thrown by HasAngle::getAngle")
    void passesThroughThrownByHasAngleGetAngle() {
        final HasVelocity target = mock(HasVelocity.class);
        doThrow(new IllegalStateException()).when(target).getVelocity();

        final Command command = new CommandCheckVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }
}