package org.openjsr.util;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;
import org.openjsr.render.framebuffer.Framebuffer;

public class Culler {
    public static boolean shouldTriangleBeCulled(Face triangle, Model model, Framebuffer framebuffer) {
        Vector4f v1 = model.getProjectedVertices()[triangle.getVertexIndices().get(0)];
        Vector4f v2 = model.getProjectedVertices()[triangle.getVertexIndices().get(1)];
        Vector4f v3 = model.getProjectedVertices()[triangle.getVertexIndices().get(2)];

        boolean shouldCutByX = (v1.x < 0 && v2.x < 0 && v3.x < 0) ||
                (v1.x > framebuffer.getWidth() && v2.x > framebuffer.getWidth() && v3.x > framebuffer.getWidth());
        boolean shouldCutByY = (v1.y < 0 && v2.y < 0 && v3.y < 0) ||
                (v1.y > framebuffer.getHeight() && v2.y > framebuffer.getHeight() && v3.y > framebuffer.getHeight());
        boolean shouldCutByZ = (v1.w < 0 || v2.w < 0 || v3.w < 0);

        return shouldCutByX || shouldCutByY || shouldCutByZ;
    }

    public static boolean shouldPolygonBeCulled(Face polygon, Model model, Framebuffer framebuffer) {
        boolean cutByXLeft = true;
        boolean cutByXRight = true;
        boolean cutByYUp = true;
        boolean cutByYDown = true;

        for (int ind = 0; ind < polygon.getVertexIndices().size(); ind++) {
            Vector4f vertex = model.getProjectedVertices()[polygon.getVertexIndices().get(ind)];

            if (vertex.w < 0) return true; // если любая точка вышла за пределы рендера камеры по z, бракуем полигон

            cutByXRight = cutByXRight && (vertex.x > framebuffer.getWidth());
            cutByXLeft = cutByXLeft && (vertex.x < 0);

            cutByYUp = cutByYUp && (vertex.y > framebuffer.getHeight());
            cutByYDown = cutByYDown && (vertex.y < 0);
        }

        return
                cutByYDown || cutByYUp || cutByXRight || cutByXLeft;
    }

    private boolean shouldPointBeCulled(Vector4f point, Framebuffer framebuffer) {
        int x = (int) point.x;
        int y = (int) point.y;
        return x < 0.0f || x > framebuffer.getWidth()
                || y < 0.0f || y > framebuffer.getHeight()
                || point.z < -1.0f || point.z > 1.0f;
    }
}
