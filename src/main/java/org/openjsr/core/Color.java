package org.openjsr.core;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

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
     * Преобразует данное строковое шестнадцатеричное представление цвета в цвет.
     *
     * @param hex Строка из шестнадцатеричного представления цвета в формате
     *            {@code #RRGGBB} или {@code #RRGGBBAA}. Должна начинаться с символа '#'.
     * @return Соответствующий цвет.
     * @throws NumberFormatException Если исходная строка имеет неверный формат.
     */
    public static Color fromString(String hex) {
        if (!hex.startsWith("#")) {
            throw new NumberFormatException("Строка цвета должна начинаться с символа '#'.");
        }
        int number = Integer.parseInt(hex.substring(1), 16);

        Color color = new Color();
        if (hex.length() == 9) { // RGBA
            color.alpha = (float) (number >> 24 & 0xFF);
        } else if (hex.length() != 7) {
            throw new NumberFormatException("Строка цвета имеет неверную длину: " + hex.length() + ".");
        }

        color.red = (float) (number >> 16 & 0xFF) / 255.0f;
        color.green = (float) (number >> 8 & 0xFF) / 255.0f;
        color.blue = (float) (number & 0xFF) / 255.0f;
        return color;
    }

    /**
     * Создаёт цвет по его представлению в виде 32-битного целого в формате ARGB.
     *
     * @param argb 32-битное целочисленное представление цвета в формате ARGB.
     * @return Созданный цвет.
     */
    public static Color fromArgb(int argb) {
        Color result = new Color();
        result.alpha = (argb >> 24 & 0xFF) / 255.0f;
        result.red = (argb >> 16 & 0xFF) / 255.0f;
        result.green = (argb >> 8 & 0xFF) / 255.0f;
        result.blue = (argb & 0xFF) / 255.0f;
        return result;
    }

    /**
     * Создаёт непрозрачный случайный цвет с помощью {@code ThreadLocalRandom.current()}.
     *
     * @return Непрозрачный случайный цвет.
     */
    public static Color getRandomColor() {
        return getRandomColor(ThreadLocalRandom.current());
    }

    /**
     * Создаёт непрозрачный случайный цвет с помощью данного объекта Random.
     *
     * @param random Объект Random.
     * @return Непрозрачный случайный цвет.
     */
    public static Color getRandomColor(Random random) {
        return new Color(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
    }

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
