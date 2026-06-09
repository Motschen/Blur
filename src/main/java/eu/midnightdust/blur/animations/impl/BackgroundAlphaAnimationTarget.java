package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.IEnumAnimationTarget;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.Minecraft;

public enum BackgroundAlphaAnimationTarget implements IEnumAnimationTarget {
    FadeIn(1.0F),
    FadeOut(0.0F);

    private final float target;

    BackgroundAlphaAnimationTarget(float v) {
        target = v;
    }

    public float getAnimationTarget() {
        return target;
    }
}
