package me.blokhin;

public interface QueueExecutorState {
    boolean canContinue();
    void process(Command command);
}
