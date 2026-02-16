package eu.midnightdust.blur.animations;

import eu.midnightdust.blur.config.BlurConfig;

public abstract class AbstractAnimationHandler<E extends Enum<E>> implements IAnimationHandler<E> {
    private float timeState = 0.0F;
    private float progress = 0.0F;
    private E state = null;

    public AnimationState stepAnimation(float deltaSeconds, BlurConfig.Easing easing, AnimationState oldAnimationState) {
        return oldAnimationState;
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
