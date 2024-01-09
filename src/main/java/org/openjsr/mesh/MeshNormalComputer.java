package org.openjsr.mesh;

import cg.vsu.render.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Вычисляет нормали модели.
 */
public class MeshNormalComputer {
    private static final MeshNormalComputer INSTANCE = new MeshNormalComputer();

    private MeshNormalComputer() {
    }

    public static MeshNormalComputer getInstance() {
        return INSTANCE;
    }

    /**
     * Проходит по полигонам модели, вычисляя нормали полигонов и сумму нормалей каждой вершины.
     *
     * @param mesh Модель, нормали которой необходимо перерассчитать.
     * @return Список трёхмерных векторов-нормалей вершин.
     */
    public List<Vector3f> computeNormals(Mesh mesh) {
        int faceCount = mesh.faces.size();
        int vertexCount = mesh.vertices.size();
        List<Vector3f> vertexNormals = new ArrayList<>(vertexCount);
        List<Vector3f> faceNormals = new ArrayList<>(faceCount);

        Integer[] count = new Integer[mesh.vertices.size()];
        Arrays.fill(count, 1);
        Vector3f[] sumOfNormalsForVertex = new Vector3f[mesh.vertices.size()];

        for (int faceIndex = 0; faceIndex < faceCount; faceIndex++) {
            Face face = mesh.faces.get(faceIndex);
            faceNormals.add(new Vector3f(computeFaceNormal(face, mesh.vertices)));
            
            List<Integer> faceNormalIndices = new ArrayList<>();
            for (Integer f: face.getVertexIndices()){
                faceNormalIndices.add(f);
            }
            face.setNormalIndices(faceNormalIndices);

            for (Integer vertexIndex : faceNormalIndices) {
                if (sumOfNormalsForVertex[vertexIndex] == null) {
                    sumOfNormalsForVertex[vertexIndex] = new Vector3f(faceNormals.get(faceIndex));
                } else {
                    sumOfNormalsForVertex[vertexIndex].add(faceNormals.get(faceIndex));
                    count[vertexIndex]++;
                }
            }
        }

        for (int i = 0; i < vertexCount; i++) {
            if (sumOfNormalsForVertex[i] == null) {
                vertexNormals.add(new Vector3f(0, 0, 0));
            } else {
                vertexNormals.add(sumOfNormalsForVertex[i].div(count[i]).nor());
            }
        }
        return vertexNormals;
    }

    private Vector3f computeFaceNormal(Face face, List<Vector3f> vertices) {
        List<Integer> vertexIndices = face.getVertexIndices();
        if (vertexIndices.size() < 3) throw new ArrayIndexOutOfBoundsException("У грани менее трёх вершин.");

        return getVectorBetweenPoints(
                vertices.get(vertexIndices.get(0)),
                vertices.get(vertexIndices.get(1))
        ).crs(
                getVectorBetweenPoints(
                        vertices.get(vertexIndices.get(0)),
                        vertices.get(vertexIndices.get(2))
                )
        );
    }

    private Vector3f getVectorBetweenPoints(Vector3f from, Vector3f to) {
        return new Vector3f(to.x - from.x, to.y - from.y, to.z - from.z);
    }
}
