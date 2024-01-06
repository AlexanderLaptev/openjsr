package org.openjsr.render.shader;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import javafx.scene.image.Image;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Шейдер, который накладывает на модель текстуру.
 */
public class TextureShader implements Shader {
    public enum SamplingMode {
        CLAMP,

        WRAP,
    }

    /**
     * Изображение, из которого читаются пиксели.
     */
    private Image image;

    public boolean shouldFlipU = false;

    public boolean shouldFlipV = false;

    public boolean isPerspectiveCorrectionEnabled = false;

    public SamplingMode currentSamplingMode = SamplingMode.CLAMP;

    /**
     * Создаёт текстурный шейдер из заданного изображения-текстуры.
     *
     * @param image Изображение, использующееся в качестве текстуры.
     */
    public TextureShader(Image image) {
        this.image = image;
    }

    @Override
    public Color getBaseColor(Face triangle, Model model, Vector4f[] projectedVertices, float[] coords) {
        assert triangle.getTextureVertexIndices().size() == 3;

        float b0 = coords[0];
        float b1 = coords[1];
        float b2 = coords[2];

        Vector4f v1 = projectedVertices[triangle.getVertexIndices().get(0)].cpy();
        Vector4f v2 = projectedVertices[triangle.getVertexIndices().get(1)].cpy();
        Vector4f v3 = projectedVertices[triangle.getVertexIndices().get(2)].cpy();

        Vector2f t1 = model.getMesh().textureVertices.get(triangle.getTextureVertexIndices().get(0)).cpy();
        Vector2f t2 = model.getMesh().textureVertices.get(triangle.getTextureVertexIndices().get(1)).cpy();
        Vector2f t3 = model.getMesh().textureVertices.get(triangle.getTextureVertexIndices().get(2)).cpy();

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
        int pixelX = (int) (u * imageWidth);
        int pixelY = (int) (v * imageHeight);

        switch (currentSamplingMode) {
            case WRAP -> {
                pixelX %= imageWidth;
                pixelY %= imageHeight;
            }
            case CLAMP -> {
                pixelX = MathUtils.clamp(pixelX, 0, imageWidth);
                pixelY = MathUtils.clamp(pixelY, 0, imageHeight);
            }
        }

        return Color.fromArgb(image.getPixelReader().getArgb(pixelX, pixelY));
    }
}
