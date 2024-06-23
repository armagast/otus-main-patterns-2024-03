package me.blokhin;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class QueueExecutor {
    private final Queue<Command> queue;
    private final ExceptionHandler exceptionHandler;

    private final List<Consumer<QueueExecutor>> onEnter = new LinkedList<>();
    private final List<Consumer<QueueExecutor>> onLeave = new LinkedList<>();

    private Supplier<Boolean> canContinue = this::canContinueAlways;

    public QueueExecutor(final Queue<Command> queue,
                         final ExceptionHandler exceptionHandler) {

        Assert.notNull(queue, "{queue} must not be null");
        Assert.notNull(exceptionHandler, "{exceptionHandler} must not be null");
        this.queue = queue;
        this.exceptionHandler = exceptionHandler;
    }

    public void onEnter(final Consumer<QueueExecutor> onEnter) {
        Assert.notNull(onEnter, "{onEnter} must not be null");
        this.onEnter.add(onEnter);
    }

    public void onLeave(final Consumer<QueueExecutor> onLeave) {
        Assert.notNull(onLeave, "{onLeave} must not be null");
        this.onLeave.add(onLeave);
    }

    public void run() {
        onEnter.forEach(handler -> handler.accept(this));

        while (canContinue.get()) {
            final Command command = queue.poll();

            if (Objects.nonNull(command)) {
                try {
                    command.execute();
                } catch (Exception exception) {
                    exceptionHandler.handle(command, exception);
                }
            }
        }

        onLeave.forEach(handler -> handler.accept(this));
    }

    public void kill() {
        this.canContinue = this::canContinueNever;
    }

    public void stop() {
        this.canContinue = this::canContinueOnNonEmptyQueue;
    }

    private boolean canContinueAlways() {
        return true;
    }

    private boolean canContinueNever() {
        return false;
    }

    private boolean canContinueOnNonEmptyQueue() {
        return !queue.isEmpty();
    }
}
