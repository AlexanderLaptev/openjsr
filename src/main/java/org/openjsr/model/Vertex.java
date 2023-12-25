package org.openjsr.model;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;

/**
 * Вершина хранит в себе координаты в мире, нормаль и текстурные координаты.
 */
public class Vertex {
    public Vertex() {}

    public Vertex(Vector3f cord, Vector3f normal, Vector2f uvCord) {
        this.cord = cord;
        this.normal = normal;
        this.uvCord = uvCord;
    }

    /**
     * Трехмерный вектор - координата вершины
     */
    public Vector3f cord = new Vector3f();

    /**
     * Трехмерный вектор - вектор нормали к вершине
     */
    public Vector3f normal = new Vector3f();

    /**
     * Двухмерный вектор текстурных координат (uv координаты)
     */
    public Vector2f uvCord = new Vector2f();
}
