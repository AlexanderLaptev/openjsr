package org.openjsr.render.shader;

import cg.vsu.render.math.vector.Vector2f;
import javafx.scene.image.Image;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Шейдер, который накладывает на модель текстуру.
 */
public class TextureShader implements Shader {
    /**
     * Изображение, из которого читаются пиксели.
     */
    private Image image;

    /**
     * Создаёт текстурный шейдер из заданного изображения-текстуры.
     *
     * @param image Изображение, использующееся в качестве текстуры.
     */
    public TextureShader(Image image) {
        this.image = image;
    }

    @Override
    public Color getBaseColor(Face triangle, Model model, float[] coords) {
        assert triangle.getTextureVertexIndices().size() == 3;

        Vector2f v1 = model.getMesh().textureVertices.get(triangle.getTextureVertexIndices().get(0));
        Vector2f v2 = model.getMesh().textureVertices.get(triangle.getTextureVertexIndices().get(1));
        Vector2f v3 = model.getMesh().textureVertices.get(triangle.getTextureVertexIndices().get(2));

        float u = v1.x * coords[0] + v2.x * coords[1] + v3.x * coords[2];
        float v = v1.y * coords[0] + v2.y * coords[1] + v3.y * coords[2];
        int pixelX = (int) (image.getWidth() * u);
        int pixelY = (int) (image.getHeight() * v);
        int argb;
        try {
            argb = image.getPixelReader().getArgb(pixelX, pixelY);
        } catch (Exception e) {
            argb = 0;
        }
        return Color.fromArgb(argb);
    }
}
