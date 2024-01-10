package org.openjsr.animation;

import cg.vsu.render.math.vector.Vector3f;
import org.junit.jupiter.api.Test;
import org.openjsr.animation.keyframe.ConstantKeyframe;
import org.openjsr.animation.keyframe.Keyframe;
import org.openjsr.animation.keyframe.LinearKeyframe;
import org.openjsr.core.Transform;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimationTest {

    @Test
    void timelineTest() {
        Transform transform1 = new Transform(
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 0)
        );

        Transform transform2 = new Transform(
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 0),
                new Vector3f(0, 0, 0)
        );

        Transform transform3 = new Transform(
                new Vector3f(1, 1, 1),
                new Vector3f(1, 1, 1),
                new Vector3f(1, 1, 1)
        );

        Keyframe frame1 = new ConstantKeyframe(0, transform1);
        Keyframe frame2 = new LinearKeyframe(12, transform2);
        Keyframe frame3 = new ConstantKeyframe(16, transform3);

        Animation animation = new Animation();
        animation.addKeyframe(frame2);
        animation.addKeyframe(frame1);
        animation.addKeyframe(frame3);

        assertEquals(0, animation.getTransformForTime(-1).scale.x);
        assertEquals(0, animation.getTransformForTime(0).scale.x);
        assertEquals(0, animation.getTransformForTime(6).scale.x);
        assertEquals(0, animation.getTransformForTime(12).scale.x);
        assertEquals(0.5f, animation.getTransformForTime(14).scale.x);
        assertEquals(1f, animation.getTransformForTime(16).scale.x);
        assertEquals(1f, animation.getTransformForTime(20).scale.x);
    }
}