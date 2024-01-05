package org.openjsr.mesh.reader;

import static org.junit.jupiter.api.Assertions.*;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

class ObjWriterTest {
    private final ObjReader READER = new ObjReader();

    @Test
    public void testWriteVerticesToObjFile() throws IOException {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(1.0f, 1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            ObjWriter.writeVerticesOfModel(printWriter, vertices);
        }

        String fileContent = Files.readString(Path.of("Test file.obj"));

        assertTrue(fileContent.contains("v 1.0 1.0 1.0"));
    }

    @Test
    public void testWriteTextureVerticesToObjFile() throws IOException {
        ArrayList<Vector2f> textureVertices = new ArrayList<>();
        textureVertices.add(new Vector2f(1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            ObjWriter.writeTextureVerticesOfModel(printWriter, textureVertices);
        }

        String fileContent = Files.readString(Path.of("Test file.obj"));

        assertTrue(fileContent.contains("vt 1.0 1.0"));
    }

    @Test
    public void testWriteNormalsToObjFile() throws IOException {
        ArrayList<Vector3f> normals = new ArrayList<>();
        normals.add(new Vector3f(1.0f, 1.0f, 1.0f));

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            ObjWriter.writeNormalsOfModel(printWriter, normals);
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

        try (PrintWriter printWriter = new PrintWriter("Test file.obj")) {
            ObjWriter.writePolygonsOfModel(printWriter, polygons);
        }

        String fileContent = Files.readString(Path.of("Test file.obj"));
        assertTrue(fileContent.contains("f 0/0/0 2/2/2 5/5/5"));
    }

    @Test
    public void testCompareObjFiles() throws IOException {
        String fileContent = Files.readString(Path.of("src/test/resources/meshes/cube.obj"));
        Mesh originalModel = READER.read(fileContent);

        ObjWriter.writeModelToObjFile("Test file.obj", originalModel);
        String fileContent2 = Files.readString(Path.of("Test file.obj"));
        Mesh newModel = READER.read(fileContent2);

       assertEquals(originalModel.normals, newModel.normals);
       assertEquals(originalModel.vertices, newModel.vertices);
       assertEquals(originalModel.textureVertices, newModel.textureVertices);
    }

}