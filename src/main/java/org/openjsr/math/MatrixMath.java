package org.openjsr.math;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.matrix.Matrix4f;

/**
 * Утилиты для работы с матрицами.
 */
public class MatrixMath {
    /**
     * Создаёт матрицу поворота по заданным углам в градусах в порядке XYZ.
     *
     * @param angleX Угол в градусах поворота вокруг оси X.
     * @param angleY Угол в градусах поворота вокруг оси Y.
     * @param angleZ Угол в градусах поворота вокруг оси Z.
     * @return Матрица поворота в порядке XYZ.
     */
    public static Matrix4f rotationMatrix(float angleX, float angleY, float angleZ) {
        float sinX = MathUtils.sinDeg(angleX);
        float cosX = MathUtils.cosDeg(angleX);
        float sinY = MathUtils.sinDeg(angleY);
        float cosY = MathUtils.cosDeg(angleY);
        float sinZ = MathUtils.sinDeg(angleZ);
        float cosZ = MathUtils.cosDeg(angleZ);

        float[] data = {
                cosY * cosZ,
                cosY * sinZ,
                -sinY,
                0,

                sinX * sinY * cosZ - cosX * sinZ,
                sinX * sinY * sinZ + cosX * cosZ,
                sinX * cosY,
                0,

                cosX * sinY * cosZ + sinX * sinZ,
                cosX * sinY * sinZ - sinX * cosZ,
                cosX * cosY,
                0,

                0, 0, 0, 1
        };
        return new Matrix4f(data);
    }
}
