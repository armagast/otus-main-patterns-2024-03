package me.blokhin;

import lombok.Value;

@Value
public class CommandQueueExecutorStop implements Command {
    QueueExecutor executor;

    public CommandQueueExecutorStop(QueueExecutor executor) {
        Assert.notNull(executor, "{executor} must not be null");
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.stop();
    }
}
