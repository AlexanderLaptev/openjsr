package org.openjsr.core;

import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.render.Model;
import org.openjsr.render.framebuffer.Framebuffer;

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

    /**
     * Проецирует вершины модели на плоскость.
     * @param model проецируемая модель
     * @param width ширина плоскости
     * @param height высота плоскости
     * @return массив их векторов.
     */
    public Vector3f[] project(Model model, int width, int height) {
        Vector3f[] displayCoordinates = new Vector3f[model.getMesh().vertices.size()];
        for (int vertexInd = 0; vertexInd < displayCoordinates.length; vertexInd++) {

            Vector4f temp = getCombinedMatrix().mul(new Vector4f(model.getMesh().vertices.get(vertexInd), 1));

            float x = temp.x / temp.w;
            float y = temp.y / temp.w;
            float z = temp.z / temp.w;
            x = (float) (width - 1) / 2 * (x + 1);
            y = (float) (height - 1) / 2 * (1 - y);
            displayCoordinates[vertexInd] = new Vector3f(x, y, z);
        }
        return displayCoordinates;
    }
}
