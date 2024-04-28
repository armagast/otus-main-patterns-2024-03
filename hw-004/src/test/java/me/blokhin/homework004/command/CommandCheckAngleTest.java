package me.blokhin.homework004.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandCheckAngleTest {
    @Test
    @DisplayName("Throws when {target} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandCheckAngle(null));
    }

    @Test
    @DisplayName("Throws when angle is null")
    void throwsWhenAngleIsNull() {
        final HasAngle target = mock(HasAngle.class);
        doReturn(null).when(target).getAngle();

        final CommandCheckAngle command = new CommandCheckAngle(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Passes through exception thrown by HasAngle::getAngle")
    void passesThroughThrownByHasAngleGetAngle() {
        final HasAngle target = mock(HasAngle.class);
        doThrow(new IllegalStateException()).when(target).getAngle();

        final CommandCheckAngle command = new CommandCheckAngle(target);

        assertThrows(IllegalStateException.class, command::execute);
    }
}