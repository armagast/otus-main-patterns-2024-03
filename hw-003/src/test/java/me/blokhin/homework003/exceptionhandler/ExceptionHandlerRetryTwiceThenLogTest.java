package me.blokhin.homework003.exceptionhandler;

import me.blokhin.homework003.command.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ExceptionHandlerRetryTwiceThenLogTest {
    @Test
    @DisplayName("Throws IllegalArgumentException when {queue} or {loggable} is null")
    void assertsCtorArgs() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new LinkedList<>();

        assertThrows(IllegalArgumentException.class, () -> new ExceptionHandlerRetryTwiceThenLog(null, loggable));
        assertThrows(IllegalArgumentException.class, () -> new ExceptionHandlerRetryTwiceThenLog(queue, null));
    }

    @Test
    @DisplayName("Throws IllegalArgumentException when {command} or {exception} is null")
    void assertsHandleArgs() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerRetryTwiceThenLog(queue, loggable);

        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        assertThrows(IllegalArgumentException.class, () -> exceptionHandler.handle(null, exception));
        assertThrows(IllegalArgumentException.class, () -> exceptionHandler.handle(command, null));
    }

    @Test
    @DisplayName("Adds CommandLogException to the queue when {command} is CommandRetry2ndTime")
    void addsCommandLogExceptionToTheQueue() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerRetryTwiceThenLog(queue, loggable);

        final Command innerCommand = mock(Command.class);
        final CommandRetry2ndTime command = mock(CommandRetry2ndTime.class);
        final Exception exception = mock(Exception.class);

        doReturn(innerCommand).when(command).getCommand();

        exceptionHandler.handle(command, exception);

        final Command expected = new CommandLogException(loggable, command.getCommand(), exception);

        assertEquals(1, queue.size());
        assertEquals(expected, queue.poll());
    }

    @Test
    @DisplayName("Adds CommandRetry2ndTime to the queue when {command} is CommandRetry")
    void addsCommandRetry2ndTimeTheQueue() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerRetryTwiceThenLog(queue, loggable);

        final Command innerCommand = mock(Command.class);
        final CommandRetry command = mock(CommandRetry.class);
        final Exception exception = mock(Exception.class);

        doReturn(innerCommand).when(command).getCommand();

        exceptionHandler.handle(command, exception);

        final Command expected = new CommandRetry2ndTime(command.getCommand());

        assertEquals(1, queue.size());
        assertEquals(expected, queue.poll());
    }

    @Test
    @DisplayName("Adds CommandRetry otherwise")
    void addsCommandRetryToTheQueue() {
        final Loggable loggable = mock(Loggable.class);
        final Queue<Command> queue = new LinkedList<>();
        final ExceptionHandler exceptionHandler = new ExceptionHandlerRetryTwiceThenLog(queue, loggable);

        final Command command = mock(Command.class);
        final Exception exception = mock(Exception.class);

        exceptionHandler.handle(command, exception);

        final Command expected = new CommandRetry(command);

        assertEquals(1, queue.size());
        assertEquals(expected, queue.poll());
    }
}