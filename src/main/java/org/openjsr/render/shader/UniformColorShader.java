package org.openjsr.render.shader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;

/**
 * Шейдер, который закрашивает всю модель сплошным цветом.
 */
public class UniformColorShader implements Shader {
    /**
     * Цвет, использующийся при отрисовке моделей.
     */
    public Color color = Color.getRandomColor();

    @Override
    public void getPixelColor(
            Color color,
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            float[] barycentric
    ) {
        color.red = this.color.red;
        color.green = this.color.green;
        color.blue = this.color.blue;
    }
}
