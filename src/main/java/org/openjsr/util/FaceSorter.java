package org.openjsr.util;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Вспомогательный класс, сортирующий вершины грани по возрастанию координаты y.
 */
public class FaceSorter {
    private static class FaceVertex {
        public Vector3f projected;
        public Vector3f vertex;
        public Vector2f texture;
        public Vector3f normal;
        public Integer index;

        public FaceVertex() { }
    }

    /**
     * Компаратор, сравнивающий трёхмерные векторы сначала по координате y,
     * а затем по координате x (если координаты y равны).
     * Используется для сортировки треугольников.
     */
    private static final Comparator<FaceVertex> COMPARATOR = (a, b) -> {
        int cmp = Float.compare(a.projected.y, b.projected.y);
        return cmp != 0 ? cmp : Float.compare(a.projected.x, b.projected.x);
    };

    public static Face sortFace(Mesh mesh, Face face, Vector3f[] projectedVertices) {
        List<FaceVertex> faceVertices = new ArrayList<>();

        int size = face.getVertexIndices().size();
        for (int i = 0; i < size; i++) {
            FaceVertex fv = new FaceVertex();
            fv.index = i;
            fv.projected = projectedVertices[face.getNormalIndices().get(i)];
            fv.vertex = mesh.vertices.get(face.getVertexIndices().get(i));
            if (!face.getTextureVertexIndices().isEmpty()) {
                fv.texture = mesh.textureVertices.get(face.getTextureVertexIndices().get(i));
            }
            if (!face.getNormalIndices().isEmpty()) {
                fv.normal = mesh.normals.get(face.getNormalIndices().get(i));
            }
            faceVertices.add(fv);
        }

        faceVertices.sort(COMPARATOR);
        Face sortedFace = new Face();
        for (FaceVertex fv : faceVertices) {
            sortedFace.getVertexIndices().add(fv.index);
            if (fv.texture != null) sortedFace.getTextureVertexIndices().add(fv.index);
            if (fv.normal != null) sortedFace.getNormalIndices().add(fv.index);
        }
        return sortedFace;
    }
}
