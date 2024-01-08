package org.openjsr.render;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.shader.Shader;
import org.openjsr.util.GeometryUtils;

public class Rasterizer {
    private static final Rasterizer INSTANCE = new Rasterizer();

    private Rasterizer() { }

    public static Rasterizer getInstance() {
        return INSTANCE;
    }

    private final Color tempColor = new Color();

    public void fillTriangle(
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            Shader shader,
            Framebuffer framebuffer
    ) {
        int x1 = (int) vertices[0].x;
        int x2 = (int) vertices[1].x;
        int x3 = (int) vertices[2].x;

        int y1 = (int) vertices[0].y;
        int y2 = (int) vertices[1].y;
        int y3 = (int) vertices[2].y;

        drawTopTriangle(
                x1, y1,
                x2, y2,
                x3, y3,
                vertices,
                textureVertices,
                normals,
                shader,
                framebuffer
        );
        drawBottomTriangle(
                x1, y1,
                x2, y2,
                x3, y3,
                vertices,
                textureVertices,
                normals,
                shader,
                framebuffer
        );
    }

    private void drawTopTriangle(
            int x1, int y1,
            int x2, int y2,
            int x3, int y3,
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            Shader shader,
            Framebuffer framebuffer
    ) {
        int Y1 = Math.max(0, y1);
        int Y2 = Math.min(y2, framebuffer.getHeight());

        int x2x1 = x2 - x1;
        int x3x1 = x3 - x1;
        int y2y1 = y2 - y1;
        int y3y1 = y3 - y1;

        for (int y = Y1; y < Y2; y++) {
            // Нет необходимости проверять делители на равенство
            // нулю, ибо в таком случае цикл вообще не запустится.
            int l = x2x1 * (y - y1) / y2y1 + x1; // Ребро 1-2.
            int r = x3x1 * (y - y1) / y3y1 + x1; // Ребро 1-3.

            if (l > r) { // Меняем местами концы отрезка, если нужно.
                int tmp = l;
                l = r;
                r = tmp;
            }
            l = Math.max(0, l);
            r = Math.min(r, framebuffer.getWidth());

            for (int x = l; x <= r; x++) {
                float[] barycentric = GeometryUtils.getBarycentricCoords(
                        x, y,
                        x1, y1,
                        x2, y2,
                        x3, y3
                );
                final float depth = GeometryUtils.interpolate(
                        barycentric,
                        new float[]{vertices[0].z, vertices[1].z, vertices[2].z}
                );
                drawPixel(
                        x, y, depth,
                        vertices,
                        textureVertices,
                        normals,
                        shader,
                        barycentric,
                        framebuffer
                );
            }
        }
    }

    private void drawBottomTriangle(
            int x1, int y1,
            int x2, int y2,
            int x3, int y3,
            Vector4f[] vertices,
            Vector2f[] triangleTextureVertices,
            Vector4f[] normals,
            Shader shader,
            Framebuffer framebuffer
    ) {
        int Y2 = Math.max(0, y2);
        int Y3 = Math.min(y3, framebuffer.getHeight());

        int x3x2 = x3 - x2;
        int x3x1 = x3 - x1;
        int y3y2 = y3 - y2;
        int y3y1 = y3 - y1;

        // Рисует разделяющую линию и нижний треугольник.
        if (y3y2 == 0 || y3y1 == 0) return; // Останавливается, если треугольник вырожденный.
        for (int y = Y2; y <= Y3; y++) {
            int l = x3x2 * (y - y2) / y3y2 + x2; // Ребро 2-3.
            int r = x3x1 * (y - y1) / y3y1 + x1; // Ребро 1-3.

            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            l = Math.max(0, l);
            r = Math.min(r, framebuffer.getWidth());

            for (int x = l; x <= r; x++) {
                float[] barycentric = GeometryUtils.getBarycentricCoords(
                        x, y,
                        x1, y1,
                        x2, y2,
                        x3, y3
                );
                final float depth = GeometryUtils.interpolate(
                        barycentric,
                        new float[]{vertices[0].z, vertices[1].z, vertices[2].z}
                );
                drawPixel(
                        x, y, depth,
                        vertices,
                        triangleTextureVertices,
                        normals,
                        shader,
                        barycentric,
                        framebuffer
                );
            }
        }
    }


    public void drawLine(
            int x1, int y1, float z1,
            int x2, int y2, float z2,
            boolean shouldTestDepth,
            Framebuffer framebuffer,
            Color c
    ) {
        DepthBuffer depthBuffer = framebuffer.getDepthBuffer();
        int deltaX = Math.abs(x2 - x1);
        int deltaY = Math.abs(y2 - y1);
        int error = 0;
        float z;

        // если бы все было круто, то вместо этого длинного if мы могли бы просто поменять местами y и x,
        // но к сожалнию, тогда бы не сработал setPixel правильно, пришлось бы хранить флаг и проверять при каждом новом пикселе -> затратнее.

        if (deltaX >= deltaY) {
            int y = y1; // выбрали побочную ось
            int dirY = y2 - y1;

            if (dirY > 0) dirY = 1;
            if (dirY < 0) dirY = -1;

            int deltaErr = deltaY + 1;

            if (x1 > x2) {
                y = y2;
                int tmp = x1;
                x1 = x2;
                x2 = tmp;
                dirY *= -1;
            }
            for (int x = x1; x <= x2; x++) { // основная ось
                z = 1.0f / GeometryUtils.interpolate(
                        x, y,
                        x1, y1, 1.0f / z1,
                        x2, y2, 1.0f / z2
                );

                if (shouldTestDepth) {
                    if (depthBuffer.isVisible(x, y, z)) {
                        depthBuffer.setZ(x, y, z);
                        framebuffer.setPixel(x, y, c);
                    }
                } else {
                    if (x > 0 && x < framebuffer.getWidth() && y > 0 && y < framebuffer.getHeight()) {
                        framebuffer.setPixel(x, y, c);
                    }
                }

                error = error + deltaErr;
                if (error >= (deltaX + 1)) {
                    y += dirY;
                    error -= (deltaX + 1);
                }
            }
        } else {
            int x = x1; // выбрали побочную ось
            int dirX = x2 - x1;

            if (dirX > 0) dirX = 1;
            if (dirX < 0) dirX = -1;

            int deltaErr = deltaX + 1;

            if (y1 > y2) {
                x = x2;
                int tmp = y1;
                y1 = y2;
                y2 = tmp;
                dirX *= -1;
            }
            for (int y = y1; y <= y2; y++) {
                z = 1.0f / GeometryUtils.interpolate(
                        x, y,
                        x1, y1, 1.0f / z1,
                        x2, y2,1.0f / z2
                );

                if (shouldTestDepth) {
                    if (depthBuffer.isVisible(x, y, z)) {
                        depthBuffer.setZ(x, y, z);
                        framebuffer.setPixel(x, y, c);
                    }
                } else {
                    if (x > 0 && x < framebuffer.getWidth() && y > 0 && y < framebuffer.getHeight()) {
                        framebuffer.setPixel(x, y, c);
                    }
                }

                error = error + deltaErr;
                if (error >= (deltaY + 1)) {
                    x += dirX;
                    error -= (deltaY + 1);
                }
            }
        }
    }

    public void drawPixel(
            int x, int y, float depth,
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            Shader shader,
            float[] barycentric,
            Framebuffer framebuffer
    ) {
        DepthBuffer depthBuffer = framebuffer.getDepthBuffer();
        if (depthBuffer.isVisible(x, y, depth)) {
            depthBuffer.setZ(x, y, depth);

            shader.getPixelColor(
                    tempColor,
                    vertices,
                    textureVertices,
                    normals,
                    barycentric
            );
            framebuffer.setPixel(x, y, tempColor);
        }
    }
}
