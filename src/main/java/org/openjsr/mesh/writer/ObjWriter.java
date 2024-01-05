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
        StringBuilder contentsBuilder = new StringBuilder();
        writeMeshVertices(contentsBuilder, mesh.vertices);
        writeMeshTextureVertices(contentsBuilder, mesh.textureVertices);
        writeMeshNormals(contentsBuilder, mesh.normals);
        writeMeshFaces(contentsBuilder, mesh.faces);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(contentsBuilder.toString());
        } catch (IOException e) {
            throw new ObjWriterException(
                    "Ошибка при записи модели в файл .obj: " + file.getName() + " " + e.getMessage()
            );
        }
    }

    /**
     * Читает список трёхмерных векторов (координаты вершин).
     *
     * @param vertices Список вершин.
     */
    protected void writeMeshVertices(
            StringBuilder contentsBuilder,
            List<Vector3f> vertices
    ) {
        for (Vector3f vertex : vertices) appendVertex(contentsBuilder, vertex);
    }

    private void appendVertex(
            StringBuilder contentsBuilder,
            Vector3f vertex
    ) {
        contentsBuilder.append(OBJ_VERTEX_TOKEN);
        contentsBuilder.append(" ");
        contentsBuilder.append(vertex.x);
        contentsBuilder.append(" ");
        contentsBuilder.append(vertex.y);
        contentsBuilder.append(" ");
        contentsBuilder.append(vertex.z);
        contentsBuilder.append("\n");
    }

    /**
     * Читает список трёхмерных векторов (текстурных координаты вершин).
     *
     * @param textureVertices Список текстурных вершин.
     */
    protected void writeMeshTextureVertices(
            StringBuilder contentsBuilder,
            List<Vector2f> textureVertices
    ) {
        for (Vector2f vertex : textureVertices) appendTextureVertex(contentsBuilder, vertex);
    }

    private void appendTextureVertex(
            StringBuilder contentsBuilder,
            Vector2f vertex
    ) {
        contentsBuilder.append(OBJ_TEXTURE_TOKEN);
        contentsBuilder.append(" ");
        contentsBuilder.append(vertex.x);
        contentsBuilder.append(" ");
        contentsBuilder.append(vertex.y);
        contentsBuilder.append("\n");
    }

    /**
     * Читает список трёхмерных векторов (нормалей).
     *
     * @param normals Список нормалей.
     */
    protected void writeMeshNormals(
            StringBuilder contentsBuilder,
            List<Vector3f> normals
    ) {
        for (Vector3f normal : normals) appendNormal(contentsBuilder, normal);
    }

    private void appendNormal(
            StringBuilder contentsBuilder,
            Vector3f normal
    ) {
        contentsBuilder.append(OBJ_NORMAL_TOKEN);
        contentsBuilder.append(" ");
        contentsBuilder.append(normal.x);
        contentsBuilder.append(" ");
        contentsBuilder.append(normal.y);
        contentsBuilder.append(" ");
        contentsBuilder.append(normal.z);
        contentsBuilder.append("\n");
    }

    /**
     * Читает список полигонов.
     *
     * @param faces Список полигонов.
     */
    protected void writeMeshFaces(
            StringBuilder contentsBuilder,
            List<Face> faces
    ) {
        for (Face face : faces) {
            appendFace(
                    contentsBuilder,
                    face.getVertexIndices(),
                    face.getTextureVertexIndices(),
                    face.getNormalIndices()
            );
            contentsBuilder.append("\n");
        }
    }

    /**
     * Транспонирует данные полигона в строку.
     *
     * @param vertexIndices        Список вершин полигона.
     * @param textureVertexIndices Список текстурных вершин полигона.
     * @param normalIndices        Список нормалей полигона.
     */
    private void appendFace(
            StringBuilder contentsBuilder,
            List<Integer> vertexIndices,
            List<Integer> textureVertexIndices,
            List<Integer> normalIndices
    ) {
        contentsBuilder.append(OBJ_FACE_TOKEN);
        contentsBuilder.append(" ");

        int vertexCount = vertexIndices.size();
        int lastIndex = vertexCount - 1;
        for (int i = 0; i < vertexCount; i++) {
            // Мы подразумеваем, что в грани содержатся по крайней мере три вершины.
            contentsBuilder.append(vertexIndices.get(i) + 1);

            if (!normalIndices.isEmpty()) {
                contentsBuilder.append("/");
                if (!textureVertexIndices.isEmpty()) {
                    contentsBuilder.append(textureVertexIndices.get(i) + 1);
                }
                contentsBuilder.append("/");
                contentsBuilder.append(normalIndices.get(i) + 1);
            } else if (!textureVertexIndices.isEmpty()) {
                contentsBuilder.append("/");
                contentsBuilder.append(textureVertexIndices.get(i) + 1);
            }

            if (i != lastIndex) {
                contentsBuilder.append(" ");
            }
        }
    }
}
