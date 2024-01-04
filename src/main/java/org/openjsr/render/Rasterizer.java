package org.openjsr.render;

import org.openjsr.core.PerspectiveCamera;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.lighting.LightingModel;

public class Rasterizer {
    private static final Rasterizer INSTANCE = new Rasterizer();

    private Rasterizer() { }

    public static Rasterizer getInstance() {
        return INSTANCE;
    }

    public void drawModel(Model model, PerspectiveCamera camera, LightingModel lightingModel, Framebuffer buffer) {
        // TODO!
    }
}
