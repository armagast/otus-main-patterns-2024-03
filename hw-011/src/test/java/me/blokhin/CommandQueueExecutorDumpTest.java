package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandQueueExecutorDumpTest {
    @DisplayName("Correctly updates executor state")
    @Test
    void updatesState() {
        final @SuppressWarnings("unchecked") Queue<Command> target = mock(Queue.class);

        final QueueExecutor executor = mock(QueueExecutor.class);
        final ArgumentCaptor<QueueExecutorState> executorStateCaptor = ArgumentCaptor.forClass(QueueExecutorState.class);

        final Command command = new CommandQueueExecutorDump(executor, target);

        command.execute();

        verify(executor, times(1)).setState(executorStateCaptor.capture());
        assertEquals(new QueueExecutorStateDump(executor, target), executorStateCaptor.getValue());
    }
}
