package org.openjsr.render.lighting;

import cg.vsu.render.math.MathUtils;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Модель освещения, освещающая все грани модели равномерно.
 */
public class UniformLightingModel implements LightingModel {
    /**
     * Интенсивность освещения по умолчанию.
     */
    public static final float DEFAULT_INTENSITY = 1.0f;

    /**
     * Текущая интенсивность освещения.
     */
    private float intensity = DEFAULT_INTENSITY;

    @Override
    public Color applyLighting(Color color, Face triangle, Model model, float[] coords) {
        return new Color(
                color.red * intensity,
                color.green * intensity,
                color.blue * intensity,
                color.alpha
        );
    }

    @Override
    public float getIntensity() {
        return intensity;
    }

    @Override
    public void setIntensity(float ambientLightLevel) {
        intensity = MathUtils.clamp(ambientLightLevel, 0.0f, 1.0f);
    }
}
