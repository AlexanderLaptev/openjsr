package org.openjsr.model.obj_reader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.model.Model;
import org.openjsr.model.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjReaderTest {

    @Test
    void parseVector2f() {
        String testStr = "vt 1.000000 0.500000";
        List<String> words = new ArrayList<>((List.of(testStr.split(" "))));
        words.remove(0);
        Vector2f result = ObjReader.parseVector2f(words, 0);
        Vector2f expected = new Vector2f(1.000000F, 0.500000F);
        assertEquals(expected, result);
    }

    @Test
    void parseVector3f() {
        String testStr = "v 0.000000 0.023752 -0.277450";
        List<String> words = new ArrayList<>((List.of(testStr.split(" "))));
        words.remove(0);
        Vector3f result = ObjReader.parseVector3f(words, 0);
        Vector3f expected = new Vector3f(0.000000F, 0.023752F, -0.277450F);
        assertEquals(expected, result);
    }

    @Test
    void parseFaceWord() {
        String wordInLine = "1/2/3";
        List<Integer> vertexList = new ArrayList<>();
        List<Integer> textureVertexList = new ArrayList<>();
        List<Integer> normalList = new ArrayList<>();
        int lineInd = 0;

        ObjReader.parseFaceWord(
                wordInLine,
                vertexList,
                textureVertexList,
                normalList,
                lineInd);

        assertEquals(0, vertexList.get(0));
        assertEquals(1, textureVertexList.get(0));
        assertEquals(2, normalList.get(0));
    }

    @Test
    void parseFace() {
        String testStr = "f 1/27/13 3/28/13 5/29/13 7/30/13 9/31/13 11/32/13 13/33/13 15/34/13 17/35/13 19/36/13 21/37/13 23/38/13";
        List<String> words = new ArrayList<>((List.of(testStr.split(" "))));
        words.remove(0);

        Polygon result = ObjReader.parseFace(words, 0);
        Polygon expected = new Polygon();
        expected.setVertexIndices(new ArrayList<>(Arrays.asList(0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22)));
        expected.setTextureVertexIndices(new ArrayList<>(Arrays.asList(26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37)));
        expected.setNormalIndices(new ArrayList<>(Arrays.asList(12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12)));
        assertEquals(expected.getVertexIndices(), result.getVertexIndices());
        assertEquals(expected.getTextureVertexIndices(), result.getTextureVertexIndices());
        assertEquals(expected.getNormalIndices(), result.getNormalIndices());
    }

    @Test
    void read() {
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
        Model result = ObjReader.read(testStr);

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
        expectedTextureList.add(new Vector2f(0.25F,  0.5F));
        expectedTextureList.add(new Vector2f(0.25F,  0.25F));
        expectedTextureList.add(new Vector2f(0.5F,  0.25F));
        expectedTextureList.add(new Vector2f(0.5F,  0.5F));
        expectedTextureList.add(new Vector2f(0.75F,  0.25F));
        expectedTextureList.add(new Vector2f(0.5F,  0F));
        expectedTextureList.add(new Vector2f(0.75F,  0F));
        expectedTextureList.add(new Vector2f(0.5F,  0.75F));
        expectedTextureList.add(new Vector2f(0.5F,  1F));
        expectedTextureList.add(new Vector2f(0.25F,  1F));
        expectedTextureList.add(new Vector2f(0.25F,  0.75F));
        expectedTextureList.add(new Vector2f(0F,  0F));
        expectedTextureList.add(new Vector2f(0.25F,  0F));
        expectedTextureList.add(new Vector2f(0F,  0.25F));

        List<Polygon> expectedPolygonList = new ArrayList<>();
        expectedPolygonList.add(new Polygon());
        expectedPolygonList.get(0).setVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 3, 2)));
        expectedPolygonList.get(0).setTextureVertexIndices(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
        expectedPolygonList.get(0).setNormalIndices(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));

        expectedPolygonList.add(new Polygon());
        expectedPolygonList.get(1).setVertexIndices(new ArrayList<>(Arrays.asList(2, 3, 7, 6)));
        expectedPolygonList.get(1).setTextureVertexIndices(new ArrayList<>(Arrays.asList(4, 2, 5, 6)));
        expectedPolygonList.get(1).setNormalIndices(new ArrayList<>(Arrays.asList(1, 1, 1, 1)));

        expectedPolygonList.add(new Polygon());
        expectedPolygonList.get(2).setVertexIndices(new ArrayList<>(Arrays.asList(6, 7, 5, 4)));
        expectedPolygonList.get(2).setTextureVertexIndices(new ArrayList<>(Arrays.asList(7, 8, 9, 10)));
        expectedPolygonList.get(2).setNormalIndices(new ArrayList<>(Arrays.asList(2, 2, 2, 2)));

        expectedPolygonList.add(new Polygon());
        expectedPolygonList.get(3).setVertexIndices(new ArrayList<>(Arrays.asList(4, 5, 1, 0)));
        expectedPolygonList.get(3).setTextureVertexIndices(new ArrayList<>(Arrays.asList(11, 12, 1, 13)));
        expectedPolygonList.get(3).setNormalIndices(new ArrayList<>(Arrays.asList(3, 3, 3, 3)));

        expectedPolygonList.add(new Polygon());
        expectedPolygonList.get(4).setVertexIndices(new ArrayList<>(Arrays.asList(2, 6, 4, 0)));
        expectedPolygonList.get(4).setTextureVertexIndices(new ArrayList<>(Arrays.asList(3, 7, 10, 0)));
        expectedPolygonList.get(4).setNormalIndices(new ArrayList<>(Arrays.asList(4, 4, 4, 4)));

        expectedPolygonList.add(new Polygon());
        expectedPolygonList.get(5).setVertexIndices(new ArrayList<>(Arrays.asList(7, 3, 1, 5)));
        expectedPolygonList.get(5).setTextureVertexIndices(new ArrayList<>(Arrays.asList(5, 2, 1, 12)));
        expectedPolygonList.get(5).setNormalIndices(new ArrayList<>(Arrays.asList(5, 5, 5, 5)));

        assertEquals(expectedVertexList, result.vertices);
        assertEquals(expectedTextureList, result.textureVertices);
        assertEquals(expectedNormalList, result.normals);
        for (int ind = 0; ind < expectedPolygonList.size(); ind++) {
            Polygon expectedPolygon = expectedPolygonList.get(ind);
            Polygon resPolygon = result.polygons.get(ind);
            assertEquals(expectedPolygon.getVertexIndices(), resPolygon.getVertexIndices());
            assertEquals(expectedPolygon.getTextureVertexIndices(), resPolygon.getTextureVertexIndices());
            assertEquals(expectedPolygon.getNormalIndices(), resPolygon.getNormalIndices());
        }
    }
}