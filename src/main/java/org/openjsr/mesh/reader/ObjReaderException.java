package org.openjsr.mesh.reader;

/**
 * Исключение, возникающее при чтении полигональной сетки из файла {@code .obj}.
 */
public class ObjReaderException extends RuntimeException {
    /**
     * Создаёт исключение с заданным сообщением об ошибке и номером строки.
     *
     * @param errorMessage Сообщение об ошибке.
     * @param lineInd      Номер строки, на которой произошла ошибка.
     */
    public ObjReaderException(String errorMessage, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage);
    }
}
