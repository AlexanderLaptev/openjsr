package org.openjsr.core;

import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.math.MatrixMath;

/**
 * Камера с перспективной проекцией.
 */
public class PerspectiveCamera {
    /**
     * Угол поля зрения камеры.
     */
    private float fieldOfView = 90.0f;

    /**
     * Соотношение сторон.
     */
    private float aspectRatio = 16.0f / 9.0f;

    /**
     * Дальняя плоскость отсечения.
     */
    private float farPlane = 1000.0f;

    /**
     * Ближняя плоскость отсечения.
     */
    private float nearPlane = 1.0f;

    /**
     * Вектор положения камеры.
     */
    private Vector3f position = new Vector3f();

    /**
     * Вектор точки, на которую нацелена камера.
     */
    private Vector3f viewTarget = new Vector3f();

    /**
     * Видовая матрица.
     */
    private Matrix4f viewMatrix = Matrix4f.identity();

    /**
     * Проекционная матрица.
     */
    private Matrix4f projectionMatrix = Matrix4f.identity();

    /**
     * Комбинированная матрица -- произведение проекционной и видовой матриц.
     */
    private Matrix4f combinedMatrix = Matrix4f.identity();

    /**
     * Создаёт новую камеру с позицией в точке (0,0,0).
     */
    public PerspectiveCamera() {}

    /**
     * Создаёт новую камеру и поворачивает её так, чтобы она смотрела на точку (0,0,0).
     *
     * @param position Положение камеры.
     */
    public PerspectiveCamera(Vector3f position) {
        this.position.set(position);
        lookAt(Vector3f.zero());
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
        recalculateMatrices();
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
        recalculateMatrices();
    }

    public float getFarPlane() {
        return farPlane;
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
        recalculateMatrices();
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
        recalculateMatrices();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        lookAt(viewTarget);
    }

    public Vector3f getViewTarget() {
        return viewTarget;
    }

    public void setViewTarget(Vector3f viewTarget) {
        this.viewTarget = viewTarget;
        lookAt(viewTarget);
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f getCombinedMatrix() {
        return combinedMatrix;
    }

    /**
     * Устанавливает точку, в которую смотрит камера.
     *
     * @param target Вектор точки.
     */
    public void lookAt(Vector3f target) {
        Matrix4f translation = new Matrix4f(new float[]{
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 0.0f,
                -position.x, -position.y, -position.z, 1.0f
        });
        Matrix4f orientation = MatrixMath.lookAtMatrix(position, target);
        viewMatrix = orientation.mul(translation);
        recalculateMatrices();
    }

    /**
     * Пересчитывает проекционную и комбинированную матрицы.
     */
    private void recalculateMatrices() {
        projectionMatrix = MatrixMath.projectionMatrix(fieldOfView, aspectRatio, farPlane, nearPlane);
        combinedMatrix = projectionMatrix.cpy().mul(viewMatrix);
    }
}
