package org.openjsr.render.lighting;

import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Модель освещения, полностью освещающая каждую точку модели.
 */
public class FullbrightLightingModel implements LightingModel {
    @Override
    public Color applyLighting(Color color, Face triangle, Model model, float[] coords) {
        return color;
    }
}
