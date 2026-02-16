package eu.midnightdust.blur.animations;

import eu.midnightdust.blur.config.BlurConfig;

public interface IAnimationHandler<E extends Enum<E>> {
    void updateAnimation(float deltaSeconds, BlurConfig.Easing easing);
    default void updateAnimation(float deltaSeconds) {
        updateAnimation(deltaSeconds, BlurConfig.Easing.FLAT);
    }
    void setState(E state);
    float getTimeState();
    float getProgress();
    E getState();
}
