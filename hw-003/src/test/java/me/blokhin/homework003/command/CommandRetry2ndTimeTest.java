package me.blokhin.homework003.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandRetry2ndTimeTest {
    @Test
    @DisplayName("Throws IllegalArgumentException when {command} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new CommandRetry2ndTime(null));
    }

    @Test
    @DisplayName("Does not intercept exception thrown by inner Command::execute")
    void passesThroughThrownByCommandExecute() {
        final Command innerCommand = mock(Command.class);
        doThrow(new IllegalStateException()).when(innerCommand).execute();

        final CommandRetry2ndTime command = new CommandRetry2ndTime(innerCommand);

        assertThrows(IllegalStateException.class, command::execute);
    }

    @Test
    @DisplayName("Executes inner command")
    void executesInnerCommand() {
        final Command innerCommand = mock(Command.class);

        final CommandRetry2ndTime command = new CommandRetry2ndTime(innerCommand);
        command.execute();

        verify(innerCommand).execute();
    }
}
