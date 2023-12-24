package org.openjsr.math;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.matrix.Matrix4f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MatrixMathTest {
    @Test
    void rotationX() {
        Matrix4f m = MatrixMath.rotationMatrix(45.0f, 0.0f, 0.0f);
        Matrix4f expected = new Matrix4f(new float[]{
                1, 0, 0, 0,
                0, 0.7071068f, 0.7071068f, 0,
                0, -0.7071068f, 0.7071068f, 0,
                0, 0, 0, 1
        });
        assertTrue(expected.epsEquals(m, MathUtils.EPSILON));
    }

    @Test
    void rotationY() {
        Matrix4f m = MatrixMath.rotationMatrix(0.0f, 45.0f, 0.0f);
        Matrix4f expected = new Matrix4f(new float[]{
                0.7071068f, 0, -0.7071068f, 0,
                0, 1, 0, 0,
                0.7071068f, 0, 0.7071068f, 0,
                0, 0, 0, 1
        });
        assertTrue(expected.epsEquals(m, MathUtils.EPSILON));
    }

    @Test
    void rotationZ() {
        Matrix4f m = MatrixMath.rotationMatrix(0.0f, 0.0f, 45.0f);
        Matrix4f expected = new Matrix4f(new float[]{
                0.707168f, 0.707168f, 0, 0,
                -0.707168f, 0.707168f, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1
        });
        assertTrue(expected.epsEquals(m, 3 * MathUtils.EPSILON));
    }

    @Test
    void rotationCombined() {
        Matrix4f m = MatrixMath.rotationMatrix(31.4f, -79.28f, -173.24f);
        Matrix4f expected = new Matrix4f(new float[]{
                -0.1847164f, -0.0218953f, 0.9825479f, 0.0f,
                0.6088302f, -0.7873588f, 0.0969128f, 0.0f,
                0.7714958f, 0.6161062f, 0.1587686f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        assertTrue(expected.epsEquals(m, MathUtils.EPSILON));
    }
}
