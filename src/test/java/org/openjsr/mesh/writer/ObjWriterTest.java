package org.openjsr.mesh.writer;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.mesh.reader.ObjReader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObjWriterTest {
    private final ObjReader READER = new ObjReader();

    private final ObjWriter WRITER = new ObjWriter();

    @Test
    public void testWriteMeshVertices() throws IOException {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(1.0f, 1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            WRITER.writeMeshVertices(printWriter, vertices);
        }

        String fileContent = Files.readString(Path.of("Test file.obj"));

        assertTrue(fileContent.contains("v 1.0 1.0 1.0"));
    }

    @Test
    public void testWriteMeshTextureVertices() throws IOException {
        ArrayList<Vector2f> textureVertices = new ArrayList<>();
        textureVertices.add(new Vector2f(1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            WRITER.writeMeshTextureVertices(printWriter, textureVertices);
        }

        String fileContent = Files.readString(Path.of("Test file.obj"));

        assertTrue(fileContent.contains("vt 1.0 1.0"));
    }

    @Test
    public void testWriteMeshNormals() throws IOException {
        ArrayList<Vector3f> normals = new ArrayList<>();
        normals.add(new Vector3f(1.0f, 1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            WRITER.writeMeshNormals(printWriter, normals);
        }

        String fileContent = Files.readString(Path.of("Test file.obj"));

        assertTrue(fileContent.contains("vn 1.0 1.0 1.0"));
    }

    @Test
    public void testWritePolygonsToObjFile() throws IOException {
        ArrayList<Face> polygons = new ArrayList<>();
        Face polygon = new Face();
        polygon.setVertexIndices(new ArrayList<>(Arrays.asList(-1, 1, 4)));
        polygon.setTextureVertexIndices(new ArrayList<>(Arrays.asList(-1, 1, 4)));
        polygon.setNormalIndices(new ArrayList<>(Arrays.asList(-1, 1, 4)));
        polygons.add(polygon);
    public void testWriteMeshFaces() throws IOException {

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            WRITER.writeMeshFaces(printWriter, polygons);
        }

        String fileContent = Files.readString(Path.of("Test file.obj"));
        assertTrue(fileContent.contains("f 0/0/0 2/2/2 5/5/5"));
    }

    @Test
    public void compareWithReader() throws IOException {
        String fileContent = Files.readString(Path.of("src/test/resources/meshes/cube.obj"));
        Mesh originalModel = READER.read(fileContent);

        WRITER.write(originalModel, new File("Test file.obj"));
        String fileContent2 = Files.readString(Path.of("Test file.obj"));
        Mesh newModel = READER.read(fileContent2);

        assertEquals(originalModel.normals, newModel.normals);
        assertEquals(originalModel.vertices, newModel.vertices);
        assertEquals(originalModel.textureVertices, newModel.textureVertices);
    }

}
