package me.blokhin;

import lombok.EqualsAndHashCode;

import java.util.Queue;

@EqualsAndHashCode
public class QueueExecutorStateDump implements QueueExecutorState {
    private final QueueExecutor executor;
    private final Queue<Command> target;

    public QueueExecutorStateDump(final QueueExecutor executor, final Queue<Command> target) {
        Assert.notNull(executor, "{executor} must not be null");
        Assert.notNull(target, "{target} must not be null");

        this.executor = executor;
        this.target = target;
    }

    @Override
    public boolean canContinue() {
        return !executor.isQueueEmpty();
    }

    @Override
    public void process(Command command) {
        target.add(command);
    }
}
