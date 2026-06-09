package eu.midnightdust.blur.animations.impl;

import eu.midnightdust.blur.animations.AbstractAnimationHandler;
import eu.midnightdust.blur.animations.AnimationState;
import eu.midnightdust.blur.animations.IAnimationHandler;
import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.lib.util.MidnightColorUtil;

import java.awt.*;

public class GradientAnimationHandler extends AbstractAnimationHandler<GradientAnimationTarget> implements IAnimationHandler<GradientAnimationTarget> {
    private static float rotation = (float) BlurConfig.gradientRotation;
    private static float hue1 = 0.0F;
    private static float hue2 = 0.35F;

    @Override
    public AnimationState<GradientAnimationTarget> stepAnimation(float deltaSeconds, BlurConfig.Easing easing) {
        GradientAnimationTarget target = getTarget();
        if (target == GradientAnimationTarget.Rainbow) {
            // 20 degrees of rotation per second (or 1 degree per tick, same as before)
            rotation = (rotation + deltaSeconds * 20F) % 360F;

            // 72 degrees of rotation per second (or 3.6 degree per tick, same as before)
            hue1 = (hue1 + deltaSeconds * 0.2F) % 1.0F;
            hue2 = (hue2 + deltaSeconds * 0.2F) % 1.0F;
        }
        return super.stepAnimation(deltaSeconds, easing);
    }

    public Color getFirstColor() {
        Color rainbowColor = Color.getHSBColor(hue1, 1, 1);
        Color fixedColor = MidnightColorUtil.hex2Rgb(BlurConfig.gradientStart);

        return new Color(
                (rainbowColor.getRed() / 255F) * getCurrentValue() + (fixedColor.getRed() / 255F) * (1 - getCurrentValue()),
                (rainbowColor.getGreen() / 255F) * getCurrentValue() + (fixedColor.getGreen() / 255F) * (1 - getCurrentValue()),
                (rainbowColor.getBlue() / 255F) * getCurrentValue() + (fixedColor.getBlue() / 255F) * (1 - getCurrentValue()),
                BlurConfig.gradientStartAlpha / 255F
        );
    }

    public Color getSecoundColor() {
        Color rainbowColor = Color.getHSBColor(hue2, 1, 1);
        Color fixedColor = MidnightColorUtil.hex2Rgb(BlurConfig.gradientEnd);

        return new Color(
                (rainbowColor.getRed() / 255F) * getCurrentValue() + (fixedColor.getRed() / 255F) * (1 - getCurrentValue()),
                (rainbowColor.getGreen() / 255F) * getCurrentValue() + (fixedColor.getGreen() / 255F) * (1 - getCurrentValue()),
                (rainbowColor.getBlue() / 255F) * getCurrentValue() + (fixedColor.getBlue() / 255F) * (1 - getCurrentValue()),
                BlurConfig.gradientEndAlpha / 255F
        );
    }

    public float getRotation() {
        float rainbowRotation = rotation;
        float fixedRotation = BlurConfig.gradientRotation;

        return rainbowRotation * getCurrentValue() + fixedRotation * (1 - getCurrentValue());
    }
}