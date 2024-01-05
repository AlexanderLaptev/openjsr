package org.openjsr.render.edge;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.PerspectiveCamera;
import org.openjsr.render.Model;
import org.openjsr.render.framebuffer.Framebuffer;

public interface EdgeRenderer {
    void renderEdges(Model model, Vector4f[] projectedVertices, Framebuffer buffer);
}
