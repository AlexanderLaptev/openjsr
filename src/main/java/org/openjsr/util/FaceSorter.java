package org.openjsr.util;

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
        public Vector4f projected;
        public Integer vertexIndex;
        public Integer textureVertexIndex;
        public Integer normalIndex;

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

    public static Face sortFace(Mesh mesh, Face face, Vector4f[] projectedVertices) {
        List<FaceVertex> faceVertices = new ArrayList<>();

        int size = face.getVertexIndices().size();
        for (int i = 0; i < size; i++) {
            FaceVertex fv = new FaceVertex();

            fv.vertexIndex = face.getVertexIndices().get(i);
            fv.projected = projectedVertices[fv.vertexIndex];
            if (!face.getTextureVertexIndices().isEmpty()) {
                fv.textureVertexIndex = face.getTextureVertexIndices().get(i);
            }
            if (!face.getNormalIndices().isEmpty()) {
                fv.normalIndex = face.getNormalIndices().get(i);
            }

            faceVertices.add(fv);
        }

        faceVertices.sort(COMPARATOR);
        Face sortedFace = new Face();
        for (FaceVertex fv : faceVertices) {
            sortedFace.getVertexIndices().add(fv.vertexIndex);
            if (fv.textureVertexIndex != null) sortedFace.getTextureVertexIndices().add(fv.textureVertexIndex);
            if (fv.normalIndex != null) sortedFace.getNormalIndices().add(fv.normalIndex);
        }
        return sortedFace;
    }
}
