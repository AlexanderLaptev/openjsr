package org.openjsr.render.lighting;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.matrix.Matrix4f;
import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.core.Transform;
import org.openjsr.mesh.Face;
import org.openjsr.mesh.Mesh;
import org.openjsr.render.Model;

public abstract class DirectionalLightingModel implements AmbientLightingModel {
    public static final float DEFAULT_AMBIENT_LIGHT_LEVEL = 0.13f;

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
    public Color applyLighting(Color color, Face triangle, Model model, float[] coords) {
        assert triangle.getVertexIndices().size() == 3;
        Mesh mesh = model.getMesh();
        Transform transform = model.getTransform();

        Vector4f n1 = new Vector4f(mesh.normals.get(triangle.getNormalIndices().get(0)));
        Vector4f n2 = new Vector4f(mesh.normals.get(triangle.getNormalIndices().get(1)));
        Vector4f n3 = new Vector4f(mesh.normals.get(triangle.getNormalIndices().get(2)));
        // Нормали нечувствительны к перемещению и масштабированию, но чувствительны
        // к поворотам модели, поэтому их необходимо повернуть.
        transform.rotationMatrix.mul(n1);
        transform.rotationMatrix.mul(n2);
        transform.rotationMatrix.mul(n3);

        Vector3f faceNormal = calculateNormal(n1, n2, n3, coords);
        faceNormal.scl(-1);

        float dotProduct = faceNormal.dot(direction);
        float scaleFactor = MathUtils.clamp(ambientLightLevel + Math.abs(dotProduct), 0.0f, 1.0f);
        return new Color(
                color.red * scaleFactor,
                color.green * scaleFactor,
                color.blue * scaleFactor,
                color.alpha
        );
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
