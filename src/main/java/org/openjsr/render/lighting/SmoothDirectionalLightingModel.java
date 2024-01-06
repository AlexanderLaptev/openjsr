package org.openjsr.render.lighting;

import cg.vsu.render.math.vector.Vector3f;
import cg.vsu.render.math.vector.Vector4f;

public class SmoothDirectionalLightingModel extends DirectionalLightingModel {
    @Override
    protected Vector3f calculateNormal(Vector4f n1, Vector4f n2, Vector4f n3, float[] coords) {
        return new Vector3f(
                n1.x * coords[0] + n2.x * coords[1] + n3.x * coords[2],
                n1.y * coords[0] + n2.y * coords[1] + n3.y * coords[2],
                n1.z * coords[0] + n2.z * coords[1] + n3.z * coords[2]
        );
    }
}
