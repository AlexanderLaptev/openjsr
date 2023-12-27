package org.openjsr.model;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;

import java.util.Objects;

/**
 * Вершина хранит в себе координаты в мире, нормаль и текстурные координаты.
 */
public class Vertex {
    public Vertex() {}

    /**
     * Создает вершину с задаными координатами в пространстве
     * @param coord трехмерный вектор точки в пространстве
     */
    public Vertex(Vector3f coord) {
        this.coord = coord;
    }

    /**
     * Создает вершину с заданными координатами в пространстве, нормальным вектором и текстурными координатами
     * @param coord трехмерный вектор точки в пространстве
     * @param normal трехмерный вектор нормали
     * @param uvCoord двухмерный вектор текстурной координаты
     */
    public Vertex(Vector3f coord, Vector3f normal, Vector2f uvCoord) {
        this.coord = coord;
        this.normal = normal;
        this.uvCoord = uvCoord;
    }

    /**
     * Трехмерный вектор - координата вершины
     */
    public Vector3f coord = new Vector3f();

    /**
     * Трехмерный вектор - вектор нормали к вершине
     */
    public Vector3f normal = new Vector3f();

    /**
     * Двухмерный вектор текстурных координат (uv координаты)
     */
    public Vector2f uvCoord = new Vector2f();

    /**
     * Показывает, равны ли вершины. Если значения всех координат и векторов равны, то вершины
     * @param o объект, с которым сравниваем
     * @return true, если объекты равны, false, если различны
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex other = (Vertex) o;
        return coord.equals(other.coord) &&
                normal.equals(other.normal) &&
                uvCoord.equals(other.uvCoord);
    }

    /**
     * Создает хэшкод для вершины на основе хэшей векторов.
     * @return целочисленное значение хэшкода
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                coord.hashCode(),
                normal.hashCode(),
                uvCoord.hashCode());
    }
}
