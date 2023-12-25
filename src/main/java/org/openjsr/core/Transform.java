package org.openjsr.core;

import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.math.MatrixMath;

/**
 * Комбинация положения, вращения и масштаба. Данный класс кеширует необходимые матрицы,
 * но не следит за их актуальностью, поэтому для обновления следует использовать метод
 * {@link Transform#recalculateMatrices}.
 */
public class Transform {
    /**
     * Создаёт стандартный Transform.
     */
    public Transform() {}

    /**
     * Создаёт Transform с заданным положением, вращением и масштабом.
     *
     * @param position Вектор положения.
     * @param rotation Вектор углов вращения.
     * @param scale    Вектор масштабов.
     */
    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Трёхмерный вектор положения.
     */
    public Vector3f position = new Vector3f();

    /**
     * Трёхмерный вектор углов в градусах вокруг каждой из трёх осей.
     */
    public Vector3f rotation = new Vector3f();

    /**
     * Вектор масштаба по каждой из трёх осей.
     */
    public Vector3f scale = new Vector3f(1.0f, 1.0f, 1.0f);

    /**
     * Матрица перемещения.
     */
    public Matrix4f translationMatrix = Matrix4f.identity();

    /**
     * Матрица поворота. Поворот осуществляется в порядке XYZ.
     */
    public Matrix4f rotationMatrix = Matrix4f.identity();

    /**
     * Матрица масштабирования.
     */
    public Matrix4f scaleMatrix = Matrix4f.identity();

    /**
     * Комбинированная матрица масштабирования, поворота и перемещения.
     */
    public Matrix4f combinedMatrix = Matrix4f.identity();

    /**
     * Пересчитывает кешированные матрицы масштабирования,
     * поворота,перемещения, а так же комбинированную матрицу.
     */
    public void recalculateMatrices() {
        scaleMatrix.val[Matrix4f.M11] = scale.x;
        scaleMatrix.val[Matrix4f.M22] = scale.y;
        scaleMatrix.val[Matrix4f.M33] = scale.z;

        rotationMatrix = MatrixMath.rotationMatrix(rotation.x, rotation.y, rotation.z);

        translationMatrix.val[Matrix4f.M14] = position.x;
        translationMatrix.val[Matrix4f.M24] = position.y;
        translationMatrix.val[Matrix4f.M34] = position.z;

        combinedMatrix = rotationMatrix.cpy().mul(scaleMatrix);
        combinedMatrix.val[Matrix4f.M14] = position.x;
        combinedMatrix.val[Matrix4f.M24] = position.y;
        combinedMatrix.val[Matrix4f.M34] = position.z;

//        combinedMatrix = translationMatrix.cpy().mul(rotationMatrix.cpy().mul(scaleMatrix));
    }
}
