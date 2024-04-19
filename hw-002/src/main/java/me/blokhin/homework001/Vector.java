package me.blokhin.homework001;

import lombok.Value;

@Value
public class Vector {
    int x;
    int y;

    public Vector add(Vector that) {
        Assert.notNull(that, "{that} must not be null");

        return new Vector(x + that.x, y + that.y);
    }
}
