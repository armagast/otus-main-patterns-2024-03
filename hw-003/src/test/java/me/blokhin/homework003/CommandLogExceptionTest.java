package me.blokhin.homework003;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandLogExceptionTest {
    @Test
    @DisplayName("Throws IllegalArgumentException when {loggable}, {command} or {exception} is null")
    void assertsArgs() {
        final Loggable loggable = mock(Loggable.class);
        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        assertThrows(IllegalArgumentException.class, () -> new CommandLogException(null, command, exception));
        assertThrows(IllegalArgumentException.class, () -> new CommandLogException(loggable, null, exception));
        assertThrows(IllegalArgumentException.class, () -> new CommandLogException(loggable, command, null));
    }

    @Test
    @DisplayName("Logs command and exception")
    void logsCommandAndException() {
        final Loggable loggable = mock(Loggable.class);
        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        final Command commandLogException = new CommandLogException(loggable, command, exception);
        commandLogException.execute();


        final ArgumentCaptor<Command> commandCaptor = ArgumentCaptor.forClass(Command.class);
        final ArgumentCaptor<Exception> exceptionCaptor = ArgumentCaptor.forClass(Exception.class);
        verify(loggable).log(commandCaptor.capture(), exceptionCaptor.capture());
        assertEquals(command, commandCaptor.getValue());
        assertEquals(exception, exceptionCaptor.getValue());
    }

    @Test
    @DisplayName("Does not intercept exception thrown by Loggable::log")
    void passesThroughThrownByLoggableLog() {
        final Loggable loggable = mock(Loggable.class);
        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        doThrow(new IllegalStateException()).when(loggable).log(any(), any());

        final Command commandLogException = new CommandLogException(loggable, command, exception);

        assertThrows(IllegalStateException.class, commandLogException::execute);
    }

}