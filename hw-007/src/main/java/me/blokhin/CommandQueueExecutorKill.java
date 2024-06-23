package me.blokhin;

import lombok.Value;

@Value
public class CommandQueueExecutorKill implements Command {
    QueueExecutor executor;

    public CommandQueueExecutorKill(QueueExecutor executor) {
        Assert.notNull(executor, "{executor} must not be null");
        this.executor = executor;
    }

    @Override
    public void execute() {
        executor.kill();
    }
}
