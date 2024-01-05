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
        writeModelToObjFile(file.getPath(), mesh);
    }

    /**
     * Проходит по модели и записывает в файл( {@code .obj}) все ее параметры.
     *
     * @param fileName Название файла.
     * @param mesh     Модель.
     */
    public static void writeModelToObjFile(String fileName, Mesh mesh) {

        File file = new File(fileName);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            writeVerticesOfModel(printWriter, mesh.vertices);
            writeTextureVerticesOfModel(printWriter, mesh.textureVertices);
            writeNormalsOfModel(printWriter, mesh.normals);
            writePolygonsOfModel(printWriter, mesh.faces);
        } catch (IOException e) {
            throw new ObjWriterException("Error writing model to obj file: " + fileName + " " + e.getMessage());
        }
    }

    /**
     * Читает список трёхмерных векторов (координаты вершин).
     *
     * @param vertices Список вершин.
     */
    protected static void writeVerticesOfModel(PrintWriter printWriter, List<Vector3f> vertices) {
        for (Vector3f vertex : vertices) {
            printWriter.println(OBJ_VERTEX_TOKEN + " " + vertex.x + " " + vertex.y + " " + vertex.z);
        }
    }

    /**
     * Читает список трёхмерных векторов (текстурных координаты вершин).
     *
     * @param textureVertices Список текстурных вершин.
     */
    protected static void writeTextureVerticesOfModel(PrintWriter printWriter, List<Vector2f> textureVertices) {
        for (Vector2f vertex : textureVertices) {
            printWriter.println(OBJ_TEXTURE_TOKEN + " " + vertex.x + " " + vertex.y);
        }
    }

    /**
     * Читает список трёхмерных векторов (нормалей).
     *
     * @param normals Список нормалей.
     */
    protected static void writeNormalsOfModel(PrintWriter printWriter, List<Vector3f> normals) {
        for (Vector3f normal : normals) {
            printWriter.println(OBJ_NORMAL_TOKEN + " " + normal.x + " " + normal.y + " " + normal.z);
        }
    }

    /**
     * Читает список полигонов.
     *
     * @param faces Список полигонов.
     */
    protected static void writePolygonsOfModel(PrintWriter printWriter, List<Face> faces) {
        for (Face face : faces) {
            printWriter.print(modelsPolygonToFaceForObjFile(
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
     * @param vertexIndices Список вершин полигона.
     * @param textureVertexIndices Список текстурных вершин полигона.
     * @param normalIndices Список нормалей полигона.
     */
    private static String modelsPolygonToFaceForObjFile(List<Integer> vertexIndices, List<Integer> textureVertexIndices, List<Integer> normalIndices) {
        StringBuilder objFace = new StringBuilder();
        objFace.append(OBJ_FACE_TOKEN+" ");

        for (int i = 0; i < vertexIndices.size(); i++) {
            if (!textureVertexIndices.isEmpty()) {
                objFace.append(vertexIndices.get(i) + 1).append("/").append(textureVertexIndices.get(i) + 1);
            } else {
                objFace.append(vertexIndices.get(i) + 1);
            }
            if (!normalIndices.isEmpty()) {
                if (textureVertexIndices.isEmpty()) {
                    objFace.append("/");
                }
                objFace.append("/").append(normalIndices.get(i) + 1);
            }
            if (i != vertexIndices.size() - 1) {
                objFace.append(" ");
            }
        }

        return objFace.toString();
    }
}
