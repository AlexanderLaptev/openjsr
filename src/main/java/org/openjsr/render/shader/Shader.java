package org.openjsr.render.shader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;

/**
 * Шейдер отвечает за базовый цвет пикселя, то есть цвет пикселя без учёта освещения.
 * Реализации могут, например, выдавать везде один и тот же цвет или брать цвета из текстуры.
 */
public interface Shader {
    Color getPixelColor(Vector4f[] vertices, Vector2f[] textureVertices, float[] barycentric);
}
