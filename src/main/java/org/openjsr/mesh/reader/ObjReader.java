package org.openjsr.mesh.reader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

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

    /**
     * Множество известных токенов, которые можно игнорировать.
     */
    private static final Set<String> IGNORED_TOKENS = new HashSet<>(Arrays.asList(
            "p", "l", "curv", "curv2", "surf", "g", "s", "mg", "o", "usemtl", "mtllib"
    ));

    private static final String COMMENT_CHARACTER = "#";


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
     * @param string Строка, содержащая представление модельной сетки в формате Wavefront OBJ.
     * @return Сетка, прочитанная из файла.
     * @throws ObjReaderException Если описание модели имеет неподдерживаемый формат.
     */
    public Mesh read(String string) {
        Mesh result = new Mesh();
        int lineNumber = 1;
        Scanner scanner = new Scanner(string);

        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine().trim();
            if (line.startsWith(COMMENT_CHARACTER)) continue; // Игнорируем комментарии.

            List<String> words = StringUtils.parseWords(line);
            if (words.isEmpty()) continue; // Пустые строки игнорируются.

            final String token = words.get(0);
            words.remove(0); // Исключаем токен из списка слов.
            lineNumber++;
            switch (token) {
                case OBJ_VERTEX_TOKEN -> result.vertices.add(parseVector3f(words, lineNumber));
                case OBJ_TEXTURE_TOKEN -> result.textureVertices.add(parseVector2f(words, lineNumber));
                case OBJ_NORMAL_TOKEN -> result.normals.add(parseVector3f(words, lineNumber));
                case OBJ_FACE_TOKEN -> result.faces.add(parseFace(words, lineNumber));
                default -> {
                    if (!IGNORED_TOKENS.contains(token)) {
                        // Если токен неизвестен, кидаем исключение.
                        throw new ObjReaderException(String.format("Неизвестный токен: %s.", token), lineNumber);
                    }
                }
            }
        }

        return result;
    }

    /**
     * Читает трёхмерный вектор (координаты вершины или нормали) из списка его элементов.
     *
     * @param elements Список элементов вектора в строковом представлении.
     * @param line     Номер строки (используется при возникновении ошибок).
     * @return Трёхмерный вектор координат полученной вершины.
     * @throws ObjReaderException Если координата вектора имеет неверный формат
     *                            или недостаточно компонент для создания вектора.
     */
    protected Vector3f parseVector3f(List<String> elements, int line) {
        try {
            return new Vector3f(
                    Float.parseFloat(elements.get(0)),
                    Float.parseFloat(elements.get(1)),
                    Float.parseFloat(elements.get(2))
            );
        } catch (NumberFormatException e) {
            throw new ObjReaderException("Неверный формат вещественного числа.", line);
        } catch (IndexOutOfBoundsException e) {
            throw new ObjReaderException("Слишком мало компонент трёхмерного вектора.", line);
        }
    }

    /**
     * Читает двухмерный вектор текстурной координаты из списка его элементов.
     *
     * @param elements Список элементов вектора в строковом представлении.
     * @param line     Номер строки (используется при возникновении ошибок).
     * @return двухмерный вектор текстурной координаты
     */
    protected Vector2f parseVector2f(List<String> elements, int line) {
        try {
            return new Vector2f(
                    Float.parseFloat(elements.get(0)),
                    Float.parseFloat(elements.get(1))
            );
        } catch (NumberFormatException e) {
            throw new ObjReaderException("Неверный формат вещественного числа.", line);
        } catch (IndexOutOfBoundsException e) {
            throw new ObjReaderException("Слишком мало компонент двухмерного вектора.", line);
        }
    }

    /**
     * Создаёт грань по её вершинам в строковом представлении.
     *
     * @param elements Список элементов грани. Каждый элемент должен иметь формат
     *                 {@code индекс_координаты/индекс_текстурной_вершина/индекс_нормали}.
     * @param line     Номер строки (используется при возникновении ошибок).
     * @return Новая грань, созданная из данных элементов.
     */
    protected Face parseFace(List<String> elements, int line) {
        List<Integer> faceVertexIndices = new ArrayList<>();
        List<Integer> faceTextureIndices = new ArrayList<>();
        List<Integer> faceNormalIndices = new ArrayList<>();

        for (String element : elements) {
            parseFaceVertex(
                    element,
                    faceVertexIndices,
                    faceTextureIndices,
                    faceNormalIndices,
                    line
            );
        }

        Face result = new Face();
        result.setVertexIndices(faceVertexIndices);
        result.setTextureVertexIndices(faceTextureIndices);
        result.setNormalIndices(faceNormalIndices);
        return result;
    }

    /**
     * Добавляет в грань индексы координаты вершины, текстурной координаты
     * и нормали, если они указаны, или использует значения по умолчанию.
     *
     * @param faceVertex         Вершина грани в формате
     *                           {@code индекс_координаты/индекс_текстурной_вершина/индекс_нормали}.
     * @param faceVertexIndices  Список индексов координат вершин в полигоне.
     * @param faceTextureIndices Список индексов текстурных координат в полигоне.
     * @param faceNormalIndices  Список индексов нормалей в полигоне.
     * @param line               Номер строки в файле (используется при возникновении ошибок).
     */
    protected void parseFaceVertex(
            String faceVertex,
            List<Integer> faceVertexIndices,
            List<Integer> faceTextureIndices,
            List<Integer> faceNormalIndices,
            int line
    ) {
        try {
            int firstSlash = faceVertex.indexOf('/');
            if (firstSlash == -1) { // Если слэш отсутствует, пробуем распарсить единственную вершину.
                faceVertexIndices.add(Integer.parseInt(faceVertex) - 1);
            } else {
                int secondSlash = faceVertex.indexOf('/', firstSlash + 1);
                faceVertexIndices.add(Integer.parseInt(faceVertex.substring(0, firstSlash)) - 1);
                if (secondSlash == -1) {
                    // Если второго слэша нет, то предполагаем, что
                    // даны индексы вершины и текстурной координаты.
                    faceTextureIndices.add(Integer.parseInt(faceVertex.substring(firstSlash + 1)) - 1);
                } else {
                    // Если присутствуют оба слэша, то индекс текстурной
                    // вершины может как присутствовать, так и отсутствовать.

                    String textureIndexString = faceVertex.substring(firstSlash + 1, secondSlash);
                    if (!textureIndexString.isEmpty()) {
                        faceTextureIndices.add(Integer.parseInt(faceVertex.substring(firstSlash + 1, secondSlash)) - 1);
                    }

                    String normalIndexString = faceVertex.substring(secondSlash + 1);
                    if (!normalIndexString.isEmpty()) {
                        faceNormalIndices.add(Integer.parseInt(faceVertex.substring(secondSlash + 1)) - 1);
                    }
                }
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new ObjReaderException("Неверный формат вершины грани.", line);
        }
    }
}
