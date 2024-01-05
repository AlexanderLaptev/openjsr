package org.openjsr.mesh.writer;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ObjWriter implements MeshWriter {
    /**
     * Токен вершины.
     */
    private static final String OBJ_VERTEX_TOKEN = "v";

    /**
     * Токен текстурной вершины (UV-координаты).
     */
    private static final String OBJ_TEXTURE_TOKEN = "vt";

    /**
     * Токен нормали вершины.
     */
    private static final String OBJ_NORMAL_TOKEN = "vn";

    /**
     * Токен описания грани.
     */
    private static final String OBJ_FACE_TOKEN = "f";

    @Override
    public void write(Mesh mesh, File file) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            writeVerticesOfModel(printWriter, mesh.vertices);
            writeTextureVerticesOfModel(printWriter, mesh.textureVertices);
            writeNormalsOfModel(printWriter, mesh.normals);
            writePolygonsOfModel(printWriter, mesh.faces);
        } catch (IOException e) {
            throw new ObjWriterException("Error writing model to obj file: " + file.getName() + " " + e.getMessage());
        }
    }

    /**
     * Читает список трёхмерных векторов (координаты вершин).
     *
     * @param vertices Список вершин.
     */
    protected void writeVerticesOfModel(PrintWriter printWriter, List<Vector3f> vertices) {
        for (Vector3f vertex : vertices) {
            printWriter.println(OBJ_VERTEX_TOKEN + " " + vertex.x + " " + vertex.y + " " + vertex.z);
        }
    }

    /**
     * Читает список трёхмерных векторов (текстурных координаты вершин).
     *
     * @param textureVertices Список текстурных вершин.
     */
    protected void writeTextureVerticesOfModel(PrintWriter printWriter, List<Vector2f> textureVertices) {
        for (Vector2f vertex : textureVertices) {
            printWriter.println(OBJ_TEXTURE_TOKEN + " " + vertex.x + " " + vertex.y);
        }
    }

    /**
     * Читает список трёхмерных векторов (нормалей).
     *
     * @param normals Список нормалей.
     */
    protected void writeNormalsOfModel(PrintWriter printWriter, List<Vector3f> normals) {
        for (Vector3f normal : normals) {
            printWriter.println(OBJ_NORMAL_TOKEN + " " + normal.x + " " + normal.y + " " + normal.z);
        }
    }

    /**
     * Читает список полигонов.
     *
     * @param faces Список полигонов.
     */
    protected void writePolygonsOfModel(PrintWriter printWriter, List<Face> faces) {
        for (Face face : faces) {
            printWriter.print(faceToString(
                    face.getVertexIndices(),
                    face.getTextureVertexIndices(),
                    face.getNormalIndices()
            ));
            printWriter.println();
        }
    }

    /**
     * Транспонирует данные полигона в строку.
     *
     * @param vertexIndices        Список вершин полигона.
     * @param textureVertexIndices Список текстурных вершин полигона.
     * @param normalIndices        Список нормалей полигона.
     */
    private String faceToString(
            List<Integer> vertexIndices,
            List<Integer> textureVertexIndices,
            List<Integer> normalIndices
    ) {
        StringBuilder sbFace = new StringBuilder();
        sbFace.append(OBJ_FACE_TOKEN);
        sbFace.append(" ");

        int vertexCount = vertexIndices.size();
        int lastIndex = vertexCount - 1;
        for (int i = 0; i < vertexCount; i++) {
            // Мы подразумеваем, что в грани содержатся по крайней мере три вершины.
            sbFace.append(vertexIndices.get(i) + 1);

            if (!normalIndices.isEmpty()) {
                sbFace.append("/");
                if (!textureVertexIndices.isEmpty()) {
                    sbFace.append(textureVertexIndices.get(i) + 1);
                }
                sbFace.append("/");
                sbFace.append(normalIndices.get(i) + 1);
            } else if (!textureVertexIndices.isEmpty()) {
                sbFace.append("/");
                sbFace.append(textureVertexIndices.get(i) + 1);
            }

            if (i != lastIndex) {
                sbFace.append(" ");
            }
        }

        return sbFace.toString();
    }
}
