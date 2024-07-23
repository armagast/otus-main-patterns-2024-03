package me.blokhin.port.in;

import lombok.Value;
import me.blokhin.Assert;

@Value
public class InterpretInput {
    /** Scope used to interpret command. */
    String scopeId;

    /** Identifier used to resolve command instance. */
    String commandId;

    /** Command arguments. */
    Object[] parameters;

    public InterpretInput(final String scopeId, final String commandId, final Object[] parameters) {
        Assert.notNull(scopeId, "{scopeId} must not be null");
        Assert.notNull(commandId, "{commandId} must not be null");
        Assert.notNull(parameters, "{parameters} must not be null");

        this.scopeId = scopeId;
        this.commandId = commandId;
        this.parameters = parameters;
    }
}
