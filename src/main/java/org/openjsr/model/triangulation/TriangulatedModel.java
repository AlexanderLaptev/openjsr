package org.openjsr.model.triangulation;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Transform;
import org.openjsr.model.Model;
import org.openjsr.model.Polygon;

import java.util.List;

/**
 * Класс - обертка модели, куда включен список треугольников, использующихся для дальнейшей отрисовки. Наследует модель, поэтому из нее можно достать любые данные.
 */
public class TriangulatedModel extends Model {

    public TriangulatedModel(List<Polygon> triangles) {
        this.triangles = triangles;
    }

    public TriangulatedModel(List<Vector3f> vertices, List<Vector3f> normals, List<Vector2f> textureVertices, List<Polygon> polygons, Transform transform, List<Polygon> triangles) {
        super(vertices, normals, textureVertices, polygons, transform);
        this.triangles = triangles;
    }

    public TriangulatedModel(List<Vector3f> vertices, List<Vector3f> normals, List<Vector2f> textureVertices, List<Polygon> polygons, List<Polygon> triangles) {
        super(vertices, normals, textureVertices, polygons);
        this.triangles = triangles;
    }

    /**
     * Создает новый экземпляр класса с использованием уже готовой модели - достает из нее все данные.
     * @param model исходная модель
     * @param triangles список треугольников модели
     */
    public TriangulatedModel(Model model, List<Polygon> triangles) {
        super(model.vertices, model.normals, model.textureVertices, model.polygons, model.transform);
        this.triangles = triangles;
    }

    /**
     * Список полигонов, у которых только три вершины, три текстурных координаты и три нормали.
     */
    public List<Polygon> triangles;

    /**
     * Изменяет полигоны и треугольники в модели.
     * Следует использовать этот метод, вместо изменения списка polygons напрямую, если при этом должны измениться и треугольники.
     * @param polygons новый список полигонов
     * @param triangulator экземпляр триаунгулятора, чья реализация будет использована при триангуляции полигонов.
     */
    public void setPolygons(List<Polygon> polygons, Triangulator triangulator) {
        super.polygons = polygons;
        triangles = triangulator.triangulateList(polygons);
    }
}
