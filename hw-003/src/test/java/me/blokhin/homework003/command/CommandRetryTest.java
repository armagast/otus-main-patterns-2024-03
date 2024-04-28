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
        final Command command = mock(Command.class);
        doThrow(new IllegalStateException()).when(command).execute();

        final CommandRetry commandRetry = new CommandRetry(command);

        assertThrows(IllegalStateException.class, commandRetry::execute);
    }

    @Test
    @DisplayName("Executes inner command")
    void executesInnerCommand() {
        final Command command = mock(Command.class);

        final CommandRetry commandRetry = new CommandRetry(command);
        commandRetry.execute();

        verify(command).execute();
    }
}
