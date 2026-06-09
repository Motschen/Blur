package eu.midnightdust.blur.animations;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;


public abstract class AbstractAnimationHandler<E extends Enum<E> & IEnumAnimationTarget> implements IAnimationHandler<E> {
    private AnimationState<E> state = new AnimationState<E>(
            0.0F,
            0.0F,
            0.0F,
            null
    );
    private E newTarget = null;

    public AnimationState<E> stepAnimation(float deltaSeconds, BlurConfig.Easing easing) {
        float newTimeState = Math.clamp(0, 1, getTimeState() + (1000 * deltaSeconds / getFadeTimeMillis()));

        var newValue = Math.lerp(state.startValue(), state.target().getAnimationTarget(), easing.apply((double) newTimeState).floatValue());

        return new AnimationState<E>(newTimeState, state.startValue(), newValue, state.target());
    }

    @Override
    public int getFadeTimeMillis() {
        return BlurConfig.fadeTimeMillis;
    }

    @Override
    public void updateAnimation(float deltaSeconds, BlurConfig.Easing easing) {
        if (getFadeTimeMillis() == 0 || deltaSeconds <= 0) return;
        if (newTarget != null){
            resetTarget(newTarget);
            newTarget = null;
        }
        if (getTarget() == null) return;
        // actually update the animation
        state = stepAnimation(deltaSeconds, easing);
    }

    @Override
    public float getCurrentValue() {
        return state.currentValue();
    }

    @Override
    public float getTimeState() {
        return state.timeState();
    }

    @Override
    public @Nullable E getTarget() {
        return state.target();
    }

    @Override
    public void setTarget(E target) {
        if (target == newTarget) return;
        if (target == getTarget()) {
            newTarget = null;
        } else {
            newTarget = target;
        }
    }

    public void resetTarget(E target) {
        Blur.LOGGER.debug("New Animation Target: {}", target);

        state = new AnimationState<E>(
                0.0F,
                getCurrentValue(),
                getCurrentValue(),
                target
        );
    }
}
