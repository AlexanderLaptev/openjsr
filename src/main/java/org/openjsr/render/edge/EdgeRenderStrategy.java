package org.openjsr.render.edge;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;
import org.openjsr.render.framebuffer.Framebuffer;

public interface EdgeRenderStrategy {

    void drawModelEdges(
            Model model,
            Framebuffer framebuffer
    );

    void drawFaceEdges(
            Vector4f[] vertices,
            Face face,
            Framebuffer framebuffer
    );

    boolean isDepthTestEnabled();

    void setDepthTestEnabled(boolean depthTestEnabled);
}
