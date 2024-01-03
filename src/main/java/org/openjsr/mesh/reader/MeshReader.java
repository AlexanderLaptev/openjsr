package org.openjsr.mesh.reader;

import org.openjsr.mesh.Mesh;

import java.io.File;
import java.io.IOException;

public interface MeshReader {
    Mesh read(File file) throws IOException;
}
