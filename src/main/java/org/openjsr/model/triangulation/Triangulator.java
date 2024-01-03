package org.openjsr.model.triangulation;

import org.openjsr.model.Model;
import org.openjsr.model.Polygon;

import java.util.List;

public interface Triangulator {

    /**
     * Разбивает полигон на список треугольников
     * @param polygon полигон, который нужно разбить на треугольники
     * @return список полигонов, имеющих только три вершины
     */
    List<Polygon> triangulatePolygon(Polygon polygon);

    /**
     * Создает обертку для модели, где есть список триангулированных полигонов
     * @param model первоначальная модель
     * @return модель, куда добавлен список треугольников
     */
    TriangulatedModel triangulate(Model model);

    /**
     * Перебирает список полигонов и создает из него список треугольников
     * @param polygons исходный список
     * @return список полигонов, имеющих только три вершины
     */
    List<Polygon> triangulateList(List<Polygon> polygons);
}