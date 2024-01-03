package org.openjsr.mesh;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище координат вершин, нормалей, текстурных вершин, полигонов и трансформации модели. Не подразумевает под собой никакой логики.
 */
public class Mesh {

    /**
     * Создает пустую модель
     */
    public Mesh() {
    }

    /**
     * Создает модель с указанными данными
     *
     * @param vertices        список трехмерных векторов координат вершин
     * @param normals         список трехмерных векторов нормалей
     * @param textureVertices список двухмерных векторов текстурных координат
     * @param faces           список полигонов
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
}
