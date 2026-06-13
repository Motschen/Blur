package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.IEnumAnimationTarget;
import eu.midnightdust.blur.config.BlurConfig;

public enum GradientAnimationTarget implements IEnumAnimationTarget {
    Fixed(0.0F),
    Rainbow(1.0F);

    private final float target;

    GradientAnimationTarget(float v) {
        target = v;
    }

    public float getAnimationTarget() {
        return target;
    }

    @Override
    public int getAnimationTimeMillis() {
        if (this == GradientAnimationTarget.Rainbow)
            return BlurConfig.fadeTimeMillis;
        else
            return BlurConfig.fadeOutTimeMillis;
    }
}
