package me.blokhin.homework001;

public class SolveQuadraticEquation {
    public double[] solve(final double a, final double b, final double c) {
        final double d = b * b - 4 * a * c;

        if (d < 0) {
            return new double[0];
        }

        throw new Error("Not implemented");
    }
}
