package org.openjsr.mesh.triangulation;

import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleTriangulator implements Triangulator {
    private static final SimpleTriangulator INSTANCE = new SimpleTriangulator();

    private SimpleTriangulator() { }

    public static SimpleTriangulator getInstance() {
        return INSTANCE;
    }

    /**
     * Простая триангуляция - последовательно соединяет вершины в треугольники
     *
     * @param face полигон, который нужно разбить на треугольники
     * @return список полигонов, имеющих только три вершины (треугольники)
     */
    @Override
    public List<Face> triangulateFace(Face face) {
        List<Face> triangles = new ArrayList<>();

        if (face.getVertexIndices().size() == 3) {
            triangles.add(face);
            return triangles;
        }

        for (int verListInd = 2; verListInd < face.getVertexIndices().size(); verListInd++) {
            Face triangle = new Face();
            // Списки вершин/текстурных вершин/нормалей в треугольниках имеют фиксированный размер.
            triangle.setVertexIndices(
                    Arrays.asList(
                            face.getVertexIndices().get(0),
                            face.getVertexIndices().get(verListInd - 1),
                            face.getVertexIndices().get(verListInd)
                    ));

            if (!face.getNormalIndices().isEmpty()) {
                triangle.setNormalIndices(
                        Arrays.asList(
                                face.getNormalIndices().get(0),
                                face.getNormalIndices().get(verListInd - 1),
                                face.getNormalIndices().get(verListInd)
                        )
                );
            }

            if (!face.getTextureVertexIndices().isEmpty()) {
                triangle.setTextureVertexIndices(
                        Arrays.asList(
                                face.getTextureVertexIndices().get(0),
                                face.getTextureVertexIndices().get(verListInd - 1),
                                face.getTextureVertexIndices().get(verListInd)
                        )
                );

            }
            triangles.add(triangle);
        }
        return triangles;
    }

    /**
     * Создает обертку для модели, где есть список триангулированных полигонов
     *
     * @param mesh первоначальная модель
     * @return модель, куда добавлен список треугольников
     */
    @Override
    public TriangulatedMesh triangulate(Mesh mesh) {
        List<Face> triangles = triangulateFaces(mesh.faces);
        return new TriangulatedMesh(mesh, triangles);
    }

    /**
     * Перебирает список полигонов и создает из него список треугольников
     *
     * @param faces исходный список
     * @return список полигонов, имеющих только три вершины
     */
    @Override
    public List<Face> triangulateFaces(List<Face> faces) {
        List<Face> triangles = new ArrayList<>();
        for (Face face : faces) {
            triangles.addAll(triangulateFace(face));
        }
        return triangles;
    }
}
