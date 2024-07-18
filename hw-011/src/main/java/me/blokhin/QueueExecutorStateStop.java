package me.blokhin;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class QueueExecutorStateStop extends QueueExecutorStateDefault {
    private final QueueExecutor executor;

    public QueueExecutorStateStop(final QueueExecutor executor, final ExceptionHandler exceptionHandler) {
        super(exceptionHandler);

        Assert.notNull(executor, "{executor} cannot be null");

        this.executor = executor;
    }

    @Override
    public boolean canContinue() {
        return !executor.isQueueEmpty();
    }
}
