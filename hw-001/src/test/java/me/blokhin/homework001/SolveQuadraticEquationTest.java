package me.blokhin.homework001;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SolveQuadraticEquationTest {
    private final SolveQuadraticEquation solveQuadraticEquation = new SolveQuadraticEquation();

    @Test
    void test001() {
        final double[] exp = {};

        assertArrayEquals(exp, solveQuadraticEquation.solve(1, 0, 1));
    }
}
