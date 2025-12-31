package eu.midnightdust.blur.util;

import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Math;

public class FadeAnimation {
    public long lastRender = -1;
    public long deltaTime = -1;
    public float fadeTimeState = 0.0F;
    public float fadeProgress = 0.0F;
    public boolean enabled = false;

    public void onRender(GuiGraphics context) {
        long currentTime = System.currentTimeMillis();
        if (lastRender <= 0) {
            lastRender = currentTime;
            deltaTime = 0;
        } else {
            deltaTime = System.currentTimeMillis() - lastRender;
            lastRender = currentTime;
        }

        updateFadeAnimation(context);
    }

    public void updateFadeAnimation(GuiGraphics context) {
        if (enabled) {
            fadeTimeState += deltaTime / (float) BlurConfig.fadeTimeMillis;
        }
        else {
            fadeTimeState -= deltaTime / (float) BlurConfig.fadeOutTimeMillis;
        }
        fadeTimeState = Math.clamp(0, 1, fadeTimeState);
        if (enabled) {
            fadeProgress = Math.clamp(0, 1, BlurConfig.animationCurve.apply((double) fadeTimeState).floatValue());
        } else {
            fadeProgress = Math.clamp(0, 1, 1-BlurConfig.animationCurve.apply((double) 1-fadeTimeState).floatValue());
        }
    }
}
