package org.openjsr.mesh.triangulation;

import org.junit.jupiter.api.Test;
import org.openjsr.mesh.Face;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTriangulatorTest {

    @Test
    void triangulatePolygon() {
        Triangulator triangulator = SimpleTriangulator.getInstance();
        Face face = new Face();
        face.setVertexIndices(Arrays.asList(1, 2, 3, 4));
        face.setNormalIndices(Arrays.asList(1, 2, 3, 4));
        face.setTextureVertexIndices(Arrays.asList(1, 2, 3, 4));

        List<Face> triangles = triangulator.triangulateFace(face);

        assertEquals(2, triangles.size());
        assertArrayEquals(new Integer[] {1, 2, 3}, triangles.get(0).getVertexIndices().toArray());
        assertArrayEquals(new Integer[] {1, 3, 4}, triangles.get(1).getVertexIndices().toArray());
        assertArrayEquals(new Integer[] {1, 2, 3}, triangles.get(0).getNormalIndices().toArray());
        assertArrayEquals(new Integer[] {1, 3, 4}, triangles.get(1).getNormalIndices().toArray());
        assertArrayEquals(new Integer[] {1, 2, 3}, triangles.get(0).getTextureVertexIndices().toArray());
        assertArrayEquals(new Integer[] {1, 3, 4}, triangles.get(1).getTextureVertexIndices().toArray());
        assertEquals(3, triangles.get(0).getVertexIndices().size());
        assertEquals(3, triangles.get(1).getVertexIndices().size());
    }

    @Test
    void triangulateList() {
        Face face1 = new Face();
        face1.setVertexIndices(Arrays.asList(1, 2, 3, 4));
        face1.setNormalIndices(Arrays.asList(1, 2, 3, 4));
        face1.setTextureVertexIndices(Arrays.asList(1, 2, 3, 4));

        Face face2 = new Face();
        face2.setVertexIndices(Arrays.asList(5, 6, 7));
        face2.setNormalIndices(Arrays.asList(5, 6, 7));
        face2.setTextureVertexIndices(Arrays.asList(5, 6, 7));

        Triangulator triangulator = SimpleTriangulator.getInstance();

        List<Face> faces = Arrays.asList(face1, face2);
        List<Face> triangles = triangulator.triangulateFaces(faces);

        assertEquals(3, triangles.size());
    }
}
