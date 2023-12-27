package org.openjsr.model;

import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Transform;

import java.util.*;

/**
 * Данный класс хранит в себе вершины, полигоны и <b>Transform</b> модели.
 */
public class Model {
    public Model() {}

    /**
     * Создает модель с заданным списком полигонов и вершин. При это модель принимает нулевое положение в мире.
     * @param polygons список полигонов
     * @param vertices список вершин
     */
    public Model(List<Polygon> polygons, List<Vertex> vertices) {
        this.polygons = polygons;
        this.vertices = vertices;
    }

    /**
     * Создает модель с заданным списком полигонов, вершин и трансформацией (масштабом, поворотом и сдвигом)
     * @param polygons список полигонов
     * @param vertices список вершин
     * @param transform заданное положение
     */
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
     * Список вершин модели
     */
    private List<Vertex> vertices = new ArrayList<>();

    /**
     * Информация о масштабе, повороте и положении модели в мире.
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

    /**
     * Возвращает оригинальный экземпляр веришны с такими же параметрами, как у копии
     * @param vertex копия оригинальной вершины с такими же параметрами
     * @return оригинальную вершина, используемая в модели
     */
    public Vertex getOriginalVertex(Vertex vertex) {
        for (Vertex origin : vertices) {
            if (origin.equals(vertex)) {
                return origin;
            }
        }
        return vertex;
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

    /**
     * Добавляет вершину в список всех вершин модели
     * @param vertex добавляемая вершина
     */
    public void addVertex(Vertex vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
        }
    }

    /**
     * Добавляет полигон в список всех полигонов модели. Добавляет точки полигона, которые не содержатся в общем множестве.
     * @param polygon добавляемый полигон
     */
    public void addPolygon(Polygon polygon) {
        polygons.add(polygon);
        for (Vertex vertex : polygon.getVertices()) {
            addVertex(vertex);
        }
    }
}
