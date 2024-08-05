package me.blokhin;

import lombok.Value;

import java.util.function.Consumer;

@Value
public class CommandInterpret implements Command {
    CommandSupplier supplier;
    Consumer<Command> consumer;
    Interpretable interpretable;

    public CommandInterpret(final CommandSupplier supplier,
                            final Consumer<Command> consumer,
                            final Interpretable interpretable) {

        Assert.notNull(supplier, "{supplier} must not be null");
        Assert.notNull(consumer, "{consumer} must not be null");
        Assert.notNull(interpretable, "{executable} must not be null");

        this.supplier = supplier;
        this.consumer = consumer;
        this.interpretable = interpretable;
    }

    @Override
    public void execute() {
        consumer.accept(supplier.get(interpretable.getCommandId(), interpretable.getArguments()));
    }
}
