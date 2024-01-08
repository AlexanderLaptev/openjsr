package org.openjsr.render.edge;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;
import org.openjsr.render.Rasterizer;
import org.openjsr.render.framebuffer.Framebuffer;

public abstract class DefaultEdgeRenderStrategy implements EdgeRenderStrategy {
    private boolean isDepthTestEnabled = true;

    private static final Color DEFAULT_COLOR = Color.fromString("#FFFFFF");

    @Override
    public void drawFaceEdges(Vector4f[] vertices, Face face, Framebuffer framebuffer) {
        for (int ind = 0; ind < face.getVertexIndices().size() - 1; ind++) {
            Vector4f v1 = vertices[face.getVertexIndices().get(ind)];
            Vector4f v2 = vertices[face.getVertexIndices().get(ind + 1)];
            drawEdge(
                    (int) v1.x, (int) v1.y, v1.z,
                    (int) v2.x, (int) v2.y, v2.z,
                    framebuffer
            );
        }
        Vector4f v1 = vertices[face.getVertexIndices().get(0)];
        Vector4f v2 = vertices[face.getVertexIndices().get(face.getVertexIndices().size() - 1)];
        drawEdge(
                (int) v1.x, (int) v1.y, v1.z,
                (int) v2.x, (int) v2.y, v2.z,
                framebuffer
        );
    }


    protected void drawEdge(
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            Framebuffer framebuffer
    ) {
        Rasterizer.getInstance().drawLine(
                x1, y1, z1,
                x2, y2, z2,
                isDepthTestEnabled,
                framebuffer,
                DEFAULT_COLOR
        );
    }

    @Override
    public boolean isDepthTestEnabled() {
        return isDepthTestEnabled;
    }

    @Override
    public void setDepthTestEnabled(boolean depthTestEnabled) {
        isDepthTestEnabled = depthTestEnabled;
    }
}
