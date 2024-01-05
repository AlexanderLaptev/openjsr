package org.openjsr.render;

import java.util.Arrays;

/**
 * Буфер глубины (z-буфер, z-buffer). Используется при отрисовке для сравнения глубины двух точек.
 */
public class DepthBuffer {
    /**
     * Создаёт экземпляр буфера глубины с заданными шириной и высотой и заполняет его единицами,
     * так как это максимальное расстояние до дальней плоскости проецирования (far plane).
     *
     * @param width  Ширина буфера.
     * @param height Высота буфера.
     */
    public DepthBuffer(int width, int height) {
        this.buffer = new float[height * width];
        this.height = height;
        this.width = width;
        Arrays.fill(buffer, 1f);
    }

    /**
     * Массив значений глубины (координаты z) для каждого пикселя.
     */
    private float[] buffer;

    /**
     * Ширина буфера, максимальное значение координаты x.
     */
    private final int width;

    /**
     * Высота буфера, максимальное значение координаты y.
     */
    private final int height;

    /**
     * Возвращает глубину (координату z) в пикселе с заданными координатами x и y.
     * В результате проекции все возможные значения глубины будут лежать в диапазоне
     * от -1 до 1 включительно.
     *
     * @param x Координата x точки в буфере.
     * @param y Координата y точки в буфере.
     * @return Значение глубины в пикселе с заданными координатами.
     */
    public float getZ(int x, int y) {
        return buffer[y * width + x];
    }

    /**
     * Устанавливает новое значение глубины в пикселе с заданными координатами x и y.
     *
     * @param x Координата x точки в буфере.
     * @param y Координата y точки в буфере.
     * @param z Значение глубины в пикселе с заданными координатами.
     */
    public void setZ(int x, int y, float z) {
        buffer[y * width + x] = z;
    }

    /**
     * Возвращает true, если точка с заданной глубиной находится ближе,
     * чем точка с заданными координатами x и y.
     *
     * @param x Координата x точки в буфере.
     * @param y Координата y точки в буфере.
     * @param z Значение глубины данной точки.
     * @return true, если эту данная точка находится ближе точки буфера, иначе false.
     */
    public boolean isVisible(int x, int y, float z) {
        if (x < width && y < height) return z <= getZ(x, y);
        return false;
    }
}
