package org.openjsr.render.lighting;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;

public abstract class DirectionalLightingModel implements AmbientLightingModel {
    public static final float DEFAULT_AMBIENT_LIGHT_LEVEL = 0.08f;

    public Vector3f direction = new Vector3f();

    public float ambientLightLevel = DEFAULT_AMBIENT_LIGHT_LEVEL;

    private float intensity = 1.0f;

    @Override
    public float getAmbientLightLevel() {
        return ambientLightLevel;
    }

    @Override
    public void setAmbientLightLevel(float ambientLightLevel) {
        this.ambientLightLevel = ambientLightLevel;
    }

    @Override
    public void applyLighting(
            Color color,
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            float[] barycentric
    ) {
        Vector4f n1 = normals[0];
        Vector4f n2 = normals[1];
        Vector4f n3 = normals[2];

        Vector3f faceNormal = calculateNormal(n1, n2, n3, barycentric);
        faceNormal.scl(-1);

        float dotProduct = faceNormal.dot(direction);
        float scaleFactor = MathUtils.clamp(ambientLightLevel + Math.max(0.0f, dotProduct), 0.0f, 1.0f);
        color.red *= scaleFactor;
        color.green *= scaleFactor;
        color.blue *= scaleFactor;
    }

    @Override
    public float getIntensity() {
        return intensity;
    }

    @Override
    public void setIntensity(float intensity) {
        this.intensity = Math.max(0.0f, intensity);
    }

    protected abstract Vector3f calculateNormal(
            Vector4f n1,
            Vector4f n2,
            Vector4f n3,
            float[] coords
    );
}
