package org.openjsr.model.reader;

import org.openjsr.model.Model;

import java.io.File;

public interface ModelReader {
    Model read(File file);
}
