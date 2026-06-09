package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.IEnumAnimationTarget;

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
}
