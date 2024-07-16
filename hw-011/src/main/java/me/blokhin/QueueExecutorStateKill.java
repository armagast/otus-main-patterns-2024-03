package me.blokhin;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class QueueExecutorStateKill implements QueueExecutorState {
    @Override
    public boolean canContinue() {
        return false;
    }

    @Override
    public void process(Command command) {
    }
}
