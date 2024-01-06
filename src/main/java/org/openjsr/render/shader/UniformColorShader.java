package org.openjsr.render.shader;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Шейдер, который закрашивает всю модель сплошным цветом.
 */
public class UniformColorShader implements Shader {
    /**
     * Цвет, использующийся при отрисовке моделей.
     */
    public Color color = Color.getRandomColor();

    @Override
    public Color getBaseColor(Face triangle, Model model, Vector4f[] projectedVertices, float[] coords) {
        return color;
    }
}
