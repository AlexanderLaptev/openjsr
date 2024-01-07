package org.openjsr.render;

import org.openjsr.core.PerspectiveCamera;
import org.openjsr.render.edge.EdgeRenderStrategy;
import org.openjsr.render.framebuffer.Framebuffer;
import org.openjsr.render.lighting.LightingModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Сцена, располагающая модели в трёхмерном пространстве.
 */
public class Scene {
    /**
     * Список моделей, находящихся на данной сцене.
     */
    private final List<Model> models = new ArrayList<>();

    private final List<PerspectiveCamera> cameras = new ArrayList<>();

    private EdgeRenderStrategy edgeRenderStrategy;

    /**
     * Получает список моделей, находящихся на данной сцене.
     *
     * @return список моделей, находящихся на данной сцене.
     */
    public List<Model> getModels() {
        return models;
    }

    /**
     * Получает список камер, находящихся на данной сцене.
     * @return список камер, находящихся на данной сцене.
     */
    public List<PerspectiveCamera> getCameras() {
        return cameras;
    }

    public EdgeRenderStrategy getEdgeRenderer() {
        return edgeRenderStrategy;
    }

    public void setEdgeRenderer(EdgeRenderStrategy edgeRenderStrategy) {
        this.edgeRenderStrategy = edgeRenderStrategy;
    }
}
