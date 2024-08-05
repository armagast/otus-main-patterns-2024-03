package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueueExecutorTest {
    @DisplayName("Executes commands in queue")
    @Test
    @Timeout(5)
    void runs() {
        final Command command = mock(Command.class);
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        final Queue<Command> queue = new LinkedList<>();

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(command);
        queue.add(executor::kill);

        executor.run();

        verify(command, times(1)).execute();
    }

    @DisplayName("Dispatches exceptions")
    @Test
    @Timeout(5)
    void dispatchesExceptions() {
        final Command command = mock(Command.class);
        final Exception exception = new RuntimeException("Some exception");
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
        final Queue<Command> queue = new LinkedList<>();

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        doNothing().when(exceptionHandler).handle(command, exception);
        doThrow(exception).when(command).execute();

        queue.add(command);
        queue.add(executor::kill);

        executor.run();

        verify(exceptionHandler, times(1)).handle(command, exception);
    }

    @DisplayName("Stops immediately on kill")
    @Test
    @Timeout(5)
    void stopsImmediately() {
        final Command prefixCommand = mock(Command.class);
        final Command suffixCommand = mock(Command.class);
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(prefixCommand);
        queue.add(executor::kill);
        queue.add(suffixCommand);

        executor.run();

        verify(prefixCommand, times(1)).execute();
        verifyNoInteractions(suffixCommand);
        assertFalse(queue.isEmpty());
    }

    @DisplayName("Stops after flushing queue on stop")
    @Test
    @Timeout(5)
    void stopsAfterFlushingQueue() {
        final Command prefixCommand = mock(Command.class);
        final Command suffixCommand = mock(Command.class);
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(prefixCommand);
        queue.add(executor::stop);
        queue.add(suffixCommand);

        executor.run();

        verify(prefixCommand, times(1)).execute();
        verify(suffixCommand, times(1)).execute();
        assertTrue(queue.isEmpty());
    }

    @DisplayName("Calls onEnter callback")
    @Test
    @Timeout(5)
    void callsOnEnter() {
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(executor::kill);

        @SuppressWarnings("unchecked")
        final Consumer<QueueExecutor> onEnterCallback = mock(Consumer.class);

        executor.onEnter(onEnterCallback);

        executor.run();

        verify(onEnterCallback, times(1)).accept(executor);
    }

    @DisplayName("Calls onLeave callback")
    @Test
    @Timeout(5)
    void callsOnLeave() {
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(executor::kill);

        @SuppressWarnings("unchecked")
        final Consumer<QueueExecutor> onLeaveCallback = mock(Consumer.class);

        executor.onLeave(onLeaveCallback);

        executor.run();

        verify(onLeaveCallback, times(1)).accept(executor);
    }
}