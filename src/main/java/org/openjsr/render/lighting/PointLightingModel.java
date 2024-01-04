package org.openjsr.render.lighting;

import cg.vsu.render.math.vector.Vector3f;

/**
 * Модель освещения с единственным источником освещения в виде точки.
 */
public abstract class PointLightingModel implements LightingModel {
    /**
     * Позиция источника освещения.
     */
    public Vector3f lightPosition = new Vector3f();
}
