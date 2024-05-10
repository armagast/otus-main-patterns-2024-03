package me.blokhin.homework004.command;

import me.blokhin.homework004.CommandException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MacroCommandTest {
    @Test
    @DisplayName("Throws when {commands} is null or contains null value")
    void assertsCtorArgs() {
        assertThrows(IllegalArgumentException.class, () -> new MacroCommand(null));
        assertThrows(IllegalArgumentException.class, () -> new MacroCommand(Collections.singletonList(null)));
    }

    @Test
    @DisplayName("Executes commands in order")
    void executesRetainingOrder() {
        final List<Integer> order = new LinkedList<>();

        final Command command1 = () -> order.add(1);
        final Command command2 = () -> order.add(2);
        final Command command3 = () -> order.add(3);

        final Command macro = new MacroCommand(Arrays.asList(command1, command2, command3));

        macro.execute();

        assertEquals(order, Arrays.asList(1, 2, 3));
    }

    @Test
    @DisplayName("Throws CommandException when one of the commands throws and stops execution")
    void throwsCommandException() {
        final Exception exception = new IllegalStateException();
        final Command command1 = mock(Command.class);
        final Command command2 = mock(Command.class);
        final Command command3 = mock(Command.class);

        doThrow(exception).when(command2).execute();

        final Command macro = new MacroCommand(Arrays.asList(command1, command2, command3));

        CommandException caught = assertThrows(CommandException.class, macro::execute);
        assertEquals(caught.getCause(), exception);
        verify(command1).execute();
        verify(command2).execute();
        verifyNoInteractions(command3);
    }
}