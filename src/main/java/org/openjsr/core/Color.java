package org.openjsr.core;

/**
 * Изменяемый класс цвета.
 */
public class Color {
    /**
     * Красная компонента цвета.
     */
    public float red = 0.0f;

    /**
     * Зелёная компонента цвета.
     */
    public float green = 0.0f;

    /**
     * Синяя компонента цвета.
     */
    public float blue = 0.0f;

    /**
     * Альфа-канал цвета.
     */
    public float alpha = 1.0f;

    /**
     * Создаёт непрозрачный чёрный цвет.
     */
    public Color() { }

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
        assert isValid();
    }

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        assert isValid();
    }

    public Color(int red, int green, int blue, int alpha) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
        this.alpha = alpha / 255.0f;
        assert isValid();
    }

    public Color(int red, int green, int blue) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
        assert isValid();
    }

    public int getIntRed() {
        return (int) (255.0f * red);
    }

    public int getIntGreen() {
        return (int) (255.0f * green);
    }

    public int getIntBlue() {
        return (int) (255.0f * blue);
    }

    public int getIntAlpha() {
        return (int) (255.0f * alpha);
    }

    /**
     * Возвращает true, если ни одна компонента цвета не превышает единицы.
     *
     * @return true, если ни одна компонента цвета не превышает единицы.
     */
    public boolean isValid() {
        return red <= 1.0f && green <= 1.0f && blue <= 1.0f && alpha <= 1.0f;
    }

    /**
     * Конвертирует цвет в 32-битное целое в формате ARGB.
     *
     * @return 32-битное целое в формате ARGB, соответствующее данному цвету.
     */
    public int toArgb() {
        assert isValid();
        int a = getIntAlpha();
        int r = getIntRed();
        int g = getIntGreen();
        int b = getIntBlue();
        return a << 24 | r << 16 | g << 8 | b;
    }
}
