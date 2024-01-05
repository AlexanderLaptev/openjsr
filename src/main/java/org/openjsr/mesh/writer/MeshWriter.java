package org.openjsr.mesh.writer;

import org.openjsr.mesh.Mesh;

import java.io.File;

/**
 * MeshWriter записывает полигональную сетку в файл.
 */
public interface MeshWriter {
    /**
     * Записывает данную полигональную сетку в заданный файл.
     *
     * @param mesh Сетка, которую нужно записать.
     * @param file Файл, в который будет записана сетка.
     */
    void write(Mesh mesh, File file);
}
