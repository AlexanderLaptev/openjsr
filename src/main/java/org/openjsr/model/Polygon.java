package org.openjsr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище вершин одного полигона. Не обязательно должен быть треугольником, поэтому перед растеризацией нужно проводить триангуляцию.
 */
public class Polygon {

    private List<Integer> vertexIndices = new ArrayList<>();
    private List<Integer> textureVertexIndices = new ArrayList<>();
    private List<Integer> normalIndices = new ArrayList<>();


    public Polygon() {}

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
