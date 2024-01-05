package org.openjsr.mesh.writer;

/**
 * Исключение, возникающее при сохранении модели в файл {@code .obj}.
 */
public class ObjWriterException extends RuntimeException {
    /**
     * Создаёт исключение с заданным сообщением об ошибке.
     *
     * @param errorMessage Сообщение об ошибке.
     */
    public ObjWriterException(String errorMessage) {
        super("Ошибка ObjWriter: " + errorMessage);
    }
}
