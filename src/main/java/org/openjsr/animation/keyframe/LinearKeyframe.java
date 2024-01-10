package org.openjsr.animation.keyframe;

import org.openjsr.core.Transform;

public class LinearKeyframe extends AbstractKeyframe {
    private final Transform interpolated = new Transform();

    public LinearKeyframe(float time, Transform transform) {
        super(time, transform);
    }

    @Override
    public Transform interpolate(float alpha, Keyframe next) {
        interpolated.scale = this.transform.scale.cpy().lerp(next.getTransform().scale, alpha);
        interpolated.rotation = this.transform.rotation.cpy().lerp(next.getTransform().rotation, alpha);
        interpolated.position = this.transform.position.cpy().lerp(next.getTransform().position, alpha);
        interpolated.recalculateMatrices();
        return interpolated;
    }
}
