package org.openjsr.mesh.reader;

/**
 * Исключение, возникающее при чтении полигональной сетки из файла {@code .obj}.
 */
public class ObjReaderException extends RuntimeException {
    /**
     * Создаёт исключение с заданным сообщением об ошибке и номером строки.
     *
     * @param message Сообщение об ошибке.
     * @param line    Номер строки, на которой произошла ошибка.
     */
    public ObjReaderException(String message, int line) {
        super(String.format("Ошибка при чтении модели из файла .obj на строке %d: %s.", line, message));
    }
}
