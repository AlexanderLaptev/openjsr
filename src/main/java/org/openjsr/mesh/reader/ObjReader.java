package org.openjsr.mesh.reader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Читает полигональную сетку модели из файла Wavefront OBJ ({@code .obj}).
 */
public class ObjReader implements MeshReader {
    /**
     * Название кодировки, используемой по умолчанию.
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Токен вершины.
     */
    private static final String OBJ_VERTEX_TOKEN = "v";

    /**
     * Токен текстурной вершины (UV-координаты).
     */
    private static final String OBJ_TEXTURE_TOKEN = "vt";

    /**
     * Токен нормали вершины.
     */
    private static final String OBJ_NORMAL_TOKEN = "vn";

    /**
     * Токен описания грани.
     */
    private static final String OBJ_FACE_TOKEN = "f";

    @Override
    public Mesh read(File file) throws IOException {
        return read(file, DEFAULT_CHARSET);
    }

    /**
     * Читает полигональную сетку модели из данного файла {@code .obj}.
     *
     * @param file    Файл с описанием сетки.
     * @param charset Кодировка файла.
     * @return Сетка, прочитанная из файла.
     * @throws IOException При любой ошибке ввода/вывода.
     */
    public Mesh read(File file, String charset) throws IOException {
        return read(new String(Files.readAllBytes(file.toPath()), charset));
    }

    /**
     * Считывает модель из данной строки.
     *
     * @param fileContent Строка, содержащая представление модельной сетки в формате Wavefront OBJ.
     * @return Сетка, прочитанная из файла.
     * @throws ObjReaderException Если описание модели имеет неподдерживаемый формат.
     */
    public Mesh read(String fileContent) {
        Mesh result = new Mesh();

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
                case OBJ_FACE_TOKEN -> result.faces.add(parseFace(wordsInLine, lineInd));
                default -> {
                }
            }
        }

        return result;
    }

    /**
     * Читает трёхмерный вектор (координаты вершины или нормали) из строки.
     *
     * @param wordsInLineWithoutToken Список строк, полученный путём разделения строки из {@code .obj}
     *                                файла по пробелам и удаления оттуда первого элемента.
     *                                Должен иметь в себе три элемента -- (x, y, z).
     * @param lineInd                 Номер строки (используется при возникновении ошибок).
     * @return Трёхмерный вектор координат полученной вершины.
     */
    protected Vector3f parseVector3f(final List<String> wordsInLineWithoutToken, int lineInd) {
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
     * Читает двухмерный вектор текстурной координаты из строки.
     *
     * @param wordsInLineWithoutToken Список строк, полученный путём разделения строки из {@code .obj}
     *                                файла по пробелам и удаления оттуда первого элемента.
     * @param lineInd                 Номер строки (используется при возникновении ошибок).
     * @return двухмерный вектор текстурной координаты
     */
    protected Vector2f parseVector2f(final List<String> wordsInLineWithoutToken, int lineInd) {
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
     * Читает грань из строки.
     *
     * @param wordsInLineWithoutToken Список строк, полученных путём разделения строчки
     *                                из obj файлы по пробелам и удаления оттуда первого элемента.
     *                                Каждая строка в списке (слово) описывает вершину в формате
     *                                {@code индекс_координаты/индекс_текстурной_вершина/индекс_нормали}.
     * @param lineInd                 Номер строки (используется при возникновении ошибок).
     * @return Новая грань, содержащая в себе вершины.
     */
    protected Face parseFace(final List<String> wordsInLineWithoutToken, int lineInd) {
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

        Face result = new Face();
        result.setVertexIndices(onePolygonVertexIndices);
        result.setTextureVertexIndices(onePolygonTextureVertexIndices);
        result.setNormalIndices(onePolygonNormalIndices);
        return result;
    }

    /**
     * Добавляет в грань индексы координаты вершины, текстурной координаты и нормали, если они есть в слове.
     *
     * @param wordInLine                     Строка в формате индекс_координаты/индекс_текстурной_вершина/индекс_нормали.
     * @param onePolygonVertexIndices        Список индексов координат вершин в полигоне.
     * @param onePolygonTextureVertexIndices Список индексов текстурных коородинат в полигоне.
     * @param onePolygonNormalIndices        Список индексов нормалей в полигоне.
     * @param lineInd                        Номер строки в файле (используется при возникновении ошибок).
     */
    protected void parseFaceWord(
            String wordInLine,
            List<Integer> onePolygonVertexIndices,
            List<Integer> onePolygonTextureVertexIndices,
            List<Integer> onePolygonNormalIndices,
            int lineInd) {

        try {
            String[] wordIndices = wordInLine.split("/");
            switch (wordIndices.length) {
                case 1 -> onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
                case 2 -> onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
                case 3 -> {
                    onePolygonVertexIndices.add(Integer.parseInt(wordIndices[0]) - 1);
                    onePolygonTextureVertexIndices.add(Integer.parseInt(wordIndices[1]) - 1);
                    onePolygonNormalIndices.add(Integer.parseInt(wordIndices[2]) - 1);
                }
                default -> throw new ObjReaderException("Invalid element size.", lineInd);
            }
        } catch (NumberFormatException e) {
            throw new ObjReaderException("Failed to parse int value.", lineInd);
        } catch (IndexOutOfBoundsException e) {
            throw new ObjReaderException("Too few arguments.", lineInd);
        }
    }
}
