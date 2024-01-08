package org.openjsr.render.edge;

import org.openjsr.mesh.Face;
import org.openjsr.render.Model;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.util.Culler;

public class PolygonEdgeRenderStrategy extends DefaultEdgeRenderStrategy{
    @Override
    public void drawModelEdges(Model model, Framebuffer framebuffer) {
        for (Face face : model.getMesh().faces) {
            if (!Culler.shouldPolygonBeCulled(face, model, framebuffer)) {
                drawFaceEdges(model.getProjectedVertices(), face, framebuffer);
            }
        }
    }
}
