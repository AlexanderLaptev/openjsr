package org.openjsr.model.obj_reader;

/**
 * Класс исключения, используемый в ObjReader
 */
public class ObjReaderException extends RuntimeException{
    /**
     * Создает исключение
     * @param errorMessage сообщение об ошибке
     * @param lineInd номер линии, на которой произошла ошибка
     */
    public ObjReaderException(String errorMessage, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + errorMessage);
    }
}
