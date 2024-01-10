package org.openjsr.animation.player;

import org.openjsr.animation.Animation;
import org.openjsr.render.Model;

public class AnimationPlayer {
    private Model model;

    private Animation animation;

    private float currentTime = 0.0f;

    public void update(float time) {
        currentTime = time;
        model.setTransform(animation.getTransformForTime(currentTime)); // TODO: recalculate?
//        model.getTransform().recalculateMatrices();
    }

    public void reset() {
        update(0.0f);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
        currentTime = 0.0f;
//        reset();
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
        currentTime = 0.0f;
//        reset();
    }

    public float getCurrentTime() {
        return currentTime;
    }
}
