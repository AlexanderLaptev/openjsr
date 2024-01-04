package org.openjsr.render;

import org.openjsr.core.PerspectiveCamera;

import java.util.Arrays;

/**
 * Удобная обертка для z-buffer. Содержит матрицу float значений z.
 */
public class Zbuffer {
    /**
     * Создает экземпляр z-buffer с заданными шириной и высотой и заполняет его максимальными значениями float.
     * @param width заданная ширина
     * @param height заданная высота
     */
    public Zbuffer(int width, int height) {
        this.buffer = new float[height][width];

        for (float[] row : buffer) {
            Arrays.fill(row, 1F);
        }
    }

    public Zbuffer(float[][] buffer) {
        this.buffer = buffer;
    }

    /**
     * Матрица z значений в каждом пикселе. По умолчанию выставляются максимальные значения float, т.к. чем дальше объект - тем больше его z.
     */
    private float[][] buffer;

    public float[][] getBuffer() {
        return buffer;
    }

    public void setBuffer(float[][] buffer) {
        this.buffer = buffer;
    }

    /**
     * Возвращает значение z в пикселе с заданными x и y.
     * @param x заданный x (по горизонтали)
     * @param y заданный y (по вертикали)
     * @return float значение предыдущего z в этой точке или 1F, если до этого в точке ничего не было
     * (т.к. 1F это расстояние до {@link PerspectiveCamera#farPlane} после всех преобразований координат)
     */
    public float getZ(int x, int y) {
        return buffer[y][x];
    }

    /**
     * Устанавливает новое значение глубины в пикселе  (x, y)
     * @param x заданный x пикселя
     * @param y заданный y пикселя
     * @param z значение глубины в этом пикселе
     */
    public void setZ(int x, int y, float z) {
        buffer[y][x] = z;
    }

    /**
     * Показывает, видима ли точка с заданной глубиной в данном пикселе.
     * Так же рассматривает ограничение (-1F; 1F) по z между {@link PerspectiveCamera#nearPlane} и {@link PerspectiveCamera#farPlane}.
     * @param x заданный x пикселя
     * @param y заданный y пикселя
     * @param z заданная глубина пикселя
     * @return true, если эту точку ничто не перекрывает, false, если есть объект перед ним
     */
    public boolean isVisible(int x, int y, float z) {
        return (z <= getZ(x, y)) && (z <= 1F) && (z >= -1F);
    }
}
