package me.blokhin;

import lombok.Value;

@Value
public class CommandQueueExecutorRun implements Command {
    QueueExecutor executor;

    public CommandQueueExecutorRun(QueueExecutor executor) {
        Assert.notNull(executor, "{executor} must not be null");
        this.executor = executor;
    }

    @Override
    public void execute() {
        new Thread(executor::run).start();
    }
}
