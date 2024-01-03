package org.openjsr.model.triangulation;

import org.junit.jupiter.api.Test;
import org.openjsr.model.Polygon;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleTriangulatorTest {

    @Test
    void triangulatePolygon() {
        Triangulator triangulator = new SimpleTriangulator();
        Polygon polygon = new Polygon();
        polygon.setVertexIndices(Arrays.asList(1, 2, 3, 4));
        polygon.setNormalIndices(Arrays.asList(1, 2, 3, 4));
        polygon.setTextureVertexIndices(Arrays.asList(1, 2, 3, 4));

        List<Polygon> triangles = triangulator.triangulatePolygon(polygon);

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
        Polygon polygon1 = new Polygon();
        polygon1.setVertexIndices(Arrays.asList(1, 2, 3, 4));
        polygon1.setNormalIndices(Arrays.asList(1, 2, 3, 4));
        polygon1.setTextureVertexIndices(Arrays.asList(1, 2, 3, 4));

        Polygon polygon2 = new Polygon();
        polygon2.setVertexIndices(Arrays.asList(5, 6, 7));
        polygon2.setNormalIndices(Arrays.asList(5, 6, 7));
        polygon2.setTextureVertexIndices(Arrays.asList(5, 6, 7));

        Triangulator triangulator = new SimpleTriangulator();

        List<Polygon> polygons = Arrays.asList(polygon1, polygon2);
        List<Polygon> triangles = triangulator.triangulateList(polygons);

        assertEquals(3, triangles.size());
    }
}