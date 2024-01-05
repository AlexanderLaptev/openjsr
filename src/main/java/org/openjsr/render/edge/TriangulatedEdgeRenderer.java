package org.openjsr.render.edge;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;
import org.openjsr.render.Rasterizer;
import org.openjsr.render.framebuffer.Framebuffer;

public class TriangulatedEdgeRenderer implements EdgeRenderer {
    public Color edgeColor = new Color(255, 0, 0);

    @Override
    public void renderEdges(Model model, Vector4f[] projectedVertices, Framebuffer buffer) {
        for (Face triangle : model.getMesh().triangles) {
            int x1 = (int) projectedVertices[triangle.getVertexIndices().get(0)].x;
            int y1 = (int) projectedVertices[triangle.getVertexIndices().get(0)].y;
            int x2 = (int) projectedVertices[triangle.getVertexIndices().get(1)].x;
            int y2 = (int) projectedVertices[triangle.getVertexIndices().get(1)].y;
            int x3 = (int) projectedVertices[triangle.getVertexIndices().get(2)].x;
            int y3 = (int) projectedVertices[triangle.getVertexIndices().get(2)].y;

            Rasterizer r = Rasterizer.getInstance();
            r.drawLine(x1, y1, x2, y2, buffer, edgeColor);
            r.drawLine(x2, y2, x3, y3, buffer, edgeColor);
            r.drawLine(x3, y3, x1, y1, buffer, edgeColor);
        }
    }
}
