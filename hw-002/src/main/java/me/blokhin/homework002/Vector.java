package me.blokhin.homework002;

import lombok.Value;

@Value
public class Vector {
    int[] components;

    public Vector(int ...components) {
        this.components = components;
    }

    public Vector add(Vector that) {
        Assert.notNull("that", "{that} must not be null");
        Assert.isTrue(components.length == that.components.length, "{that} must have same dimension");

        int[] componentsNext = new int[components.length];
        for (int i = 0; i < componentsNext.length; i++) {
            componentsNext[i] = components[i] + that.components[i];
        }

        return new Vector(componentsNext);
    }
}
