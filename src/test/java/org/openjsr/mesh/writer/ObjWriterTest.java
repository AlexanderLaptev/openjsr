package org.openjsr.mesh.writer;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.mesh.reader.ObjReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjWriterTest {
    private final ObjReader READER = new ObjReader();

    private final ObjWriter WRITER = new ObjWriter();

    private final StringBuilder BUILDER = new StringBuilder();

    @BeforeEach
    public void setup() {
        BUILDER.setLength(0);
    }

    @Test
    public void testWriteMeshVertices() {
        ArrayList<Vector3f> vertices = new ArrayList<>();
        vertices.add(new Vector3f(1.0f, 1.0f, 1.0f));
        WRITER.writeMeshVertices(BUILDER, vertices);

        assertEquals("v 1.0 1.0 1.0\n", BUILDER.toString());
    }

    @Test
    public void testWriteMeshTextureVertices() {
        ArrayList<Vector2f> textureVertices = new ArrayList<>();
        textureVertices.add(new Vector2f(1.0f, 1.0f));
        WRITER.writeMeshTextureVertices(BUILDER, textureVertices);

        assertEquals("vt 1.0 1.0\n", BUILDER.toString());
    }

    @Test
    public void testWriteMeshNormals() {
        ArrayList<Vector3f> normals = new ArrayList<>();
        normals.add(new Vector3f(1.0f, 1.0f, 1.0f));
        WRITER.writeMeshNormals(BUILDER, normals);

        assertEquals("vn 1.0 1.0 1.0\n", BUILDER.toString());
    }

    @Test
    public void testWriteMeshFaces() {
        ArrayList<Face> faces = new ArrayList<>();
        Face face = new Face();
        face.setVertexIndices(new ArrayList<>(Arrays.asList(3, 0, 4)));
        face.setTextureVertexIndices(new ArrayList<>(Arrays.asList(8, 2, 5)));
        face.setNormalIndices(new ArrayList<>(Arrays.asList(0, 4, 6)));
        faces.add(face);
        WRITER.writeMeshFaces(BUILDER, faces);

        assertEquals("f 4/9/1 1/3/5 5/6/7\n", BUILDER.toString());
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
