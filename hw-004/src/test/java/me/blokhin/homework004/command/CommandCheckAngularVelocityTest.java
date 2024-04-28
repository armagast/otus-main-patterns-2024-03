package me.blokhin.homework004.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandCheckAngularVelocityTest {
    @Test
    @DisplayName("Throws when {target} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandCheckAngularVelocity(null));
    }

    @Test
    @DisplayName("Throws when angularVelocity is null")
    void throwsWhenAngularVelocityIsNull() {
        final HasAngularVelocity target = mock(HasAngularVelocity.class);
        doReturn(null).when(target).getAngularVelocity();

        final Command command = new CommandCheckAngularVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Passes through exception thrown by HasAngularVelocity::getAngularVelocity")
    void passesThroughThrownByHasAngularVelocityGetAngularVelocity() {
        final HasAngularVelocity target = mock(HasAngularVelocity.class);
        doThrow(new IllegalStateException()).when(target).getAngularVelocity();

        final Command command = new CommandCheckAngularVelocity(target);

        assertThrows(IllegalStateException.class, command::execute);
    }
}