package org.openjsr.mesh.reader;

import org.openjsr.mesh.Mesh;

import java.io.File;
import java.io.IOException;

/**
 * MeshReader читает полигональную сетку из файла.
 */
public interface MeshReader {
    /**
     * Читает полигональную сетку из данного файла.
     *
     * @param file Файл с описанием сетки.
     * @return Сетка, прочитанная из файла.
     * @throws IOException При любой ошибке ввода/вывода.
     */
    Mesh read(File file) throws IOException;
}
