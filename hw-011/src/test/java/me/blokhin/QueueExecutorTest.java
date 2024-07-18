package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueueExecutorTest {
    @DisplayName("Uses state to decide if execution should continue")
    @Test
    @Timeout(1)
    void usesStateCanContinue() {
        final QueueExecutorState state = mock(QueueExecutorState.class);
        when(state.canContinue())
                .thenReturn(true)
                .thenReturn(false);

        final Queue<Command> queue = new LinkedList<>();

        final QueueExecutor executor = new QueueExecutor(queue, state);

        executor.run();

        verify(state, times(2)).canContinue();
    }

    @DisplayName("Uses state to process command")
    @Test
    @Timeout(1)
    void usesStateProcess() {
        final Command command1 = mock(Command.class);
        final Command command2 = mock(Command.class);
        final Command command3 = mock(Command.class);

        final Queue<Command> queue = new LinkedList<>();
        queue.add(command1);
        queue.add(command2);
        queue.add(command3);

        final ArgumentCaptor<Command> stateProcessCaptor = ArgumentCaptor.forClass(Command.class);
        final QueueExecutorState state = mock(QueueExecutorState.class);
        when(state.canContinue()).then((Answer<Boolean>) (invocation) -> !queue.isEmpty());

        final QueueExecutor executor = new QueueExecutor(queue, state);

        executor.run();

        verify(state, times(3)).process(stateProcessCaptor.capture());
        assertEquals(Arrays.asList(command1, command2, command3), stateProcessCaptor.getAllValues());
    }

    @DisplayName("Updates state")
    @Test
    @Timeout(1)
    void updatesState() {
        final QueueExecutorState state = mock(QueueExecutorState.class);
        when(state.canContinue()).thenReturn(true);

        final QueueExecutorState stateNext = mock(QueueExecutorState.class);
        when(stateNext.canContinue()).thenReturn(false);

        final Queue<Command> queue = new LinkedList<>();

        final QueueExecutor executor = new QueueExecutor(queue, state);

        executor.setState(stateNext);
        executor.run();

        verify(state, times(0)).canContinue();
        verify(stateNext, times(1)).canContinue();
    }

    @DisplayName("Calls onEnter callback")
    @Test
    @Timeout(1)
    void callsOnEnter() {
        final QueueExecutorState state = mock(QueueExecutorState.class);
        when(state.canContinue()).thenReturn(false);

        final Queue<Command> queue = new LinkedList<>();

        final QueueExecutor executor = new QueueExecutor(queue, state);

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
        final QueueExecutorState state = mock(QueueExecutorState.class);
        when(state.canContinue()).thenReturn(false);

        final Queue<Command> queue = new LinkedList<>();

        final QueueExecutor executor = new QueueExecutor(queue, state);

        @SuppressWarnings("unchecked")
        final Consumer<QueueExecutor> onLeaveCallback = mock(Consumer.class);

        executor.onLeave(onLeaveCallback);

        executor.run();

        verify(onLeaveCallback, times(1)).accept(executor);
    }
}
