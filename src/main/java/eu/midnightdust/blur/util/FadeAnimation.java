package eu.midnightdust.blur.util;

import eu.midnightdust.blur.config.BlurConfig;
import org.joml.Math;

public class FadeAnimation {
    public long lastRender = -1;
    public long deltaTime = -1;
    public float fadeTimeState = 0.0F;
    public float fadeProgress = 0.0F;
    public boolean enabled = false;
    public BlurConfig.Easing easing = BlurConfig.Easing.FLAT;

    public void setEasing(BlurConfig.Easing easing) {
        this.easing = easing;
    }

    public void onRender() {
        long currentTime = System.currentTimeMillis();
        if (lastRender <= 0) {
            lastRender = currentTime;
            deltaTime = 0;
        } else {
            deltaTime = System.currentTimeMillis() - lastRender;
            lastRender = currentTime;
        }

        updateFadeAnimation();
    }

    public void updateFadeAnimation() {
        if (enabled) {
            fadeTimeState += deltaTime / (float) BlurConfig.fadeTimeMillis;
        }
        else {
            fadeTimeState -= deltaTime / (float) BlurConfig.fadeOutTimeMillis;
        }
        fadeTimeState = Math.clamp(0, 1, fadeTimeState);
        if (enabled) {
            fadeProgress = easing.apply((double) fadeTimeState).floatValue();
        } else {
            fadeProgress = 1-easing.apply((double) 1-fadeTimeState).floatValue();
        }
        fadeProgress = Math.clamp(0, 1, fadeProgress);
    }
}
