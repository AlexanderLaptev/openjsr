package org.openjsr.model.obj_reader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.model.Model;
import org.openjsr.model.Polygon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ObjReader {
    private static final String OBJ_VERTEX_TOKEN = "v";
    private static final String OBJ_TEXTURE_TOKEN = "vt";
    private static final String OBJ_NORMAL_TOKEN = "vn";
    private static final String OBJ_FACE_TOKEN = "f";

    /**
     * Считывает модель из текста
     * @param fileContent исходный текст в формате obj файла
     * @return готовую модель из файла
     */
    public static Model read(String fileContent) {
        Model result = new Model();

        int lineInd = 0;
        Scanner scanner = new Scanner(fileContent);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            List<String> wordsInLine = new ArrayList<>(Arrays.asList(line.split("\\s+")));
            if (wordsInLine.isEmpty()) {
                continue;
            }

            final String token = wordsInLine.get(0);
            wordsInLine.remove(0);

            ++lineInd;
            switch (token) {
                case OBJ_VERTEX_TOKEN -> result.vertices.add(parseVector3f(wordsInLine, lineInd));
                case OBJ_TEXTURE_TOKEN -> result.textureVertices.add(parseVector2f(wordsInLine, lineInd));
                case OBJ_NORMAL_TOKEN -> result.normals.add(parseVector3f(wordsInLine, lineInd));
                case OBJ_FACE_TOKEN -> result.polygons.add(parseFace(wordsInLine, lineInd));
                default -> {
                }
            }
        }

        return result;
    }

    /**
     * Создает трехмерный вектор (координаты вершины или нормали)
     *
     * @param wordsInLineWithoutToken список строк, полученный путем разделения строчки из obj файла по пробелам и удаления оттуда первого элемента. Должен иметь в себе три элемента (x, y, z)
     * @param lineInd                 номер строки (используется при возникновении ошибок)
     * @return трехмерный вектор координат полученной вершины
     */
    protected static Vector3f parseVector3f(final List<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector3f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1)),
                    Float.parseFloat(wordsInLineWithoutToken.get(2)));

        } catch (NumberFormatException e) {
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch (IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few arguments.", lineInd);
        }
    }

    /**
     * Создает двухмерный вектор текстурной координаты.
     *
     * @param wordsInLineWithoutToken список строк, полученный путем разделения строчки из obj файла по пробелам и удаления оттуда первого элемента.
     * @param lineInd                 номер строки (используется при возникновении ошибок)
     * @return двухмерный вектор текстурной координаты
     */
    protected static Vector2f parseVector2f(final List<String> wordsInLineWithoutToken, int lineInd) {
        try {
            return new Vector2f(
                    Float.parseFloat(wordsInLineWithoutToken.get(0)),
                    Float.parseFloat(wordsInLineWithoutToken.get(1))
            );

        } catch (NumberFormatException e) {
            throw new ObjReaderException("Failed to parse float value.", lineInd);

        } catch (IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few arguments.", lineInd);
        }
    }

    /**
     * Парсит строку в полигон
     *
     * @param wordsInLineWithoutToken список строк, полученных путем разделения строчки
     *                                из obj файлы по пробелам и удаления оттуда первого элемента.
     *                                каждая строка в списке (слово) описывает вершину в формате
     *                                индекс_координаты/индекс_текстурной_вершина/индекс_нормали
     * @param lineInd                 номер строки (используется при возникновении ошибок)
     * @return новый полигон, содержащий в себе вершины
     */
    protected static Polygon parseFace(final List<String> wordsInLineWithoutToken, int lineInd) {
        List<Integer> onePolygonVertexIndices = new ArrayList<>();
        List<Integer> onePolygonTextureVertexIndices = new ArrayList<>();
        List<Integer> onePolygonNormalIndices = new ArrayList<>();

        for (String s : wordsInLineWithoutToken) {
            parseFaceWord(
                    s,
                    onePolygonVertexIndices,
                    onePolygonTextureVertexIndices,
                    onePolygonNormalIndices,
                    lineInd);
        }

        Polygon result = new Polygon();
        result.setVertexIndices(onePolygonVertexIndices);
        result.setTextureVertexIndices(onePolygonTextureVertexIndices);
        result.setNormalIndices(onePolygonNormalIndices);
        return result;
    }

    /**
     * Добавляет в полигон индексы координаты вершины, текстурной координаты и нормали, если они есть в слове.
     *
     * @param wordInLine                     строка в формате индекс_координаты/индекс_текстурной_вершина/индекс_нормали
     * @param onePolygonVertexIndices        список индексов координат вершин в полигоне
     * @param onePolygonTextureVertexIndices список индексов текстурных коородинат в полигоне
     * @param onePolygonNormalIndices        список индексов нормалей в полигоне
     * @param lineInd                        номер строки в файле (используется при возникновении ошибок)
     */
    protected static void parseFaceWord(
            String wordInLine,
            List<Integer> onePolygonVertexIndices,
            List<Integer> onePolygonTextureVertexIndices,
            List<Integer> onePolygonNormalIndices,
            int lineInd) {

        try {
            String[] wordIndices = wordInLine.split("/");
            switch (wordIndices.length) {
                case 1 -> {
                    onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
                }
                case 2 -> {
                    onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
                }
                case 3 -> {
                    onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
                    onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
                    onePolygonNormalIndices.add(Integer.parseInt(wordIndices[2]) - 1);
                }
                default -> {
                    throw new ObjReaderException("Invalid element size.", lineInd);
                }
            }

        } catch (NumberFormatException e) {
            throw new ObjReaderException("Failed to parse int value.", lineInd);

        } catch (IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few arguments.", lineInd);
        }
    }
}
