package me.blokhin;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class QueueExecutorStateDefault implements QueueExecutorState {
    private final ExceptionHandler exceptionHandler;

    public QueueExecutorStateDefault(final ExceptionHandler exceptionHandler) {
        Assert.notNull(exceptionHandler, "{exceptionHandler} must not be null");

        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public boolean canContinue() {
        return true;
    }

    @Override
    public void process(Command command) {
        try {
            command.execute();
        } catch (Exception exception) {
            exceptionHandler.handle(command, exception);
        }
    }
}
