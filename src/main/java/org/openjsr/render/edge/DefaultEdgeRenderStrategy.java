package org.openjsr.render.edge;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.render.Rasterizer;
import org.openjsr.render.framebuffer.Framebuffer;

public class DefaultEdgeRenderStrategy implements EdgeRenderStrategy {
    private boolean isDepthTestEnabled = true;

    private static final Color DEFAULT_COLOR = Color.fromString("#FFFFFF");

    @Override
    public void drawTriangleEdges(
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            Framebuffer framebuffer
    ) {
        int x1 = (int) vertices[0].x;
        int x2 = (int) vertices[1].x;
        int x3 = (int) vertices[2].x;

        int y1 = (int) vertices[0].y;
        int y2 = (int) vertices[1].y;
        int y3 = (int) vertices[2].y;

        float z1 = vertices[0].z;
        float z2 = vertices[1].z;
        float z3 = vertices[2].z;

        drawEdge(x1, y1, z1, x2, y2, z2, framebuffer);
        drawEdge(x2, y2, z2, x3, y3, z3, framebuffer);
        drawEdge(x1, y1, z1, x3, y3, z3, framebuffer);
    }

    private void drawEdge(
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            Framebuffer framebuffer
    ) {
        if (isDepthTestEnabled) {
            Rasterizer.getInstance().drawDepthTestLine(
                    x1, y1, z1,
                    x2, y2, z2,
                    framebuffer,
                    DEFAULT_COLOR
            );
        } else {
            // За неимением проверенной реализации алгоритма Брезенхема, рисуем через Canvas.
            // TODO: исправить перекрытие гранями (slope.obj, caracal_cube.obj).
            CanvasFramebuffer cfb = (CanvasFramebuffer) framebuffer;
            cfb.getCanvas().getGraphicsContext2D().setStroke(DEFAULT_COLOR);
            cfb.getCanvas().getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
        }
    }
}
