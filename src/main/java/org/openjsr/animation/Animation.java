package org.openjsr.animation;

import org.openjsr.animation.keyframe.Keyframe;
import org.openjsr.core.Transform;

import java.util.ArrayList;
import java.util.List;

public class Animation {
    private List<Keyframe> timeline = new ArrayList<>();

    private Keyframe first;

    private Keyframe last;

    private float length;

    private boolean isDirty = false;

    public List<Keyframe> getKeyframes() {
        return timeline;
    }

    public float getLength() {
        return length;
    }

    public Transform getTransformForTime(float time) {
        if (timeline.isEmpty()) {
            throw new IndexOutOfBoundsException("В анимации нет ключевых кадров.");
        }

        sortTimeline();
        if (time < first.getTime()) {
            return first.getTransform();
        }
        if (time >= last.getTime()) {
            return last.getTransform();
        }

        for (int keyFrameIndex = 1; keyFrameIndex < timeline.size(); keyFrameIndex++) {
            Keyframe right = timeline.get(keyFrameIndex);
            Keyframe left = timeline.get(keyFrameIndex - 1);
            if (right.getTime() >= time) {
                float alpha = (time - left.getTime()) / (right.getTime() - left.getTime());
                return timeline.get(keyFrameIndex - 1).interpolate(alpha, timeline.get(keyFrameIndex));
            }
        }
        throw new IllegalStateException("Вызван недостижимый код.");
    }

    public void addKeyframe(Keyframe keyframe) {
        timeline.add(keyframe);
        isDirty = true;
    }

    public void removeKeyframe(Keyframe keyframe) {
        timeline.remove(keyframe);
        isDirty = true;
    }

    public void removeKeyframe(int index) {
        timeline.remove(index);
        isDirty = true;
    }

    public void sortTimeline() {
        if (!isDirty) return;
        timeline.sort((a, b) -> Float.compare(a.getTime(), b.getTime()));
        first = timeline.get(0);
        last = timeline.get(timeline.size() - 1);
    }
}
