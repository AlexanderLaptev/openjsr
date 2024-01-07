package org.openjsr.render.lighting;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Модель освещения. Отвечает за применение освещения к пикселю.
 */
public interface LightingModel {
    void applyLighting(
            Color color,
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            float[] barycentric
    );

    float getIntensity();

    void setIntensity(float intensity);
}
