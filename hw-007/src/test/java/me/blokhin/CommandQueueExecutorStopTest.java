package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CommandQueueExecutorStopTest {
    @DisplayName("Calls the stop method")
    @Test
    void callsStop() {
        final QueueExecutor executor = mock(QueueExecutor.class);

        new CommandQueueExecutorStop(executor).execute();

        verify(executor, times(1)).stop();
    }
}