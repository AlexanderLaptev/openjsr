package org.openjsr.render.lighting;

/**
 * Модель освещения с симуляцией отражённого света от окружающей среды.
 */
public interface AmbientLightingModel extends LightingModel {
    /**
     * Получает уровень отражённого света.
     *
     * @return Уровень отражённого света в пределах от 0 до 1.
     */
    float getAmbientLightLevel();

    /**
     * Устанавливает уровень отражённого света.
     *
     * @param ambientLightLevel Новый уровень отражённого света. Если этот уровень
     *                          не находится в пределах от 0 до 1, он будет ограничен
     *                          соответствующим образом.
     */
    void setAmbientLightLevel(float ambientLightLevel);
}
