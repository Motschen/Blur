package eu.midnightdust.blur.animations;

import eu.midnightdust.blur.config.BlurConfig;
import org.joml.Math;

public abstract class AbstractAnimationHandler<E extends Enum<E>> implements IAnimationHandler<E> {
    private float timeState = 0.0F;
    private float progress = 0.0F;
    private E state = null;

    public AnimationState stepAnimation(float deltaSeconds, BlurConfig.Easing easing, AnimationState oldAnimationState) {
        return oldAnimationState;
    }

    public AnimationState stepForward(float deltaSeconds, BlurConfig.Easing easing, AnimationState oldAnimationState) {
        float newTimeState;
        float newProgress;

        if (BlurConfig.fadeTimeMillis > 0) {
            newTimeState = Math.clamp(0, 1, oldAnimationState.timeState() + (1000 * deltaSeconds / BlurConfig.fadeTimeMillis));
            newProgress = Math.clamp(0, 1, easing.apply((double) newTimeState).floatValue());
        } else {
            newTimeState = 1.0F;
            newProgress = 1.0F;
        }

        return new AnimationState(newTimeState, newProgress);
    }

    public AnimationState stepBackward(float deltaSeconds, BlurConfig.Easing easing, AnimationState oldAnimationState) {
        float newTimeState;
        float newProgress;

        if (BlurConfig.fadeOutTimeMillis > 0) {
            newTimeState = Math.clamp(0, 1, oldAnimationState.timeState() - (1000 * deltaSeconds / BlurConfig.fadeOutTimeMillis));
            newProgress = Math.clamp(0, 1, 1 - easing.apply((double) 1 - newTimeState).floatValue());
        } else {
            newTimeState = 0.0F;
            newProgress = 0.0F;
        }

        return new AnimationState(newTimeState, newProgress);
    }

    @Override
    public void updateAnimation(float deltaSeconds, BlurConfig.Easing easing) {
        if (state == null || deltaSeconds <= 0) return;
        // actually update the animation
        AnimationState newAnimationState = stepAnimation(deltaSeconds, easing, new AnimationState(getTimeState(), getProgress()));
        timeState = newAnimationState.timeState();
        progress = newAnimationState.progress();
    }

    @Override
    public float getProgress() {
        return progress;
    }

    @Override
    public float getTimeState() {
        return timeState;
    }

    @Override
    public E getState() {
        return state;
    }

    @Override
    public void setState(E state) {
        this.state = state;
    }
}
