package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.AbstractAnimationHandler;
import eu.midnightdust.blur.animations.AnimationState;
import eu.midnightdust.blur.animations.GradientAnimationState;
import eu.midnightdust.blur.animations.IAnimationHandler;
import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.lib.util.MidnightColorUtil;

import java.awt.*;

public class GradientAnimationHandler extends AbstractAnimationHandler<GradientAnimationState> implements IAnimationHandler<GradientAnimationState> {
    private static float rotation = (float) BlurConfig.gradientRotation;
    private static float hue1 = 0.0F;
    private static float hue2 = 0.35F;

    @Override
    public AnimationState stepAnimation(float deltaSeconds, BlurConfig.Easing easing, AnimationState oldAnimationState) {
        AnimationState newAnimationState = oldAnimationState;
        switch (getState()) {
            case Rainbow -> {
                newAnimationState = this.stepForward(deltaSeconds, easing, oldAnimationState);

                // 20 degrees of rotation per second (or 1 degree per tick, same as before)
                rotation = (rotation + deltaSeconds * 20F) % 360F;

                // 72 degrees of rotation per second (or 3.6 degree per tick, same as before)
                hue1 = (hue1 + deltaSeconds * 0.2F) % 1.0F;
                hue2 = (hue2 + deltaSeconds * 0.2F) % 1.0F;
            }
            case Fixed -> {
                newAnimationState = this.stepBackward(deltaSeconds, easing, oldAnimationState);
            }
        }

        return newAnimationState;
    }

    public Color getFirstColor() {
        Color rainbowColor = Color.getHSBColor(hue1, 1, 1);
        Color fixedColor = MidnightColorUtil.hex2Rgb(BlurConfig.gradientStart);

        return new Color(
                (rainbowColor.getRed() / 255F) * getProgress() + (fixedColor.getRed() / 255F) * (1 - getProgress()),
                (rainbowColor.getGreen() / 255F) * getProgress() + (fixedColor.getGreen() / 255F) * (1 - getProgress()),
                (rainbowColor.getBlue() / 255F) * getProgress() + (fixedColor.getBlue() / 255F) * (1 - getProgress()),
                BlurConfig.gradientStartAlpha / 255F
        );
    }

    public Color getSecoundColor() {
        Color rainbowColor = Color.getHSBColor(hue2, 1, 1);
        Color fixedColor = MidnightColorUtil.hex2Rgb(BlurConfig.gradientEnd);

        return new Color(
                (rainbowColor.getRed() / 255F) * getProgress() + (fixedColor.getRed() / 255F) * (1 - getProgress()),
                (rainbowColor.getGreen() / 255F) * getProgress() + (fixedColor.getGreen() / 255F) * (1 - getProgress()),
                (rainbowColor.getBlue() / 255F) * getProgress() + (fixedColor.getBlue() / 255F) * (1 - getProgress()),
                BlurConfig.gradientEndAlpha / 255F
        );
    }

    public float getRotation() {
        float rainbowRotation = rotation;
        float fixedRotation = BlurConfig.gradientRotation;

        return rainbowRotation * getProgress() + fixedRotation * (1 - getProgress());
    }
}