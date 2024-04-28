package me.blokhin.homework003.exceptionhandler;

import me.blokhin.homework003.command.Command;
import me.blokhin.homework003.command.CommandLogException;
import me.blokhin.homework003.command.Loggable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ExceptionHandlerLogExceptionTest {
    @Test
    @DisplayName("Throws IllegalArgumentException when {queue} is null")
    public void assertsCtorArgs() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new PriorityQueue<>();

        assertThrows(IllegalArgumentException.class, () -> new ExceptionHandlerLogException(null, loggable));
        assertThrows(IllegalArgumentException.class, () -> new ExceptionHandlerLogException(queue, null));
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when {command} or {exception} is null")
    public void assertsHandleArgs() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new PriorityQueue<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerLogException(queue, loggable);

        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        assertThrows(IllegalArgumentException.class, () -> exceptionHandler.handle(null, exception));
        assertThrows(IllegalArgumentException.class, () -> exceptionHandler.handle(command, null));
    }

    @Test
    @DisplayName("Pushes CommandLogException to the queue")
    public void pushesCommandLogExceptionToTheQueue() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new PriorityQueue<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerLogException(queue, loggable);

        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        exceptionHandler.handle(command, exception);

        final Command expected = new CommandLogException(loggable, command, exception);

        assertEquals(1, queue.size());
        assertEquals(expected, queue.poll());
    }
}