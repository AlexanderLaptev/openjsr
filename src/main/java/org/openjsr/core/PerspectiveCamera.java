package org.openjsr.core;

import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;

/**
 * Камера с перспективной проекцией.
 */
public class PerspectiveCamera {
    /**
     * Угол поля зрения камеры.
     */
    private float fieldOfView = 90.0f;

    /**
     * Соотношение сторон (отношение высоты экрана к его ширине).
     */
    private float aspectRatio = 9.0f / 16.0f;

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
    public PerspectiveCamera() { }

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
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                -position.x, -position.y, -position.z, 1.0f
        });
        Matrix4f orientation = MatrixMath.lookAtMatrix(position, target);
        viewMatrix = translation.mul(orientation);
        recalculateMatrices();
    }

    /**
     * Пересчитывает проекционную и комбинированную матрицы.
     */
    private void recalculateMatrices() {
        projectionMatrix = MatrixMath.projectionMatrix(fieldOfView, aspectRatio, farPlane, nearPlane);
        combinedMatrix = projectionMatrix.cpy().mul(viewMatrix);
    }

    /**
     * Преобразует координаты точки из нормализованных (от -1 до 1) в экранные.
     * Координаты z и w остаются без изменений.
     * Модифицирует переданный вектор на месте, не создаёт новых векторов.
     *
     * @param normalized   Точка в нормализованной системе координат.
     * @param screenWidth  Ширина экрана.
     * @param screenHeight Высота экрана.
     * @return Та же точка в системе координат экрана.
     */
    public Vector4f normalizedToScreen(Vector4f normalized, int screenWidth, int screenHeight) {
        normalized.x = (screenWidth / 2.0f) * (normalized.x + 1.0f);
        normalized.y = (screenHeight / 2.0f) * (1.0f - normalized.y);
        return normalized;
    }

    /**
     * Преобразует координаты точки из мировых в нормализованные (от -1 до 1).
     * Модифицирует переданный вектор на месте, не создаёт новых векторов.
     *
     * @param worldPosition Точка с координатами в мировой системе.
     * @return Точка в нормализованной системе координат.
     */
    public Vector4f project(Vector4f worldPosition) {
        combinedMatrix.mul(worldPosition);
        worldPosition.x = worldPosition.x / worldPosition.w;
        worldPosition.y = worldPosition.y / worldPosition.w;
        worldPosition.z = worldPosition.z / worldPosition.w;
        return worldPosition;
    }

    /**
     * Преобразует координаты точки из мировых в экранные.
     * Модифицирует переданный вектор на месте, не создаёт новых векторов.
     *
     * @param worldPosition Точка с координатами в мировой системе.
     * @param screenWidth   Ширина экрана.
     * @param screenHeight  Высота экрана.
     * @return Та же точка в системе координат экрана.
     */
    public Vector4f project(Vector4f worldPosition, int screenWidth, int screenHeight) {
        project(worldPosition);
        normalizedToScreen(worldPosition, screenWidth, screenHeight);
        return worldPosition;
    }
}
