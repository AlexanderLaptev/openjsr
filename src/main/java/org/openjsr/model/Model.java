package org.openjsr.model;

import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Transform;

import java.util.ArrayList;
import java.util.List;

/**
 * Данный класс
 */
public class Model {
    public Model() {}

    public Model(List<Polygon> polygons, List<Vertex> vertices) {
        this.polygons = polygons;
        this.vertices = vertices;
    }

    public Model(List<Polygon> polygons, List<Vertex> vertices, Transform transform) {
        this.polygons = polygons;
        this.vertices = vertices;
        this.transform = transform;
    }

    /**
     * Список полигонов (плоскостей).
     */
    private List<Polygon> polygons = new ArrayList<>();

    /**
     * Список вершин.
     */
    private List<Vertex> vertices = new ArrayList<>();

    /**
     * Информация о трансформации модели
     */
    private Transform transform = new Transform(
            new Vector3f(new float[]{0f, 0f, 0f}),
            new Vector3f(new float[]{0f, 0f, 0f}),
            new Vector3f(new float[]{0f, 0f, 0f}));


    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }

    public void addVertex(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
    }
}
