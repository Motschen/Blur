package eu.midnightdust.blur.util;

import eu.midnightdust.blur.config.BlurConfig;
import org.joml.Math;

public class AnimationHandler {
    public float timeState = 0.0F;
    public float progress = 0.0F;
    public boolean enabled = false;

    public void updateAnimation(long delta, BlurConfig.Easing easing) {
        if (delta <= 0) return;

        float newFadeTimeState = timeState;
        float newFadeProgress = progress;

        if (enabled) {
            if (BlurConfig.fadeTimeMillis > 0) {
                newFadeTimeState += delta / (float) (BlurConfig.fadeTimeMillis * 1000000);
                newFadeTimeState = Math.clamp(0, 1, newFadeTimeState);
                newFadeProgress = easing.apply((double) newFadeTimeState).floatValue();
            } else {
                newFadeTimeState = 1.0F;
                newFadeProgress = 1.0F;
            }
        }
        else {
            if (BlurConfig.fadeOutTimeMillis > 0) {
                newFadeTimeState -= delta / (float) (BlurConfig.fadeOutTimeMillis * 1000000);
                newFadeTimeState = Math.clamp(0, 1, newFadeTimeState);
                newFadeProgress = 1-easing.apply((double) 1-newFadeTimeState).floatValue();
            } else {
                newFadeTimeState = 0.0F;
                newFadeProgress = 0.0F;
            }
        }

        timeState = Math.clamp(0, 1, newFadeTimeState);
        progress = Math.clamp(0, 1, newFadeProgress);
    }
}
