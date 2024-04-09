package me.blokhin.homework001;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class SolveQuadraticEquationTest {
    private final SolveQuadraticEquation solveQuadraticEquation = new SolveQuadraticEquation();

    @Test
    @DisplayName("x² + 1 = 0 has no solutions")
    void test001() {
        final double[] exp = {};

        assertArrayEquals(exp, solveQuadraticEquation.solve(1, 0, 1));
    }

    @Test
    @DisplayName("x² - 1 = 0 has 2 solutions")
    void test002() {
        final double[] exp = {(-1), 1};
        final double[] act = solveQuadraticEquation.solve(1, 0, (-1));

        assertArrayEquals(exp, Arrays.stream(act).sorted().toArray());
    }

    @Test
    @DisplayName("x² + 2x - 1 = 0 has 1 solution")
    void test003() {
        final double[] exp = {(-1)};

        assertArrayEquals(exp, solveQuadraticEquation.solve(1, 2, 1));
    }

    @Test
    @DisplayName("throws exception when {a} is near zero")
    void test004() {
        assertThrows(IllegalArgumentException.class, () -> solveQuadraticEquation.solve(0, 1, 1));
    }
}
