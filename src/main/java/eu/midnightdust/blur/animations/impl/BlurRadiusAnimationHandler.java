package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.animations.AbstractAnimationHandler;
import eu.midnightdust.blur.animations.IAnimationHandler;
import eu.midnightdust.blur.config.BlurConfig;

public class BlurRadiusAnimationHandler extends AbstractAnimationHandler<BlurRadiusAnimationTarget> implements IAnimationHandler<BlurRadiusAnimationTarget> {
    boolean prevReducedBlur = Blur.reducedBlur;

    @Override
    public int getFadeTimeMillis() {
        switch (getTarget()) {
            case FadeIn -> {
                return BlurConfig.fadeTimeMillis;
            }
            case FadeOut -> {
                return BlurConfig.fadeOutTimeMillis;
            }
            case null -> {
                return BlurConfig.fadeTimeMillis;
            }
        }
    }

    @Override
    public void updateAnimation(float deltaSeconds, BlurConfig.Easing easing) {
        if (Blur.reducedBlur != prevReducedBlur) {
            resetTarget(getTarget());
            prevReducedBlur = Blur.reducedBlur;
        }
        super.updateAnimation(deltaSeconds, easing);
    }
}
