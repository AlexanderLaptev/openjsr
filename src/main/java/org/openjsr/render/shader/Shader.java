package org.openjsr.render.shader;

import org.openjsr.mesh.Face;
import org.openjsr.render.Model;

/**
 * Шейдер отвечает за базовый цвет пикселя, то есть цвет пикселя без учёта освещения.
 * Реализации могут, например, выдавать везде один и тот же цвет или брать цвета из текстуры.
 */
public interface Shader {
    /**
     * Выдаёт базовый цвет (цвет при 100% освещённости) точки на треугольнике.
     *
     * @param triangle Грань, для которой вычисляется цвет. Должна являться треугольником.
     * @param model    Модель, для которой производится отрисовка.
     * @param coords   Барицентрические координаты точки на треугольнике.
     * @return Базовый цвет заданной точки на треугольнике.
     */
    int getBaseColor(Face triangle, Model model, float[] coords);
}
