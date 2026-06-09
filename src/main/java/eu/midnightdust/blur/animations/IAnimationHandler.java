package eu.midnightdust.blur.animations;

import eu.midnightdust.blur.config.BlurConfig;

public interface IAnimationHandler<E extends Enum<E> & IEnumAnimationTarget> {
    void updateAnimation(float deltaSeconds, BlurConfig.Easing easing);
    void setTarget(E target);
    int getFadeTimeMillis();
    float getTimeState();
    float getCurrentValue();
    E getTarget();
}
