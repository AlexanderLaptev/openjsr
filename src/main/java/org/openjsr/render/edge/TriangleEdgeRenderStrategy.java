package org.openjsr.render.edge;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.util.Culler;
import org.openjsr.util.FaceSorter;

import java.util.List;

public class TriangleEdgeRenderStrategy extends DefaultEdgeRenderStrategy {
    @Override
    public void drawModelEdges(Model model, Framebuffer framebuffer) {
        for (Face triangle : model.getMesh().triangles) {
            if (!Culler.shouldTriangleBeCulled(triangle, model, framebuffer)) {
                drawFaceEdges(model.getProjectedVertices(), triangle, framebuffer);
            }
        }
    }
}
