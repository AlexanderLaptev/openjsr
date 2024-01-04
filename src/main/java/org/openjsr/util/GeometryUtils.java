package org.openjsr.util;

import cg.vsu.render.math.vector.Vector2f;

/**
 * Утилиты для работы с простой геометрией
 */
public class GeometryUtils {

    /**
     * Решает систему уравнений барицентрических координат методом Крамера и возвращает массив барицентрических координат (alpha, beta, gamma)
     * Углы треугольника будут принимать значения (1, 0, 0), (0, 1, 0), (0, 0, 1) в зависимости от порядка их задания.
     * @param dotX x точки, координаты которой нужно найти
     * @param dotY y точки, координаты которой нужно найти
     * @param triangle массив двумерных векторов координат вершин треугольника (задан по часовой стрелке)
     * @return float[] массив барицентрических координат (alpha, beta, gamma)
     */
    public static float[] getBarycentricCoords(float dotX, float dotY, Vector2f[] triangle) {
        float aX = triangle[0].x;
        float bX = triangle[1].x;
        float cX = triangle[2].x;
        float aY = triangle[0].y;
        float bY = triangle[1].y;
        float cY = triangle[2].y;

        float denominator = (bY - cY) * (aX - cX) + (cX - bX) * (aY - cY);
        float alpha = ((bY - cY) * (dotX - cX) + (cX - bX) * (dotY - cY)) / denominator;
        float beta = ((cY - aY) * (dotX - cX) + (aX - cX) * (dotY - cY)) / denominator;
        float gamma = 1.0f - alpha - beta;

        return new float[]{Math.abs(alpha), Math.abs(beta), Math.abs(gamma)};
    }

    /**
     * @param point двумерный вектор координат точки, барицентрические координаты которой нужно найти
     * @param triangle массив двумерных векторов координат вершин треугольника
     * @return массив барицентрических координат (alpha, beta, gamma)
     */
    public static float[] getBarycentricCoords(Vector2f point, Vector2f[] triangle) {
        return getBarycentricCoords(point.x, point.y, triangle);
    }
}
