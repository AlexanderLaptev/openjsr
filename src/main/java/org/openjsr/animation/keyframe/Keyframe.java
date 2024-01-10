package org.openjsr.animation.keyframe;

import org.openjsr.core.Transform;

public interface Keyframe {
    float getTime();

    Transform getTransform();

    Transform interpolate(float alpha, Keyframe other);
}
