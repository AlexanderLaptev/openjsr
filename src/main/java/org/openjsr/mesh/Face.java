package org.openjsr.mesh;

import java.util.ArrayList;
import java.util.List;

/**
 * Легковесная грань модельной сетки. Хранит только индексы вершин, нормалей и текстурных
 * координат исходной модели. Необязательно является треугольником, поэтому перед растеризацией
 * необходимо вручную производить триангуляцию модельной сетки.
 *
 * @see org.openjsr.mesh.triangulation.Triangulator
 * @see org.openjsr.mesh.triangulation.TriangulatedMesh
 */
public class Face {
    /**
     * Создаёт пустую грань.
     */
    public Face() { }

    /**
     * Список индексов вершин модели.
     */
    private List<Integer> vertexIndices = new ArrayList<>();

    /**
     * Список индексов текстурных вершин модели.
     */
    private List<Integer> textureVertexIndices = new ArrayList<>();

    /**
     * Список индексов нормалей модели.
     */
    private List<Integer> normalIndices = new ArrayList<>();

    /**
     * Устанавливает индексы вершин данной грани.
     *
     * @param vertexIndices Новые индексы вершин данной грани.
     */
    public void setVertexIndices(List<Integer> vertexIndices) {
        assert vertexIndices.size() >= 3;
        this.vertexIndices = vertexIndices;
    }

    /**
     * Устанавливает индексы текстурных координат данной грани.
     *
     * @param textureVertexIndices Новые индексы текстурных координат данной грани.
     */
    public void setTextureVertexIndices(List<Integer> textureVertexIndices) {
        assert textureVertexIndices.size() >= 3;
        this.textureVertexIndices = textureVertexIndices;
    }

    /**
     * Устанавливает индексы нормалей данной грани.
     *
     * @param normalIndices Новые индексы нормалей данной грани.
     */
    public void setNormalIndices(List<Integer> normalIndices) {
        assert normalIndices.size() >= 3;
        this.normalIndices = normalIndices;
    }

    /**
     * Получает индексы вершин данной грани.
     *
     * @return Индексы вершин данной грани.
     */
    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    /**
     * Получает индексы текстурных координат данной грани.
     *
     * @return Индексы текстурных координат данной грани.
     */
    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    /**
     * Получает индексы нормалей данной грани.
     *
     * @return Индексы нормалей данной грани.
     */
    public List<Integer> getNormalIndices() {
        return normalIndices;
    }
}
