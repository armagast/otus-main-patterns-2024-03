package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class QueueExecutorStateDumpTest {
    @DisplayName("canContinue returns true if executor queue is not empty")
    @Test
    void canContinueOnNonEmptyQueue() {
        final @SuppressWarnings("unchecked") Queue<Command> target = mock(Queue.class);

        final QueueExecutor executor = mock(QueueExecutor.class);
        doReturn(false).when(executor).isQueueEmpty();

        final QueueExecutorState state = new QueueExecutorStateDump(executor, target);

        assertTrue(state.canContinue());
    }

    @DisplayName("canContinue returns false if executor queue is empty")
    @Test
    void cannotContinueOnEmptyQueue() {
        final @SuppressWarnings("unchecked") Queue<Command> target = mock(Queue.class);

        final QueueExecutor executor = mock(QueueExecutor.class);
        doReturn(true).when(executor).isQueueEmpty();

        final QueueExecutorState state = new QueueExecutorStateDump(executor, target);

        assertFalse(state.canContinue());
    }

    @DisplayName("Adds command to the target queue")
    @Test
    void addsCommand() {
        final @SuppressWarnings("unchecked") Queue<Command> target = mock(Queue.class);
        final QueueExecutor executor = mock(QueueExecutor.class);

        final Command command = mock(Command.class);

        final QueueExecutorState state = new QueueExecutorStateDump(executor, target);

        state.process(command);

        verify(target, times(1)).add(command);
    }

    @DisplayName("Does not execute command")
    @Test
    void doesNotExecuteCommand() {
        final @SuppressWarnings("unchecked") Queue<Command> target = mock(Queue.class);
        final QueueExecutor executor = mock(QueueExecutor.class);

        final Command command = mock(Command.class);

        final QueueExecutorState state = new QueueExecutorStateDump(executor, target);

        state.process(command);

        verifyNoInteractions(command);
    }

}
