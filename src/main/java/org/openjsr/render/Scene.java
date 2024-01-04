package org.openjsr.render;

import org.openjsr.core.PerspectiveCamera;
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

    /**
     * Получает список моделей, находящихся на данной сцене.
     *
     * @return список моделей, находящихся на данной сцене.
     */
    public List<Model> getModels() {
        return models;
    }

    /**
     * Вызывает отрисовку сцены глобальным растеризатором.
     *
     * @param camera        Активная камера.
     * @param lightingModel Модель освещения.
     */
    public void render(PerspectiveCamera camera, LightingModel lightingModel, Framebuffer buffer) {
        for (Model model : models) {
            Rasterizer.getInstance().drawModel(model, camera, lightingModel, buffer);
        }
    }
}
