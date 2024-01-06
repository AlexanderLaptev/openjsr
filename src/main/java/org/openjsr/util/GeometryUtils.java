package org.openjsr.util;

import cg.vsu.render.math.vector.Vector2f;

/**
 * Утилиты для работы с простой геометрией
 */
public class GeometryUtils {

    /**
     * @param point    двумерный вектор координат точки, барицентрические координаты которой нужно найти
     * @param triangle массив двумерных векторов координат вершин треугольника
     * @return массив барицентрических координат (alpha, beta, gamma)
     */
    public static float[] getBarycentricCoords(Vector2f point, Vector2f[] triangle) {
        return getBarycentricCoords(point.x, point.y, triangle);
    }

    /**
     * @param dotX     x точки, координаты которой нужно найти
     * @param dotY     y точки, координаты которой нужно найти
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

        return getBarycentricCoords(
                dotX, dotY,
                aX, aY,
                bX, bY,
                cX, cY
        );
    }

    /**
     * Решает систему уравнений барицентрических координат методом Крамера и возвращает массив барицентрических координат (alpha, beta, gamma)
     * Углы треугольника будут принимать значения (1, 0, 0), (0, 1, 0), (0, 0, 1) в зависимости от порядка их задания.
     *
     * @param dotX x точки, координаты который нужно найти
     * @param dotY y точки, координаты которой нужно найти
     * @param aX   x веришны A
     * @param aY   y веришны A
     * @param bX   x веришны B
     * @param bY   y веришны B
     * @param cX   x веришны C
     * @param cY   y веришны C
     * @return float[] массив барицентрических координат (alpha, beta, gamme)
     */
    public static float[] getBarycentricCoords(
            float dotX, float dotY,
            float aX, float aY,
            float bX, float bY,
            float cX, float cY
    ) {
        float denominator = (bY - cY) * (aX - cX) + (cX - bX) * (aY - cY);
        float alpha = ((bY - cY) * (dotX - cX) + (cX - bX) * (dotY - cY)) / denominator;
        float beta = ((cY - aY) * (dotX - cX) + (aX - cX) * (dotY - cY)) / denominator;
        float gamma = 1.0f - alpha - beta;

        return new float[]{Math.abs(alpha), Math.abs(beta), Math.abs(gamma)};
    }

    /**
     * Интерполирует значения по барицентрическим координатам
     *
     * @param cords  барицентрические координаты
     * @param values значения в вершинах
     * @return единственное интерполированное значение
     */
    public static float interpolate(float[] cords, float[] values) {
        return cords[0] * values[0] + cords[1] * values[1] + cords[2] * values[2];
    }

    public static float interpolate(
            int x, int y,
            int x1, int y1, float z1,
            int x2, int y2, float z2
    ) {
        return (float) (z1 + (z2 - z1) * Math.sqrt(
                (Math.pow(x - x1, 2) + Math.pow(y - y1, 2))
                        / (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))
        ));
    }
}
