package org.openjsr.core;

import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.MathUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformTest {
    @Test
    void test() {
        Transform tr = new Transform();
        tr.position.set(5.65f, -18.28f, 31.4f);
        tr.rotation.set(55.0f, 27.4f, -39.24f);
        tr.scale.set(14.33f, -5.29f, -18.14f);
        tr.recalculateMatrices();

        Matrix4f expected = new Matrix4f(new float[]{
                9.8535344f, -8.0478068f, -6.5946631f, 0.0f,
                -3.463862f, -1.0885471f, -3.8471832f, 0.0f,
                5.6911256f, 14.5375556f, -9.2374322f, 0.0f,
                5.65f, -18.28f, 31.4f, 1.0f
        });

        assertTrue(expected.epsEquals(tr.combinedMatrix, MathUtils.EPSILON));
    }
}
