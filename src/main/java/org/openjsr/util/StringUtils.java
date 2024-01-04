package org.openjsr.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Утилиты для работы со строками.
 */
public class StringUtils {
    /**
     * Делит строку слов, разделённых любыми непечатаемыми символами (проблемы, табуляции и пр.) на список слов.
     *
     * @param string Исходная строка слов.
     * @return Список строк, разделённых пробелами.
     */
    public static List<String> parseWords(String string) {
        return new ArrayList<>(Arrays.asList(string.split("\\s+")));
    }
}
