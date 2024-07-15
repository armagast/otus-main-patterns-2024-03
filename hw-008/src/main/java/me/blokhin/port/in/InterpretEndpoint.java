package me.blokhin.port.in;

import me.blokhin.Command;
import me.blokhin.CommandInterpret;
import me.blokhin.IOC;

public class InterpretEndpoint {
    public void interpret(InterpretInput input) {
        IOC.<Command>resolve("IOC.Scopes.use", input.getScopeId()).execute();

        final Command command = new CommandInterpret(input.getCommandId(), input.getArgs());
        IOC.<Command>resolve("queue.add", command);
    }
}
