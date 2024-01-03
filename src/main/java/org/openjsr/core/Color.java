package org.openjsr.core;

/**
 * Изменяемый класс цвета.
 */
public class Color {
    public float red = 0.0f;

    public float green = 0.0f;

    public float blue = 0.0f;

    public float alpha = 1.0f;

    public Color() {}

    public Color(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public Color(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Color(int red, int green, int blue, int alpha) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
        this.alpha = alpha / 255.0f;
    }

    public Color(int red, int green, int blue) {
        this.red = red / 255.0f;
        this.green = green / 255.0f;
        this.blue = blue / 255.0f;
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

    public int toArgb() {
        int a = getIntAlpha();
        int r = getIntRed();
        int g = getIntGreen();
        int b = getIntBlue();
        return a << 24 | r << 16 | g << 8 | b;
    }
}
