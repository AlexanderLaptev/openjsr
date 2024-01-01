package org.openjsr.util;

import java.util.Random;

/**
 * Утилиты для работы с цветами.
 */
public class ColorUtils {
    /**
     * Преобразует данное строковое шестнадцатиричное представление цвета в цвет в формате ARGB.
     *
     * @param hex Строка из шестнадцатиричного представления цвета в
     *            формате {@code #RRGGBB}. Должна начинаться с символа '#'.
     * @return Цвет в формате ARGB.
     * @throws NumberFormatException Если исходная строка имеет неверный формат.
     */
    public static int hexToArgb(String hex) {
        if (!hex.startsWith("#") || hex.length() != 7) {
            throw new NumberFormatException("Строка имеет неверный формат.");
        }

        int number = Integer.parseInt(hex.substring(1), 16);
        int red = number >> 16 & 0xFF;
        int green = number >> 8 & 0xFF;
        int blue = number & 0xFF;
        return 255 << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * Получает случайный цвет в формате ARGB.
     *
     * @param random Объект Random.
     * @return Случайный цвет в формате ARGB.
     */
    public static int getRandomColor(Random random) {
        int red = random.nextInt(0, 256);
        int green = random.nextInt(0, 256);
        int blue = random.nextInt(0, 256);
        return 255 << 24 | red << 16 | green << 8 | blue;
    }
}
