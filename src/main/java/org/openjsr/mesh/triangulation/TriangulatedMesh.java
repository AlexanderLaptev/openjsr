package org.openjsr.mesh.triangulation;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Transform;
import org.openjsr.mesh.Mesh;
import org.openjsr.mesh.Face;

import java.util.List;

/**
 * Класс - обертка модели, куда включен список треугольников, использующихся для дальнейшей отрисовки. Наследует модель, поэтому из нее можно достать любые данные.
 */
public class TriangulatedMesh extends Mesh {

    public TriangulatedMesh(List<Face> triangles) {
        this.triangles = triangles;
    }

    public TriangulatedMesh(List<Vector3f> vertices, List<Vector3f> normals, List<Vector2f> textureVertices, List<Face> faces, Transform transform, List<Face> triangles) {
        super(vertices, normals, textureVertices, faces, transform);
        this.triangles = triangles;
    }

    public TriangulatedMesh(List<Vector3f> vertices, List<Vector3f> normals, List<Vector2f> textureVertices, List<Face> faces, List<Face> triangles) {
        super(vertices, normals, textureVertices, faces);
        this.triangles = triangles;
    }

    /**
     * Создает новый экземпляр класса с использованием уже готовой модели - достает из нее все данные.
     * @param mesh исходная модель
     * @param triangles список треугольников модели
     */
    public TriangulatedMesh(Mesh mesh, List<Face> triangles) {
        super(mesh.vertices, mesh.normals, mesh.textureVertices, mesh.faces, mesh.transform);
        this.triangles = triangles;
    }

    /**
     * Список полигонов, у которых только три вершины, три текстурных координаты и три нормали.
     */
    public List<Face> triangles;

    /**
     * Изменяет полигоны и треугольники в модели.
     * Следует использовать этот метод, вместо изменения списка polygons напрямую, если при этом должны измениться и треугольники.
     * @param faces новый список полигонов
     * @param triangulator экземпляр триаунгулятора, чья реализация будет использована при триангуляции полигонов.
     */
    public void setPolygons(List<Face> faces, Triangulator triangulator) {
        super.faces = faces;
        triangles = triangulator.triangulateFaces(faces);
    }
}
