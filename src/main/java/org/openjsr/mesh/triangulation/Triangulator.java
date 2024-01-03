package org.openjsr.mesh.triangulation;

import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;

import java.util.List;

public interface Triangulator {

    /**
     * Разбивает полигон на список треугольников
     *
     * @param face полигон, который нужно разбить на треугольники
     * @return список полигонов, имеющих только три вершины
     */
    List<Face> triangulateFace(Face face);

    /**
     * Создает обертку для модели, где есть список триангулированных полигонов
     *
     * @param mesh первоначальная модель
     * @return модель, куда добавлен список треугольников
     */
    TriangulatedMesh triangulate(Mesh mesh);

    /**
     * Перебирает список полигонов и создает из него список треугольников
     *
     * @param faces исходный список
     * @return список полигонов, имеющих только три вершины
     */
    List<Face> triangulateFaces(List<Face> faces);
}
