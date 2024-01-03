package org.openjsr.mesh;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище координат вершин, нормалей, текстурных вершин, полигонов и трансформации модели. Не подразумевает под собой никакой логики.
 */
public class Mesh {

    /**
     * Создает пустую модель с нулевой трансформацией
     */
    public Mesh() {
    }

    /**
     * Создает модель с указанными данными
     * @param vertices список трехмерных векторов координат вершин
     * @param normals список трехмерных векторов нормалей
     * @param textureVertices список двухмерных векторов текстурных координат
     * @param faces список полигонов
     * @param transform трансформация модели
     */
    public Mesh(
            List<Vector3f> vertices,
            List<Vector3f> normals,
            List<Vector2f> textureVertices,
            List<Face> faces,
            Transform transform) {

        this.vertices = vertices;
        this.normals = normals;
        this.textureVertices = textureVertices;
        this.faces = faces;
        this.transform = transform;
    }

    /**
     * Создает модель с указанными данными и нулевой трансформацией.
     * @param vertices список трехмерных векторов координат вершин
     * @param normals список трехмерных векторов нормалей
     * @param textureVertices список двухмерных векторов текстурных координат
     * @param faces список полигонов
     */
    public Mesh(
            List<Vector3f> vertices,
            List<Vector3f> normals,
            List<Vector2f> textureVertices,
            List<Face> faces) {

        this.vertices = vertices;
        this.normals = normals;
        this.textureVertices = textureVertices;
        this.faces = faces;
    }

    /**
     * Список трехмерных векторов - координат вершин
     */
    public List<Vector3f> vertices = new ArrayList<>();

    /**
     * Список трехмерных векторов - координат нормалей
     */
    public List<Vector3f> normals = new ArrayList<>();

    /**
     * Список двухмерных векторов - текстурных координат
     */
    public List<Vector2f> textureVertices = new ArrayList<>();

    /**
     * Список полигонов
     */
    public List<Face> faces = new ArrayList<>();

    /**
     * Трансформация модели (ее  положение, вращение и масштаб). По умолчанию положение в нуле, вращение отсутствует, а масштаб - единица.
     */
    public Transform transform = new Transform(
            new Vector3f(new float[]{0.0F, 0.0F, 0.0F}),
            new Vector3f(new float[]{0.0F, 0.0F, 0.0F}),
            new Vector3f(new float[]{1.0F, 1.0F, 1.0F})
    );
}
