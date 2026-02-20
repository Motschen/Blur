package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.AbstractAnimationHandler;
import eu.midnightdust.blur.animations.AnimationState;
import eu.midnightdust.blur.animations.IAnimationHandler;
import eu.midnightdust.blur.config.BlurConfig;

public class FadeAnimationHandler extends AbstractAnimationHandler<FadeAnimationState> implements IAnimationHandler<FadeAnimationState> {
    @Override
    public AnimationState stepAnimation(float deltaSeconds, BlurConfig.Easing easing, AnimationState oldAnimationState) {
        switch (getState()) {
            case FadeIn -> {
                return this.stepForward(deltaSeconds, easing, oldAnimationState);
            }
            case FadeOut -> {
                return this.stepBackward(deltaSeconds, easing, oldAnimationState);
            }
        }
        return oldAnimationState;
    }
}
