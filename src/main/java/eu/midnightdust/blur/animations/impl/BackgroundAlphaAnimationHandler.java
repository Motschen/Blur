package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.AbstractAnimationHandler;
import eu.midnightdust.blur.animations.IAnimationHandler;
import eu.midnightdust.blur.config.BlurConfig;

public class BackgroundAlphaAnimationHandler extends AbstractAnimationHandler<BackgroundAlphaAnimationTarget> implements IAnimationHandler<BackgroundAlphaAnimationTarget> {
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
}
