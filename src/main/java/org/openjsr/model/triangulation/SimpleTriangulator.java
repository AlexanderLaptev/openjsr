package org.openjsr.model.triangulation;

import org.openjsr.model.Model;
import org.openjsr.model.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTriangulator implements Triangulator {

    /**
     * Простая триангуляция - последовательно соединяет вершины в треугольники
     * @param polygon полигон, который нужно разбить на треугольники
     * @return список полигонов, имеющих только три вершины (треугольники)
     */
    @Override
    public List<Polygon> triangulatePolygon(Polygon polygon) {
        List<Polygon> triangles = new ArrayList<>();

        if (polygon.getVertexIndices().size() == 3) {
            triangles.add(polygon);
            return triangles;
        }

        for (int verListInd = 2; verListInd < polygon.getVertexIndices().size(); verListInd++) {
            Polygon triangle = new Polygon();
            triangle.setVertexIndices(
                    Arrays.asList(
                            polygon.getVertexIndices().get(0),
                            polygon.getVertexIndices().get(verListInd - 1),
                            polygon.getVertexIndices().get(verListInd)));

            triangle.setNormalIndices(
                    Arrays.asList(
                            polygon.getNormalIndices().get(0),
                            polygon.getNormalIndices().get(verListInd - 1),
                            polygon.getNormalIndices().get(verListInd)));

            triangle.setTextureVertexIndices(
                    Arrays.asList(
                            polygon.getTextureVertexIndices().get(0),
                            polygon.getTextureVertexIndices().get(verListInd - 1),
                            polygon.getTextureVertexIndices().get(verListInd)));

            triangles.add(triangle);
        }
        return triangles;
    }

    /**
     * Создает обертку для модели, где есть список триангулированных полигонов
     * @param model первоначальная модель
     * @return модель, куда добавлен список треугольников
     */
    @Override
    public TriangulatedModel triangulate(Model model) {
        List<Polygon> triangles = triangulateList(model.polygons);
        return new TriangulatedModel(model, triangles);
    }

    /**
     * Перебирает список полигонов и создает из него список треугольников
     * @param polygons исходный список
     * @return список полигонов, имеющих только три вершины
     */
    @Override
    public List<Polygon> triangulateList(List<Polygon> polygons) {
        List<Polygon> triangles = new ArrayList<>();
        for (Polygon polygon : polygons) {
            triangles.addAll(triangulatePolygon(polygon));
        }
        return triangles;
    }
}
