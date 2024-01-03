package org.openjsr.render;

import cg.vsu.render.math.matrix.Matrix4f;
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
     * Массив координат вершин в мировом пространстве. При обновлении трансформации модели
     * эти координаты должны быть пересчитаны с помощью метода {@link Model#updateWorldCoordinates()}.
     */
    private Vector4f[] worldVertexCoordinates;

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
        this.worldVertexCoordinates = new Vector4f[mesh.vertices.size()];
        updateWorldCoordinates();
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
     * Устанавливает сетку для данной модели и пересчитывает мировые координаты её вершин.
     *
     * @param mesh Новая сетка для данной модели.
     * @see Model#updateWorldCoordinates()
     */
    public void setMesh(TriangulatedMesh mesh) {
        this.mesh = mesh;
        updateWorldCoordinates();
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
     * Устанавливает трансформацию для данной модели и пересчитывает мировые координаты её вершин.
     *
     * @param transform Новый объект {@link Transform} для данной модели.
     */
    public void setTransform(Transform transform) {
        this.transform = transform;
        updateWorldCoordinates();
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
    public Vector4f[] getWorldVertexCoordinates() {
        return worldVertexCoordinates;
    }

    /**
     * Пересчитывает мировые координаты вершин после обновления соответствующего {@link Transform}.
     */
    public void updateWorldCoordinates() {
        Matrix4f T = transform.combinedMatrix.cpy();
        for (int i = 0; i < mesh.vertices.size(); i++) {
            Vector4f meshVertex = new Vector4f(mesh.vertices.get(i));
            worldVertexCoordinates[i] = T.mul(meshVertex);
        }
    }
}
