package org.openjsr.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Хранилище вершин одного полигона. Не обязательно должен быть треугольником, поэтому перед растеризацией нужно проводить триангуляцию.
 */
public class Polygon {
    public Polygon() {}

    public Polygon(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    /**
     * Список вершин полигона
     */
    private List<Vertex> vertices = new ArrayList<>();

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }
}
