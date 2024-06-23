package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class CommandQueueExecutorKillTest {
    @DisplayName("Calls the kill method")
    @Test
    void callsKill() {
        final QueueExecutor executor = mock(QueueExecutor.class);

        new CommandQueueExecutorKill(executor).execute();

        verify(executor, times(1)).kill();
    }
}