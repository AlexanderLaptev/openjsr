package org.openjsr.render.shader;

import cg.vsu.render.math.vector.Vector2f;
import cg.vsu.render.math.vector.Vector4f;
import org.openjsr.core.Color;
import org.openjsr.render.lighting.LightingModel;

public class LightingShader implements Shader {
    private Shader baseColorShader;

    private LightingModel lightingModel;

    public LightingShader(Shader baseColorShader, LightingModel lightingModel) {
        this.baseColorShader = baseColorShader;
        this.lightingModel = lightingModel;
    }

    public Shader getBaseColorShader() {
        return baseColorShader;
    }

    public void setBaseColorShader(Shader baseColorShader) {
        this.baseColorShader = baseColorShader;
    }

    public LightingModel getLightingModel() {
        return lightingModel;
    }

    public void setLightingModel(LightingModel lightingModel) {
        this.lightingModel = lightingModel;
    }

    @Override
    public void getPixelColor(
            Color color,
            Vector4f[] vertices,
            Vector2f[] textureVertices,
            Vector4f[] normals,
            float[] barycentric
    ) {
        baseColorShader.getPixelColor(color, vertices, textureVertices, normals, barycentric);
        lightingModel.applyLighting(color, vertices, textureVertices, normals, barycentric);
    }
}
