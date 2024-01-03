package org.openjsr.mesh.reader;

import org.openjsr.mesh.Mesh;

import java.io.File;

public interface MeshReader {
    Mesh read(File file);
}
