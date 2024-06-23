package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueueExecutorTest {
    @DisplayName("Executes commands in queue")
    @Test
    @Timeout(1)
    void runs() {
        final AtomicBoolean commandExecuted = new AtomicBoolean(false);

        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(() -> commandExecuted.set(true));
        queue.add(executor::kill);

        executor.run();

        assertTrue(commandExecuted.get());
    }

    @DisplayName("Dispatches exceptions")
    @Test
    @Timeout(1)
    void dispatchesExceptions() {
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        final Command command = mock(Command.class);
        final Exception exception = new RuntimeException("Some exception");

        doNothing().when(exceptionHandler).handle(command, exception);
        doThrow(exception).when(command).execute();

        queue.add(command);
        queue.add(executor::kill);

        executor.run();

        verify(exceptionHandler, times(1)).handle(command, exception);
    }

    @DisplayName("Stops immediately on kill")
    @Test
    @Timeout(1)
    void stopsImmediately() {
        final AtomicBoolean prefixExecuted = new AtomicBoolean(false);
        final AtomicBoolean suffixExecuted = new AtomicBoolean(false);

        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(() -> prefixExecuted.set(true));
        queue.add(executor::kill);
        queue.add(() -> suffixExecuted.set(true));

        executor.run();

        assertTrue(prefixExecuted.get());
        assertFalse(suffixExecuted.get());
        assertFalse(queue.isEmpty());
    }

    @DisplayName("Stops after flushing queue on stop")
    @Test
    @Timeout(1)
    void stopsAfterFlushingQueue() {
        final AtomicBoolean prefixExecuted = new AtomicBoolean(false);
        final AtomicBoolean suffixExecuted = new AtomicBoolean(false);

        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

        final QueueExecutor executor = new QueueExecutor(queue, exceptionHandler);

        queue.add(() -> prefixExecuted.set(true));
        queue.add(executor::stop);
        queue.add(() -> suffixExecuted.set(true));

        executor.run();

        assertTrue(prefixExecuted.get());
        assertTrue(suffixExecuted.get());
        assertTrue(queue.isEmpty());
    }

    @DisplayName("Calls onEnter callback")
    @Test
    @Timeout(1)
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
    @Timeout(1)
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