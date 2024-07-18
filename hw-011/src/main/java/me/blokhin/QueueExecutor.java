package me.blokhin;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;

public class QueueExecutor {
    private final Queue<Command> queue;

    private final List<Consumer<QueueExecutor>> onEnter = new LinkedList<>();
    private final List<Consumer<QueueExecutor>> onLeave = new LinkedList<>();

    private QueueExecutorState state;

    public QueueExecutor(final Queue<Command> queue, final QueueExecutorState state) {
        Assert.notNull(queue, "{queue} must not be null");
        Assert.notNull(state, "{state} must not be null");

        this.queue = queue;
        this.state = state;
    }

    public void setState(final QueueExecutorState state) {
        Assert.notNull(state, "{state} must not be null");

        this.state = state;
    }

    public void onEnter(final Consumer<QueueExecutor> onEnter) {
        Assert.notNull(onEnter, "{onEnter} must not be null");
        this.onEnter.add(onEnter);
    }

    public void onLeave(final Consumer<QueueExecutor> onLeave) {
        Assert.notNull(onLeave, "{onLeave} must not be null");
        this.onLeave.add(onLeave);
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

    public void run() {
        onEnter.forEach(handler -> handler.accept(this));

        while (state.canContinue()) {
            final Command command = queue.poll();

            if (Objects.nonNull(command)) {
                state.process(command);
            }
        }

        onLeave.forEach(handler -> handler.accept(this));
    }
}
