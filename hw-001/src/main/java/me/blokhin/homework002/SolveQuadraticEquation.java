package me.blokhin.homework002;

public class SolveQuadraticEquation {
    public static void assertTrue(final boolean condition, final String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }

    public double[] solve(final double a, final double b, final double c) {
        return solve(a, b, c, 1E-05);
    }

    public double[] solve(final double a, final double b, final double c, final double epsilon) {
        assertTrue(Double.isFinite(a), "{a} must be finite number");
        assertTrue(Double.isFinite(b), "{b} must be finite number");
        assertTrue(Double.isFinite(c), "{c} must be finite number");
        assertTrue(Double.isFinite(epsilon), "{epsilon} must be finite number");

        assertTrue(epsilon > 0, "{epsilon} must be greater than 0");
        assertTrue(Math.abs(a) > epsilon, "{a} must not be in [-epsilon, +epsilon]");

        final double d = b * b - 4 * a * c;

        if (d < -epsilon) {
            return new double[0];
        }

        if (d > +epsilon) {
            final double x1 = (-b + Math.sqrt(d)) / (2 * a);
            final double x2 = (-b - Math.sqrt(d)) / (2 * a);

            return new double[]{x1, x2};
        }

        final double x = (-b) / (2 * a);

        return new double[]{x};
    }
}
