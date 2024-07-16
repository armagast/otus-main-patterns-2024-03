package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommandQueueExecutorStopTest {
    @DisplayName("Correctly updates executor state")
    @Test
    void updatesState() {
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = mock(QueueExecutor.class);
        final ArgumentCaptor<QueueExecutorState> executorStateCaptor = ArgumentCaptor.forClass(QueueExecutorState.class);

        final Command command = new CommandQueueExecutorStop(executor, exceptionHandler);

        command.execute();

        verify(executor, times(1)).setState(executorStateCaptor.capture());
        assertEquals(new QueueExecutorStateStop(executor, exceptionHandler), executorStateCaptor.getValue());
    }
}
