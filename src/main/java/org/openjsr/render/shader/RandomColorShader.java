package org.openjsr.render.shader;

import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

import java.util.HashMap;
import java.util.Map;

public class RandomColorShader implements Shader {
    private static final Map<Face, Color> COLORS = new HashMap<>();

    @Override
    public Color getBaseColor(Face triangle, Model model, Vector4f[] projectedVertices, float[] coords) {
        return COLORS.computeIfAbsent(triangle, (f) -> Color.getRandomColor());
    }
}
