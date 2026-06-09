package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.animations.IEnumAnimationTarget;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.Minecraft;

public enum BlurRadiusAnimationTarget implements IEnumAnimationTarget {
    FadeIn(1.0F),
    FadeOut(0.0F);

    private final float target;

    BlurRadiusAnimationTarget(float v) {
        target = v;
    }

    public float getAnimationTarget() {
        if (Blur.reducedBlur) {
            return target * 0.5F;
        }
        return target;
    }
}
