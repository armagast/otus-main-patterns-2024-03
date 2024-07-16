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
    @Timeout(1)
    void callsRun() throws Exception {
        final Semaphore semaphore = new Semaphore(1);
        semaphore.acquire();

        final Command command = mock(Command.class);
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        final Queue<Command> queue = new LinkedList<>();
        final QueueExecutorState state = new QueueExecutorStateDefault(exceptionHandler);

        final QueueExecutor executor = new QueueExecutor(queue, state);

        executor.onLeave(ex -> semaphore.release());

        queue.add(command);
        queue.add(() -> {
            executor.setState(new QueueExecutorStateKill());
        });

        new CommandQueueExecutorRun(executor).execute();

        semaphore.acquire();

        verify(command, times(1)).execute();
    }
}
