package org.openjsr.core;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.vector.Vector3f;

/**
 * Утилиты для работы с матрицами.
 */
public class MatrixMath {
    /**
     * Глобальное направление "вверх". Используется для расчёта матрицы "look at".
     */
    private static final Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);

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

    /**
     * Создаёт матрицу "look at".
     *
     * @param position Вектор положения камеры.
     * @param target   Вектор положения точки, на которую нацелена камера.
     * @return Матрица "look at".
     */
    public static Matrix4f lookAtMatrix(Vector3f position, Vector3f target) {
        Vector3f z = target.cpy().sub(position).nor();
        Vector3f x = UP.cpy().crs(z).nor();
        Vector3f y = z.cpy().crs(x).nor();

        return new Matrix4f(new float[]{
                x.x, y.x, z.x, 0.0f,
                x.y, y.y, z.y, 0.0f,
                x.z, y.z, z.z, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        });
    }

    /**
     * Создаёт матрицу перспективной проекции.
     *
     * @param fov         Полный угол поля зрения камеры.
     * @param aspectRatio Отношение сторон.
     * @param far         Дальняя плоскость отсечения.
     * @param near        Ближняя плоскость отсечения.
     * @return Матрица перспективной проекции.
     */
    public static Matrix4f projectionMatrix(float fov, float aspectRatio, float far, float near) {
        float tan = MathUtils.tanDeg(fov / 2.0f);
        return new Matrix4f(new float[]{
                1.0f / tan, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f / tan / aspectRatio, 0.0f, 0.0f,
                0.0f, 0.0f, (far + near) / (far - near), 1.0f,
                0.0f, 0.0f, (2.0f * far * near) / (near - far), 0.0f
        });
    }
}
