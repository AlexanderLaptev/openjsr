package org.openjsr.animation.keyframe;

import org.openjsr.core.Transform;

public class ConstantKeyframe extends AbstractKeyframe {
    public ConstantKeyframe(float time, Transform transform) {
        super(time, transform);
    }

    @Override
    public Transform interpolate(float alpha, Keyframe other) {
        return transform;
    }
}
