package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueueExecutorStateStopTest {
    @DisplayName("canContinue returns true if executor queue is not empty")
    @Test
    void canContinueOnNonEmptyQueue() {
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = mock(QueueExecutor.class);
        doReturn(false).when(executor).isQueueEmpty();

        final QueueExecutorState state = new QueueExecutorStateStop(executor, exceptionHandler);

        assertTrue(state.canContinue());
    }

    @DisplayName("canContinue returns false if executor queue is empty")
    @Test
    void cannotContinueOnEmptyQueue() {
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = mock(QueueExecutor.class);
        doReturn(true).when(executor).isQueueEmpty();

        final QueueExecutorState state = new QueueExecutorStateStop(executor, exceptionHandler);

        assertFalse(state.canContinue());
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
