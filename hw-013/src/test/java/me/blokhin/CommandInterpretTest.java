package me.blokhin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommandInterpretTest {
    @DisplayName("Passes command identifier and parameters to supplier")
    @Test
    void passesCommandAndParametersToSupplier() {
        final @SuppressWarnings("unchecked") Consumer<Command> consumer = mock(Consumer.class);

        final String commandIdentifier = "commandIdentifier";
        final Object[] commandArguments = new Object[]{42, "42"};
        final Interpretable interpretable = mock(Interpretable.class);
        doReturn(commandIdentifier).when(interpretable).getCommandId();
        doReturn(commandArguments).when(interpretable).getArguments();

        final CommandSupplier supplier = mock(CommandSupplier.class);

        new CommandInterpret(supplier, consumer, interpretable).execute();

        verify(supplier, times(1)).get(commandIdentifier, commandArguments);
    }

    @DisplayName("Passes supplied command to consumer")
    @Test
    void passesCommandToConsumer() {
        final Command suppliedCommand = mock(Command.class);

        final CommandSupplier supplier = mock(CommandSupplier.class);
        doReturn(suppliedCommand).when(supplier).get(any(), any());

        final @SuppressWarnings("unchecked") Consumer<Command> consumer = mock(Consumer.class);

        final Interpretable interpretable = mock(Interpretable.class);

        new CommandInterpret(supplier, consumer, interpretable).execute();

        verify(consumer, times(1)).accept(suppliedCommand);
    }

}