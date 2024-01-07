package org.openjsr.render.shader;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import javafx.scene.image.Image;
import org.openjsr.core.Color;

/**
 * Шейдер, который накладывает на модель текстуру.
 */
public class TextureShader implements Shader {
    /**
     * Изображение, из которого читаются пиксели.
     */
    private Image image;

    public boolean shouldFlipU = true;

    public boolean shouldFlipV = true;

    public boolean isPerspectiveCorrectionEnabled = true;

    /**
     * Создаёт текстурный шейдер из заданного изображения-текстуры.
     *
     * @param image Изображение, использующееся в качестве текстуры.
     */
    public TextureShader(Image image) {
        this.image = image;
    }

    @Override
    public void getPixelColor(
            Color color,
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            float[] coords
    ) {
        float b0 = coords[0];
        float b1 = coords[1];
        float b2 = coords[2];

        Vector4f v1 = vertices[0];
        Vector4f v2 = vertices[1];
        Vector4f v3 = vertices[2];

        Vector2f t1 = textureVertices[0];
        Vector2f t2 = textureVertices[1];
        Vector2f t3 = textureVertices[2];

        float u;
        float v;
        if (isPerspectiveCorrectionEnabled) {
            float b = b0 / v1.w + b1 / v2.w + b2 / v3.w;
            float B0 = (b0 / v1.w) / b;
            float B1 = (b1 / v2.w) / b;
            float B2 = (b2 / v3.w) / b;

            u = t1.x * B0 + t2.x * B1 + t3.x * B2;
            v = t1.y * B0 + t2.y * B1 + t3.y * B2;
        } else {
            u = t1.x * b0 + t2.x * b1 + t3.x * b2;
            v = t1.y * b0 + t2.y * b1 + t3.y * b2;
        }

        if (shouldFlipU) u = 1.0f - u;
        if (shouldFlipV) v = 1.0f - v;

        int imageWidth = (int) image.getWidth() - 1;
        int imageHeight = (int) image.getHeight() - 1;
        int pixelX = MathUtils.clamp((int) (u * imageWidth), 0, imageWidth);
        int pixelY = MathUtils.clamp((int) (v * imageHeight), 0, imageHeight);

        int argb = image.getPixelReader().getArgb(pixelX, pixelY);
        color.red = ((argb >> 16) & 0xFF) / 255.0f;
        color.green = ((argb >> 8) & 0xFF) / 255.0f;
        color.blue = (argb & 0xFF) / 255.0f;
    }
}
