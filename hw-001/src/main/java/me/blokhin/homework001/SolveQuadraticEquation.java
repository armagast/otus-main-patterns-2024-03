package me.blokhin.homework001;

public class SolveQuadraticEquation {
    public static void assertTrue(final boolean condition, final String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    public double[] solve(final double a, final double b, final double c) {
        return solve(a, b, c, 1E-05);
    }

    public double[] solve(final double a, final double b, final double c, final double epsilon) {
        assertTrue(Math.abs(a) > epsilon, "{a} must not be in [-epsilon, +epsilon]");

        final double d = b * b - 4 * a * c;

        if (d < 0) {
            return new double[0];
        }

        if (d > 0) {
            final double x1 = (-b + Math.sqrt(d)) / (2 * a);
            final double x2 = (-b - Math.sqrt(d)) / (2 * a);

            return new double[]{x1, x2};
        }

        final double x = (-b) / (2 * a);

        return new double[]{x};
    }
}
