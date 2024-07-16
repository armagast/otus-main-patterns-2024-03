package me.blokhin;

import lombok.Value;

import java.util.Queue;

@Value
public class CommandQueueExecutorDump implements Command {
    QueueExecutor executor;
    Queue<Command> target;

    public CommandQueueExecutorDump(final QueueExecutor executor, final Queue<Command> target) {
        Assert.notNull(executor, "{executor} must not be null");
        Assert.notNull(target, "{target} must not be null");

        this.executor = executor;
        this.target = target;
    }

    @Override
    public void execute() {
        executor.setState(new QueueExecutorStateDump(executor, target));
    }
}
