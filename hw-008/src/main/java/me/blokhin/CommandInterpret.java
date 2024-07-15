package me.blokhin;

import lombok.Value;

@Value
public class CommandInterpret implements Command {
    String commandId;
    Object[] args;

    public CommandInterpret(final String commandId, final Object[] args) {
        Assert.notNull(commandId, "{commandId} must not be null");
        Assert.notNull(args, "{args} must not be null");

        this.commandId = commandId;
        this.args = args;
    }

    @Override
    public void execute() {
        final Command command = IOC.resolve("Commands.resolve", Varargs.concat(commandId, args));
        IOC.<Command>resolve("queue.add", command).execute();
    }
}
