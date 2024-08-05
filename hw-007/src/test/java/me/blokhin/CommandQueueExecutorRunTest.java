package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import static org.mockito.Mockito.*;

class CommandQueueExecutorRunTest {
    @DisplayName("Runs executor in new thread")
    @Test
    @Timeout(5)
    void callsRun() throws Exception {
        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        final Command command = mock(Command.class);
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        final Queue<Command> queue = new LinkedList<>();

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        executor.onLeave(ex -> semaphore.release());

        queue.add(command);
        queue.add(executor::stop);

        new CommandQueueExecutorRun(executor).execute();

        semaphore.acquire();

        verify(command, times(1)).execute();
    }
}
