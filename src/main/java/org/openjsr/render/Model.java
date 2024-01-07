package org.openjsr.render;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Transform;
import org.openjsr.mesh.triangulation.TriangulatedMesh;
import org.openjsr.render.shader.Shader;

/**
 * Представляет собой трёхмерную модель на сцене.
 */
public class Model {
    /**
     * Триангулированная сетка, на которой основана данная модель.
     */
    private TriangulatedMesh mesh;

    /**
     * Трансформация данной модели.
     */
    private Transform transform;

    /**
     * Шейдер, использующийся для данной модели.
     */
    private Shader shader;

    /**
     * Массив координат вершин в мировом пространстве.
     */
    private Vector4f[] worldVertices;

    /**
     * Массив спроецированных вершин в мировом пространстве.
     */
    private Vector4f[] projectedVertices;

    /**
     * Создаёт модель с заданной сеткой, трансформацией и шейдером.
     *
     * @param mesh      Сетка модели.
     * @param transform Трансформация модели.
     * @param shader    Шейдер модели.
     */
    public Model(TriangulatedMesh mesh, Transform transform, Shader shader) {
        this.mesh = mesh;
        this.transform = transform;
        this.shader = shader;
        this.worldVertices = new Vector4f[mesh.vertices.size()];
    }


    /**
     * Создаёт модель с заданными сеткой и шейдером и трансформацией по умолчанию.
     *
     * @param mesh   Сетка модели.
     * @param shader Шейдер модели.
     */
    public Model(TriangulatedMesh mesh, Shader shader) {
        this(mesh, new Transform(), shader);
    }

    /**
     * Получает сетку данной модели.
     *
     * @return Сетку данной модели.
     */
    public TriangulatedMesh getMesh() {
        return mesh;
    }

    /**
     * Устанавливает сетку для данной модели.
     *
     * @param mesh Новая сетка для данной модели.
     */
    public void setMesh(TriangulatedMesh mesh) {
        this.mesh = mesh;
    }

    /**
     * Получает объект {@link Transform} данной модели.
     *
     * @return Объект {@link Transform} данной модели.
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Устанавливает трансформацию для данной модели.
     *
     * @param transform Новый объект {@link Transform} для данной модели.
     */
    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    /**
     * Получает шейдер, использующийся данной моделью.
     *
     * @return Шейдер, использующийся данной моделью.
     */
    public Shader getShader() {
        return shader;
    }

    /**
     * Устанавливает шейдер, использующийся данной моделью.
     *
     * @param shader Новый шейдер для данной модели.
     */
    public void setShader(Shader shader) {
        this.shader = shader;
    }

    /**
     * Получает массив мировых координат вершин модели.
     *
     * @return Массив координат вершин модели в мировом пространстве.
     */
    public Vector4f[] getWorldVertices() {
        return worldVertices;
    }

    public void setWorldVertices(Vector4f[] worldVertices) {
        this.worldVertices = worldVertices;
    }

    public boolean isValid() {
        return mesh != null && !mesh.vertices.isEmpty() && shader != null && transform != null;
    }

    public Vector4f[] getProjectedVertices() {
        return projectedVertices;
    }

    public void setProjectedVertices(Vector4f[] projectedVertices) {
        this.projectedVertices = projectedVertices;
    }
}
