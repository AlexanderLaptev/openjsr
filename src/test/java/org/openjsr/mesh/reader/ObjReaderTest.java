package org.openjsr.mesh.reader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjReaderTest {
    private final ObjReader READER = new ObjReader();

    @Test
    void vector2fNormal() {
        String testStr = "vt 151.37926 -84.52563";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        Vector2f result = READER.parseVector2f(words, 1);
        Vector2f expected = new Vector2f(151.37926f, -84.52563f);

        assertEquals(expected, result);
    }

    @Test
    void vector2fExtra() {
        String testStr = "vt 151.37926 -84.52563 51.672221 -248.3241";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        Vector2f result = READER.parseVector2f(words, 1);
        Vector2f expected = new Vector2f(151.37926f, -84.52563f);

        assertEquals(expected, result);
    }

    @Test
    void vector2fInvalidNumber() {
        String testStr = "vt 151.37926 -B84.52563";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        assertThrows(ObjReaderException.class, () -> READER.parseVector2f(words, 1));
    }

    @Test
    void vector2fTooFewCoords() {
        String testStr = "vt 151.37926";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        assertThrows(ObjReaderException.class, () -> READER.parseVector2f(words, 1));
    }

    @Test
    void vector3fExpected() {
        String testStr = "v 3 0.023752 -5.3400";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        Vector3f result = READER.parseVector3f(words, 0);
        Vector3f expected = new Vector3f(3.0f, 0.023752f, -5.34f);

        assertEquals(expected, result);
    }

    @Test
    void vector3fExtra() {
        String testStr = "v 3 0.023752 -5.3400 ashl;hklqha";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        Vector3f result = READER.parseVector3f(words, 0);
        Vector3f expected = new Vector3f(3.0f, 0.023752f, -5.34f);

        assertEquals(expected, result);
    }

    @Test
    void vector3fInvalidNumber() {
        String testStr = "v 3 0.023752 -b5.3^%^#400f";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        assertThrows(ObjReaderException.class, () -> READER.parseVector3f(words, 0));
    }

    @Test
    void vector3fTooFewCoords() {
        String testStr = "v 3 0.023752";
        List<String> words = new ArrayList<>(Arrays.asList(testStr.split(" ")));
        words.remove(0);

        assertThrows(ObjReaderException.class, () -> READER.parseVector3f(words, 0));
    }

    @Test
    void faceVertexOnlyVertex() {
        String faceVertex = "7";
        List<Integer> vertex = new ArrayList<>();
        List<Integer> texture = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();

        READER.parseFaceVertex(faceVertex, vertex, texture, normal, 1);

        assertEquals(List.of(6), vertex);
        assertTrue(texture.isEmpty());
        assertTrue(normal.isEmpty());
    }

    @Test
    void faceVertexVertexAndTexture() {
        String faceVertex = "7/4";
        List<Integer> vertex = new ArrayList<>();
        List<Integer> texture = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();

        READER.parseFaceVertex(faceVertex, vertex, texture, normal, 1);

        assertEquals(List.of(6), vertex);
        assertEquals(List.of(3), texture);
        assertTrue(normal.isEmpty());
    }

    @Test
    void faceVertexVertexAndNormal() {
        String faceVertex = "7//2";
        List<Integer> vertex = new ArrayList<>();
        List<Integer> texture = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();

        READER.parseFaceVertex(faceVertex, vertex, texture, normal, 1);

        assertEquals(List.of(6), vertex);
        assertTrue(texture.isEmpty());
        assertEquals(List.of(1), normal);
    }

    @Test
    void faceVertexEverything() {
        String faceVertex = "7/4/2";
        List<Integer> vertex = new ArrayList<>();
        List<Integer> texture = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();

        READER.parseFaceVertex(faceVertex, vertex, texture, normal, 1);

        assertEquals(List.of(6), vertex);
        assertEquals(List.of(3), texture);
        assertEquals(List.of(1), normal);
    }

    @Test
    void faceVertexExtraCoordinate() {
        String faceVertex = "7/4/2/-14";
        List<Integer> vertex = new ArrayList<>();
        List<Integer> texture = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();

        assertThrows(
                ObjReaderException.class,
                () -> READER.parseFaceVertex(faceVertex, vertex, texture, normal, 1)
        );
    }

    @Test
    void faceVertexInvalidNumber() {
        String faceVertex = "7/4/2.04";
        List<Integer> vertex = new ArrayList<>();
        List<Integer> texture = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();

        assertThrows(
                ObjReaderException.class,
                () -> READER.parseFaceVertex(faceVertex, vertex, texture, normal, 1)
        );
    }

    @Test
    void faceVertexExtraSlashes() {
        String faceVertex1 = "7///4";
        String faceVertex2 = "7//4/";
        List<Integer> vertex = new ArrayList<>();
        List<Integer> texture = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();

        assertThrows(
                ObjReaderException.class,
                () -> READER.parseFaceVertex(faceVertex1, vertex, texture, normal, 1)
        );
        assertThrows(
                ObjReaderException.class,
                () -> READER.parseFaceVertex(faceVertex2, vertex, texture, normal, 1)
        );
    }

    @Test
    void readFromString() {
        String testStr = """
                o Cube
                v -1.000000 -1.000000 1.000000
                v -1.000000 1.000000 1.000000
                v -1.000000 -1.000000 -1.000000
                v -1.000000 1.000000 -1.000000
                v 1.000000 -1.000000 1.000000
                v 1.000000 1.000000 1.000000
                v 1.000000 -1.000000 -1.000000
                v 1.000000 1.000000 -1.000000
                vn -1.0000 -0.0000 -0.0000
                vn -0.0000 -0.0000 -1.0000
                vn 1.0000 -0.0000 -0.0000
                vn -0.0000 -0.0000 1.0000
                vn -0.0000 -1.0000 -0.0000
                vn -0.0000 1.0000 -0.0000
                vt 0.250000 0.500000
                vt 0.250000 0.250000
                vt 0.500000 0.250000
                vt 0.500000 0.500000
                vt 0.750000 0.250000
                vt 0.500000 0.000000
                vt 0.750000 0.000000
                vt 0.500000 0.750000
                vt 0.500000 1.000000
                vt 0.250000 1.000000
                vt 0.250000 0.750000
                vt 0.000000 0.000000
                vt 0.250000 0.000000
                vt 0.000000 0.250000
                s 0
                f 1/1/1 2/2/1 4/3/1 3/4/1
                f 3/5/2 4/3/2 8/6/2 7/7/2
                f 7/8/3 8/9/3 6/10/3 5/11/3
                f 5/12/4 6/13/4 2/2/4 1/14/4
                f 3/4/5 7/8/5 5/11/5 1/1/5
                f 8/6/6 4/3/6 2/2/6 6/13/6
                """;
        Mesh result = READER.read(testStr);

        List<Vector3f> expectedVertexList = new ArrayList<>();
        expectedVertexList.add(new Vector3f(-1F, -1F, 1F));
        expectedVertexList.add(new Vector3f(-1F, 1F, 1F));
        expectedVertexList.add(new Vector3f(-1F, -1F, -1F));
        expectedVertexList.add(new Vector3f(-1F, 1F, -1F));
        expectedVertexList.add(new Vector3f(1F, -1F, 1F));
        expectedVertexList.add(new Vector3f(1F, 1F, 1F));
        expectedVertexList.add(new Vector3f(1F, -1F, -1F));
        expectedVertexList.add(new Vector3f(1F, 1F, -1F));

        List<Vector3f> expectedNormalList = new ArrayList<>();
        expectedNormalList.add(new Vector3f(-1F, -0F, -0F));
        expectedNormalList.add(new Vector3f(-0F, -0F, -1F));
        expectedNormalList.add(new Vector3f(1F, -0F, -0F));
        expectedNormalList.add(new Vector3f(-0F, -0F, 1F));
        expectedNormalList.add(new Vector3f(-0F, -1F, -0F));
        expectedNormalList.add(new Vector3f(-0F, 1F, -0F));

        List<Vector2f> expectedTextureList = new ArrayList<>();
        expectedTextureList.add(new Vector2f(0.25F, 0.5F));
        expectedTextureList.add(new Vector2f(0.25F, 0.25F));
        expectedTextureList.add(new Vector2f(0.5F, 0.25F));
        expectedTextureList.add(new Vector2f(0.5F, 0.5F));
        expectedTextureList.add(new Vector2f(0.75F, 0.25F));
        expectedTextureList.add(new Vector2f(0.5F, 0F));
        expectedTextureList.add(new Vector2f(0.75F, 0F));
        expectedTextureList.add(new Vector2f(0.5F, 0.75F));
        expectedTextureList.add(new Vector2f(0.5F, 1F));
        expectedTextureList.add(new Vector2f(0.25F, 1F));
        expectedTextureList.add(new Vector2f(0.25F, 0.75F));
        expectedTextureList.add(new Vector2f(0F, 0F));
        expectedTextureList.add(new Vector2f(0.25F, 0F));
        expectedTextureList.add(new Vector2f(0F, 0.25F));

        List<Face> expectedFaceList = new ArrayList<>();
        expectedFaceList.add(new Face());
        expectedFaceList.get(0).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 3, 2)));
        expectedFaceList.get(0).setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
        expectedFaceList.get(0).setNormalIndices(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));

        expectedFaceList.add(new Face());
        expectedFaceList.get(1).setVertexIndices(new ArrayList<>(Arrays.asList(2, 3, 7, 6)));
        expectedFaceList.get(1).setTextureVertexIndices(new ArrayList<>(Arrays.asList(4, 2, 5, 6)));
        expectedFaceList.get(1).setNormalIndices(new ArrayList<>(Arrays.asList(1, 1, 1, 1)));

        expectedFaceList.add(new Face());
        expectedFaceList.get(2).setVertexIndices(new ArrayList<>(Arrays.asList(6, 7, 5, 4)));
        expectedFaceList.get(2).setTextureVertexIndices(new ArrayList<>(Arrays.asList(7, 8, 9, 10)));
        expectedFaceList.get(2).setNormalIndices(new ArrayList<>(Arrays.asList(2, 2, 2, 2)));

        expectedFaceList.add(new Face());
        expectedFaceList.get(3).setVertexIndices(new ArrayList<>(Arrays.asList(4, 5, 1, 0)));
        expectedFaceList.get(3).setTextureVertexIndices(new ArrayList<>(Arrays.asList(11, 12, 1, 13)));
        expectedFaceList.get(3).setNormalIndices(new ArrayList<>(Arrays.asList(3, 3, 3, 3)));

        expectedFaceList.add(new Face());
        expectedFaceList.get(4).setVertexIndices(new ArrayList<>(Arrays.asList(2, 6, 4, 0)));
        expectedFaceList.get(4).setTextureVertexIndices(new ArrayList<>(Arrays.asList(3, 7, 10, 0)));
        expectedFaceList.get(4).setNormalIndices(new ArrayList<>(Arrays.asList(4, 4, 4, 4)));

        expectedFaceList.add(new Face());
        expectedFaceList.get(5).setVertexIndices(new ArrayList<>(Arrays.asList(7, 3, 1, 5)));
        expectedFaceList.get(5).setTextureVertexIndices(new ArrayList<>(Arrays.asList(5, 2, 1, 12)));
        expectedFaceList.get(5).setNormalIndices(new ArrayList<>(Arrays.asList(5, 5, 5, 5)));

        assertEquals(expectedVertexList, result.vertices);
        assertEquals(expectedTextureList, result.textureVertices);
        assertEquals(expectedNormalList, result.normals);
        for (int ind = 0; ind < expectedFaceList.size(); ind++) {
            Face expectedFace = expectedFaceList.get(ind);
            Face resFace = result.faces.get(ind);
            assertEquals(expectedFace.getVertexIndices(), resFace.getVertexIndices());
            assertEquals(expectedFace.getTextureVertexIndices(), resFace.getTextureVertexIndices());
            assertEquals(expectedFace.getNormalIndices(), resFace.getNormalIndices());
        }
    }

    @Test
    void readFromFile() {
        @SuppressWarnings("DataFlowIssue")
        File file = new File(getClass().getResource("/meshes/hammer.obj").getFile());
        assertDoesNotThrow(() -> READER.read(file));
    }
}
