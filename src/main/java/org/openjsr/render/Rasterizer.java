package org.openjsr.render;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.lighting.LightingModel;
import org.openjsr.render.shader.Shader;
import org.openjsr.util.GeometryUtils;

public class Rasterizer {
    private static final Rasterizer INSTANCE = new Rasterizer();

    private Rasterizer() { }

    public static Rasterizer getInstance() {
        return INSTANCE;
    }

    public void fillTriangle(
            Vector4f[] vertices,
            Shader shader,
            LightingModel lightingModel,
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

        drawTopTriangle(
                x1, y1, z1,
                x2, y2, z2,
                x3, y3, z3,
                shader,
                lightingModel,
                framebuffer
        );
        drawBottomTriangle(
                x1, y1, z1,
                x2, y2, z2,
                x3, y3, z3,
                shader,
                lightingModel,
                framebuffer
        );
    }

    private void drawTopTriangle(
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            int x3, int y3, float z3,
            Shader shader,
            LightingModel lightingModel,
            Framebuffer framebuffer
    ) {
        int x2x1 = x2 - x1;
        int x3x1 = x3 - x1;
        int y2y1 = y2 - y1;
        int y3y1 = y3 - y1;

        for (int y = y1; y < y2; y++) {
            // Нет необходимости проверять делители на равенство
            // нулю, ибо в таком случае цикл вообще не запустится.
            int l = x2x1 * (y - y1) / y2y1 + x1; // Ребро 1-2.
            int r = x3x1 * (y - y1) / y3y1 + x1; // Ребро 1-3.

            if (l > r) { // Меняем местами концы отрезка, если нужно.
                int tmp = l;
                l = r;
                r = tmp;
            }

            for (int x = l; x < r; x++) {
                float[] barycentric = GeometryUtils.getBarycentricCoords(
                        x, y,
                        x1, y1,
                        x2, y2,
                        x3, y3
                );
                final float z = GeometryUtils.interpolate(barycentric, new float[]{z1, z2, z3});
                drawPixel(
                        x, y, z,
                        shader,
                        lightingModel,
                        barycentric,
                        framebuffer
                );
            }
        }
    }

    private void drawBottomTriangle(
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            int x3, int y3, float z3,
            Shader shader,
            LightingModel lightingModel,
            Framebuffer framebuffer
    ) {
        int x3x2 = x3 - x2;
        int x3x1 = x3 - x1;
        int y3y2 = y3 - y2;
        int y3y1 = y3 - y1;

        // Рисует разделяющую линию и нижний треугольник.
        if (y3y2 == 0 || y3y1 == 0) return; // Останавливается, если треугольник вырожденный.
        for (int y = y2; y <= y3; y++) {
            int l = x3x2 * (y - y2) / y3y2 + x2; // Ребро 2-3.
            int r = x3x1 * (y - y1) / y3y1 + x1; // Ребро 1-3.

            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }

            for (int x = l; x < r; x++) {
                float[] barycentric = GeometryUtils.getBarycentricCoords(
                        x, y,
                        x1, y1,
                        x2, y2,
                        x3, y3
                );
                final float z = GeometryUtils.interpolate(barycentric, new float[]{z1, z2, z3});
                drawPixel(
                        x, y, z,
                        shader,
                        lightingModel,
                        barycentric,
                        framebuffer
                );
            }
        }
    }

    public void drawPixel(
            int x, int y, float z,
            Shader shader,
            LightingModel lightingModel,
            float[] barycentric,
            Framebuffer framebuffer
    ) {
        DepthBuffer depthBuffer = framebuffer.getDepthBuffer();
        if (depthBuffer.isVisible(x, y, z)) {
            depthBuffer.setZ(x, y, z);

//            Color color = shader.getBaseColor(triangle, model, projectedVertices, barycentric);
//            color = lightingModel.applyLighting(color, triangle, model, barycentric);

            // TODO!
            framebuffer.setPixel(x, y, new Color(255, 0, 0));
        }
    }
}
