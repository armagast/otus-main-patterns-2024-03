package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandQueueExecutorRunTest {
    @DisplayName("Runs executor in new thread")
    @Test
    @Timeout(1)
    void callsRun() throws Exception {
        final AtomicBoolean commandExecuted = new AtomicBoolean(false);

        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        executor.onLeave(ex -> semaphore.release());

        queue.add(() -> commandExecuted.set(true));
        queue.add(executor::stop);

        Command command = new CommandQueueExecutorRun(executor);
        command.execute();

        semaphore.acquire();

        assertTrue(commandExecuted.get());
    }
}
