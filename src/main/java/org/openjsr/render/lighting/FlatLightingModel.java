package org.openjsr.render.lighting;

import cg.vsu.render.math.MathUtils;
import cg.vsu.render.math.vector.Vector3f;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Модель освещения с равномерным освещением каждой грани. В качестве
 * нормали используется среднее всех нормалей треугольника.
 */
public class FlatLightingModel extends PointLightingModel {
    public float ambientLight = 0.1f;

    @Override
    public Color applyLighting(Color color, Face triangle, Model model, float[] coords) {
        assert triangle.getTextureVertexIndices().size() == 3;

        Vector3f v1 = model.getMesh().vertices.get(triangle.getTextureVertexIndices().get(0));
        Vector3f v2 = model.getMesh().vertices.get(triangle.getTextureVertexIndices().get(1));
        Vector3f v3 = model.getMesh().vertices.get(triangle.getTextureVertexIndices().get(2));

        Vector3f n1 = model.getMesh().normals.get(triangle.getTextureVertexIndices().get(0));
        Vector3f n2 = model.getMesh().normals.get(triangle.getTextureVertexIndices().get(1));
        Vector3f n3 = model.getMesh().normals.get(triangle.getTextureVertexIndices().get(2));

        float px = v1.x * coords[0] + v2.x * coords[1] + v3.x * coords[2];
        float py = v1.y * coords[0] + v2.y * coords[1] + v3.y * coords[2];
        float pz = v1.z * coords[0] + v2.z * coords[1] + v3.z * coords[2];

        float nx = (n1.x + n2.x + n3.x) / 3.0f;
        float ny = (n1.y + n2.y + n3.y) / 3.0f;
        float nz = (n1.z + n2.z + n3.z) / 3.0f;
        Vector3f normal = new Vector3f(-nx, -ny, -nz); // Разворачиваем нормаль для удобства.

        Vector3f lightRay = new Vector3f(
                px - lightPosition.x,
                py - lightPosition.y,
                pz - lightPosition.z
        ).nor();

        float scaleFactor = MathUtils.clamp(ambientLight + lightRay.dot(normal), 0.0f, 1.0f);
        color.red *= scaleFactor;
        color.green *= scaleFactor;
        color.blue *= scaleFactor;

        return color;
    }
}
