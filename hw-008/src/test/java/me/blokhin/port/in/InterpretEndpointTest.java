package me.blokhin.port.in;

import me.blokhin.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InterpretEndpointTest {
    @BeforeAll
    static void beforeAll() {
        new CommandIOCInitialize().execute();
    }

    @DisplayName("Creates instance of CommandInterpret and adds it to the queue")
    @Test
    void addsCommandToTheQueue() {
        final String scopeId = "addsCommandToTheQueue";
        final String commandId = "SomeCommand";
        final Object[] commandArgs = {42, "42"};

        final Command queueAddCommand = mock(Command.class);
        final DependencySupplier queueAddSupplier = mock(DependencySupplier.class);
        final ArgumentCaptor<Object[]> queueAddCaptor = ArgumentCaptor.forClass(Object[].class);
        doReturn(queueAddCommand).when(queueAddSupplier).get(queueAddCaptor.capture());

        IOC.<Command>resolve("IOC.Scopes.new", scopeId).execute();
        IOC.<Command>resolve("IOC.Scopes.use", scopeId).execute();
        IOC.<Command>resolve("IOC.register", "queue.add", queueAddSupplier).execute();
        IOC.<Command>resolve("IOC.Scopes.use", "root").execute();

        final InterpretEndpoint endpoint = new InterpretEndpoint();

        endpoint.interpret(new InterpretInput(scopeId, commandId, commandArgs));

        final Command commandExpected = new CommandInterpret(commandId, commandArgs);

        verify(queueAddSupplier, times(1)).get(queueAddCaptor.capture());
        assertEquals(commandExpected, queueAddCaptor.getValue()[0]);
    }
}
