package me.blokhin;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class CommandIOCRegisterAdapterResolverTest {
    @BeforeAll
    static void setUp() {
        new CommandIOCInitialize().execute();

        final String scopeId = "CommandIOCRegisterAdapterResolverTest";
        ((Command) IOC.resolve("IOC.scopes.new", scopeId)).execute();
        ((Command) IOC.resolve("IOC.scopes.useScope", scopeId)).execute();

        new CommandIOCRegisterAdapterResolver().execute();
    }

    @DisplayName("Returns instance of the interface")
    @Test
    void instanceOfInterface() {
        final UObject target = mock(UObject.class);
        final Object instance = IOC.resolve("Adapter", CommandIOCRegisterAdapterResolveTestInterface.class, target);

        assertInstanceOf(CommandIOCRegisterAdapterResolveTestInterface.class, instance);
    }
}