package me.blokhin.port.in;

import me.blokhin.*;

import java.util.function.Consumer;

public class InterpretEndpoint {
    public void interpret(final UserContext userContext, final InterpretInput input) {
        Assert.notNull(userContext, "{userContext} must not be null");
        Assert.notNull(input, "{input} must not be null");

        final CommandSupplier supplier = (final String command, final Object[] arguments) -> {
            IOC.<Command>resolve("IOC.Scopes.use", input.getScopeId()).execute();

            final String userScopeId = IOC.resolve("Scopes.getByUserId", userContext.getUserId());
            IOC.<Command>resolve("IOC.Scopes.use", userScopeId).execute();

            return IOC.resolve("Commands.resolve", Varargs.concat(command, arguments));
        };

        final Consumer<Command> consumer = command -> {
            IOC.<Command>resolve("IOC.Scopes.use", input.getScopeId()).execute();
            IOC.<Command>resolve("queue.add", command).execute();
        };

        final Command command = new CommandInterpret(
                supplier,
                consumer,
                new Interpretable() {
                    @Override
                    public String getCommandId() {
                        return input.getCommandId();
                    }

                    @Override
                    public Object[] getArguments() {
                        return input.getParameters();
                    }
                }
        );

        command.execute();
    }
}
