package org.openjsr.mesh;

import cg.vsu.render.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MeshNormalComputer {
    /**
     * Проходит по полигонам модели, вычисляя нормали полигонов и сумму нормалей каждой вершины.
     * нормализует каждую нормаль вершиын.
     *
     * @param mesh Модель.
     * @return Список трёхмерных векторов (нормалей вершин).
     */
    public List<Vector3f> normalsVertex(Mesh mesh) {
        List<Vector3f> normalsVertex = new ArrayList<Vector3f>();
        List<Vector3f> normalsPolygon = new ArrayList<Vector3f>();

        Integer[] count = new Integer[mesh.vertices.size()];
        Vector3f[] normalSummaVertex = new Vector3f[mesh.vertices.size()];

        for (int indexPoligon = 0; indexPoligon < mesh.faces.size(); indexPoligon++) {
            normalsPolygon.add(indexPoligon, normalPolygon(mesh.faces.get(indexPoligon), mesh.vertices));

            List<Integer> vertexIndices = mesh.faces.get(indexPoligon).getVertexIndices();

            mesh.faces.get(indexPoligon).setNormalIndices(vertexIndices);

            for (Integer vertexIndex : vertexIndices) {
                if (normalSummaVertex[vertexIndex] == null) {
                    normalSummaVertex[vertexIndex] = new Vector3f(normalsPolygon.get(indexPoligon));
                    count[vertexIndex] = 1;
                } else {
                    normalSummaVertex[vertexIndex].add(normalsPolygon.get(indexPoligon));
                    count[vertexIndex]++;
                }
            }
        }

        for (int i = 0; i < count.length; i++) {
            normalsVertex.add(i, normalSummaVertex[i].div(count[i]).div(normalSummaVertex[i].div(count[i]).len()));
        }
        return normalsVertex;
    }

    private Vector3f normalPolygon(Face face, List<Vector3f> vertices) {
        List<Integer> vertexIndices = face.getVertexIndices();
        try {
            return vector(
                    vertices.get(vertexIndices.get(0)),
                    vertices.get(vertexIndices.get(1))
            ).crs(
                    vector(
                            vertices.get(vertexIndices.get(0)),
                            vertices.get(vertexIndices.get(2))
                    )
            );
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("известно менее 3 вершин (у полигона)");
        }
        return null;
    }

    private Vector3f vector(Vector3f v1, Vector3f v2) {
        return new Vector3f(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }
}
