package me.blokhin.homework003.exceptionhandler;

import me.blokhin.homework003.command.Command;
import me.blokhin.homework003.command.CommandLogException;
import me.blokhin.homework003.command.CommandRetry;
import me.blokhin.homework003.command.Loggable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ExceptionHandlerRetryTest {
    @Test
    @DisplayName("Throws IllegalArgumentException when {queue} is null")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new ExceptionHandlerRetry(null));
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when {command} or {exception} is null")
    void assertsHandleArgs() {
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerRetry(queue);

        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        assertThrows(IllegalArgumentException.class, () -> exceptionHandler.handle(null, exception));
        assertThrows(IllegalArgumentException.class, () -> exceptionHandler.handle(command, null));
    }

    @Test
    @DisplayName("Adds CommandRetry to the queue")
    void addsCommandRetryToTheQueue() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerRetry(queue);

        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        exceptionHandler.handle(command, exception);

        final Command expected = new CommandRetry(command);

        assertEquals(1, queue.size());
        assertEquals(expected, queue.poll());
    }
}
