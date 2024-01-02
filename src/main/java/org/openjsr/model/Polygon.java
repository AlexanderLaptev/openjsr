package org.openjsr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище индексов вершин, нормалей и текстурных координат. Не обязательно должен быть треугольником, поэтому перед растеризацией нужно проводить триангуляцию.
 */
public class Polygon {

    /**
     * Создает пустой полигон
     */
    public Polygon() {}

    /**
     * Список индексов вершин из модели
     */
    private List<Integer> vertexIndices = new ArrayList<>();

    /**
     * Список индексов текстурных вершин из модели
     */
    private List<Integer> textureVertexIndices = new ArrayList<>();

    /**
     * Список индексов нормалей из модели
     */
    private List<Integer> normalIndices = new ArrayList<>();

    public void setVertexIndices(List<Integer> vertexIndices) {
        assert vertexIndices.size() >= 3;
        this.vertexIndices = vertexIndices;
    }

    public void setTextureVertexIndices(List<Integer> textureVertexIndices) {
        assert textureVertexIndices.size() >= 3;
        this.textureVertexIndices = textureVertexIndices;
    }

    public void setNormalIndices(List<Integer> normalIndices) {
        assert normalIndices.size() >= 3;
        this.normalIndices = normalIndices;
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }

    public List<Integer> getTextureVertexIndices() {
        return textureVertexIndices;
    }

    public List<Integer> getNormalIndices() {
        return normalIndices;
    }
}
