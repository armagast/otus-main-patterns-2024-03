package me.blokhin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandInterpretTest {
    @BeforeAll
    static void beforeAll() {
        new CommandIOCInitialize().execute();
    }

    @DisplayName("Resolves command and adds it to the queue")
    @Test
    void resolvesCommandAndAddsItToTheQueue() {
        final String scopeId = "resolvesCommandAndAddsItToTheQueue";

        IOC.<Command>resolve("IOC.Scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.Scopes.use", scopeId).execute();

        // Commands.resolve
        final Command commandResolved = mock(Command.class);
        final DependencySupplier commandsResolveSupplier = mock(DependencySupplier.class);
        IOC.<Command>resolve("IOC.register", "Commands.resolve", commandsResolveSupplier).execute();

        final ArgumentCaptor<Object[]> commandsResolveCaptor = ArgumentCaptor.forClass(Object[].class);
        doReturn(commandResolved).when(commandsResolveSupplier).get(commandsResolveCaptor.capture());

        // queue.add
        final Command queueAdd = mock(Command.class);
        final DependencySupplier queueAddSupplier = mock(DependencySupplier.class);
        IOC.<Command>resolve("IOC.register", "queue.add", queueAddSupplier).execute();

        final ArgumentCaptor<Object[]> queueAddCaptor = ArgumentCaptor.forClass(Object[].class);
        doReturn(queueAdd).when(queueAddSupplier).get(queueAddCaptor.capture());

        final String commandId = "SomeCommand";
        final Object[] commandArgs = {42, "42"};

        final Command command = new CommandInterpret(commandId, commandArgs);

        command.execute();

        verify(commandsResolveSupplier, times(1)).get(commandsResolveCaptor.capture());
        assertArrayEquals(Varargs.concat(commandId, commandArgs), commandsResolveCaptor.getValue());

        verify(queueAddSupplier, times(1)).get(queueAddCaptor.capture());
        assertArrayEquals(new Object[] {commandResolved}, queueAddCaptor.getValue());

        verify(queueAdd, times(1)).execute();
    }

    @DisplayName("Passes through exception thrown by {Commands.resolve}")
    @Test
    void passesThroughThrownByCommandsResolve() {
        final String scopeId = "passesThroughThrownByCommandsResolve";

        IOC.<Command>resolve("IOC.Scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.Scopes.use", scopeId).execute();

        // Commands.resolve
        final DependencySupplier commandsResolveSupplier = mock(DependencySupplier.class);
        IOC.<Command>resolve("IOC.register", "Commands.resolve", commandsResolveSupplier).execute();

        final ArgumentCaptor<Object[]> commandsResolveCaptor = ArgumentCaptor.forClass(Object[].class);
        doThrow(new IllegalStateException()).when(commandsResolveSupplier).get(commandsResolveCaptor.capture());

        // queue.add
        final Command queueAdd = mock(Command.class);
        final DependencySupplier queueAddSupplier = mock(DependencySupplier.class);
        IOC.<Command>resolve("IOC.register", "queue.add", queueAddSupplier).execute();

        final ArgumentCaptor<Object[]> queueAddCaptor = ArgumentCaptor.forClass(Object[].class);
        doReturn(queueAdd).when(queueAddSupplier).get(queueAddCaptor.capture());

        final String commandId = "SomeCommand";
        final Object[] commandArgs = {42, "42"};

        final Command command = new CommandInterpret(commandId, commandArgs);

        assertThrows(IllegalStateException.class, command::execute);
    }
}