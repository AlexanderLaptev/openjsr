package org.openjsr.render.framebuffer;

/**
 * Буфер пикселей с прямым доступом к цвету.
 */
public interface Framebuffer {
    /**
     * Получает ширину данного буфера в пикселях.
     *
     * @return Ширина данного буфера в пикселях.
     */
    int getWidth();

    /**
     * Получает высоту данного буфера в пикселях.
     *
     * @return Высота данного буфера в пикселях.
     */
    int getHeight();

    /**
     * Устанавливает цвет пикселя в буфере.
     *
     * @param x     x-координата пикселя.
     * @param y     y-координата пикселя.
     * @param color Цвет пикселя.
     */
    void setPixel(int x, int y, int color);
}
