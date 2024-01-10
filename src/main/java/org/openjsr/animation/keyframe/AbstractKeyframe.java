package org.openjsr.animation.keyframe;

import org.openjsr.core.Transform;

public abstract class AbstractKeyframe implements Keyframe {
    public AbstractKeyframe(float time, Transform transform) {
        this.time = time;
        this.transform = transform;
    }

    protected float time = 0.0f;

    protected Transform transform;

    @Override
    public float getTime() {
        return time;
    }

    @Override
    public Transform getTransform() {
        return transform;
    }
}
