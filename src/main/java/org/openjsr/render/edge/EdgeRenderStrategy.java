package org.openjsr.render.edge;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.render.framebuffer.Framebuffer;

public interface EdgeRenderStrategy {
    void drawTriangleEdges(Vector4f[] vertices, Framebuffer framebuffer);
}
