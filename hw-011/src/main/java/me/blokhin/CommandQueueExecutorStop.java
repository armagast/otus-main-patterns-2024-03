package me.blokhin;

import lombok.Value;

@Value
public class CommandQueueExecutorStop implements Command {
    QueueExecutor executor;
    ExceptionHandler exceptionHandler;

    public CommandQueueExecutorStop(final QueueExecutor executor, final ExceptionHandler exceptionHandler) {
        Assert.notNull(executor, "{executor} must not be null");
        Assert.notNull(exceptionHandler, "{exceptionHandler} must not be null");
        this.executor = executor;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void execute() {
        executor.setState(new QueueExecutorStateStop(executor, exceptionHandler));
    }
}
