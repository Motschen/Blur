package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.AbstractAnimationHandler;
import eu.midnightdust.blur.animations.AnimationState;
import eu.midnightdust.blur.animations.FadeAnimationState;
import eu.midnightdust.blur.animations.IAnimationHandler;
import eu.midnightdust.blur.config.BlurConfig;
import org.joml.Math;

public class FadeAnimationHandler extends AbstractAnimationHandler<FadeAnimationState> implements IAnimationHandler<FadeAnimationState> {
    @Override
    public AnimationState stepAnimation(float deltaSeconds, BlurConfig.Easing easing, AnimationState state) {
        float newFadeTimeState = state.timeState();
        float newFadeProgress = state.progress();

        switch (getState()) {
            case FadeIn -> {
                if (BlurConfig.fadeTimeMillis > 0) {
                    newFadeTimeState += deltaSeconds / (BlurConfig.fadeTimeMillis / 1000F);
                    newFadeTimeState = Math.clamp(0, 1, newFadeTimeState);
                    newFadeProgress = easing.apply((double) newFadeTimeState).floatValue();
                } else {
                    newFadeTimeState = 1.0F;
                    newFadeProgress = 1.0F;
                }
            }
            case FadeOut -> {
                if (BlurConfig.fadeOutTimeMillis > 0) {
                    newFadeTimeState -= deltaSeconds / (BlurConfig.fadeOutTimeMillis / 1000F);
                    newFadeTimeState = Math.clamp(0, 1, newFadeTimeState);
                    newFadeProgress = 1-easing.apply((double) 1-newFadeTimeState).floatValue();
                } else {
                    newFadeTimeState = 0.0F;
                    newFadeProgress = 0.0F;
                }
            }
        }

        newFadeTimeState = Math.clamp(0, 1, newFadeTimeState);
        newFadeProgress = Math.clamp(0, 1, newFadeProgress);

        return new AnimationState(newFadeTimeState, newFadeProgress);
    }
}
