package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.IEnumAnimationTarget;

public enum FadeAnimationTarget implements IEnumAnimationTarget {
    FadeIn(1.0F),
    FadeOut(0.0F);

    private final float target;

    FadeAnimationTarget(float v) {
        target = v;
    }

    public float getAnimationTarget() {
        return target;
    }
}
