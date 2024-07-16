package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueueExecutorStateDefaultTest {
    @DisplayName("canContinue returns true")
    @Test
    void canContinueAlways() {
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutorState state = new QueueExecutorStateDefault(exceptionHandler);

        assertTrue(state.canContinue());
    }

    @DisplayName("Executes command")
    @Test
    void executesCommand() {
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        final Command command = mock(Command.class);

        final QueueExecutorState state = new QueueExecutorStateDefault(exceptionHandler);

        state.process(command);

        verify(command, times(1)).execute();
    }

    @DisplayName("Handles exception thrown by {command::execute}")
    @Test
    void handlesException() {
        final Exception exception = new IllegalStateException();

        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        final ArgumentCaptor<Command> exceptionHandleCommandCaptor = ArgumentCaptor.forClass(Command.class);
        final ArgumentCaptor<Exception> exceptionHandleExceptionCaptor = ArgumentCaptor.forClass(Exception.class);

        final Command command = mock(Command.class);
        doThrow(exception).when(command).execute();

        final QueueExecutorState state = new QueueExecutorStateDefault(exceptionHandler);

        state.process(command);

        verify(exceptionHandler, times(1)).handle(exceptionHandleCommandCaptor.capture(), exceptionHandleExceptionCaptor.capture());
        assertEquals(command, exceptionHandleCommandCaptor.getValue());
        assertEquals(exception, exceptionHandleExceptionCaptor.getValue());
    }
}
