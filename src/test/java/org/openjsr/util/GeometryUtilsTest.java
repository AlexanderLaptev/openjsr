package org.openjsr.util;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeometryUtilsTest {

    @Test
    void getBarycentricCoords() {
        Vector2f[] rightTriangle = new Vector2f[]{
                new Vector2f(1, 0),
                new Vector2f(1, 1),
                new Vector2f(0, 1)
        };

        //Проверка вершин
        Vector2f point1 = new Vector2f(1, 0);
        Vector2f point2 = new Vector2f(1, 1);
        Vector2f point3 = new Vector2f(0, 1);

        float[] expected1 = new float[]{1f, 0f, 0f};
        float[] expected2 = new float[]{0f, 1f, 0f};
        float[] expected3 = new float[]{0f, 0f, 1f};

        assertArrayEquals(expected1, GeometryUtils.getBarycentricCoords(point1, rightTriangle));
        assertArrayEquals(expected2, GeometryUtils.getBarycentricCoords(point2, rightTriangle));
        assertArrayEquals(expected3, GeometryUtils.getBarycentricCoords(point3, rightTriangle));

        //Проверка внутренних точек
        Vector2f[] equilateralTriangle = new Vector2f[]{
                new Vector2f(-1.5f, 3f),
                new Vector2f(1.5f, 3f),
                new Vector2f(0f, 0f),
        };
        Vector2f point4 = new Vector2f(0f, 2f);
        Vector2f point5 = new Vector2f(0.75f, 1.5f);

        float[] expected4 = new float[]{(float) 1 / 3, (float) 1 / 3, (float) 1 / 3};
        float[] expected5 = new float[]{0f, (float) 1 / 2, (float) 1 / 2};
        assertArrayEquals(expected4, GeometryUtils.getBarycentricCoords(point4, equilateralTriangle), 10E-6F);
        assertArrayEquals(expected5, GeometryUtils.getBarycentricCoords(point5, equilateralTriangle), 10E-6F);
    }

    @Test
    void angle() {
        Vector3f v1 = new Vector3f(0, 0, 1);
        Vector3f v2 = new Vector3f(0, 1, 0);
        assertEquals(90, GeometryUtils.angle(v1, v2));
        assertEquals(90, GeometryUtils.angle(v2, v1));

        v1 = new Vector3f(0, 1, 1);
        v2 = new Vector3f(0, 0, 1);
        assertEquals(45, GeometryUtils.angle(v1, v2));
    }
}