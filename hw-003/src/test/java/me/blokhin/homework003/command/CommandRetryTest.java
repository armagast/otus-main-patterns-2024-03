package me.blokhin.homework003.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandRetryTest {
    @Test
    @DisplayName("Throws IllegalArgumentException when {command} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandRetry(null));
    }

    @Test
    @DisplayName("Does not intercept exception thrown by inner Command::execute")
    void passesThroughThrownByCommandExecute() {
        final Command innerCommand = mock(Command.class);
        doThrow(new IllegalStateException()).when(innerCommand).execute();

        final CommandRetry command = new CommandRetry(innerCommand);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Executes inner command")
    void executesInnerCommand() {
        final Command innerCommand = mock(Command.class);

        final CommandRetry command = new CommandRetry(innerCommand);
        command.execute();

        verify(innerCommand).execute();
    }
}
