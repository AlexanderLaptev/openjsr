package org.openjsr.render.lighting;

import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;

public class FlatDirectionalLightingModel extends DirectionalLightingModel {
    @Override
    protected Vector3f calculateNormal(Vector4f n1, Vector4f n2, Vector4f n3, float[] coords) {
        return new Vector3f(
                (n1.x + n2.x + n3.x) / 3.0f,
                (n1.y + n2.y + n3.y) / 3.0f,
                (n1.z + n2.z + n3.z) / 3.0f
        );
    }
}
