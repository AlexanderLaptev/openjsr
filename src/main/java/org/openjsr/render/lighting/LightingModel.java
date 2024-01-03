package org.openjsr.render.lighting;

import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Модель освещения. Отвечает за применение освещения к пикселю.
 */
public interface LightingModel {
    /**
     * Применяет освещение к данной точке треугольника.
     *
     * @param color    Цвет треугольника в данной точке без освещения.
     * @param triangle Грань, для которой выполняется освещение. Должна являться треугольником.
     * @param model    Модель, для которой производится отрисовка.
     * @param coords   Барицентрические координаты точки на треугольнике.
     * @return Цвет данной точки с учётом освещения.
     */
    int applyLighting(int color, Face triangle, Model model, float[] coords);
}
