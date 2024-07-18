package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueueExecutorStateKillTest {
    @DisplayName("canContinue returns false")
    @Test
    void canContinueAlways() {
        final QueueExecutorState state = new QueueExecutorStateKill();

        assertFalse(state.canContinue());
    }

    @DisplayName("Does not execute command")
    @Test
    void doesNotExecuteCommand() {
        final Command command = mock(Command.class);

        final QueueExecutorState state = new QueueExecutorStateKill();

        state.process(command);

        verifyNoInteractions(command);
    }
}
