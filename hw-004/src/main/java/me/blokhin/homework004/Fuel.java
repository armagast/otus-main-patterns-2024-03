package me.blokhin.homework004;

public interface Fuel {
    Fuel burn(Fuel amount);

    boolean lessThan(Fuel that);
}
