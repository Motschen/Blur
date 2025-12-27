package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.blur.util.RainbowColor;
import eu.midnightdust.lib.util.MidnightColorUtil;
import org.joml.Math;
import org.joml.Matrix3x2f;

import java.awt.Color;
import java.lang.Double;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static eu.midnightdust.blur.BlurInfo.*;
import static eu.midnightdust.blur.util.RainbowColor.hue;
import static eu.midnightdust.blur.util.RainbowColor.hue2;

public class Blur {
    public static final String MOD_ID = "blur";
    public static void init() {
        BlurConfig.init(MOD_ID, BlurConfig.class);
    }

    public static boolean doFade = false;

    public static void onRender() {
        if (!BlurInfo.doTest && BlurInfo.screenChanged) { // After the tests for blur and background color have been completed
            Blur.onScreenChange();
            BlurInfo.screenChanged = false;
        }
        BlurInfo.doTest = false; // Set the test state to completed, as tests will happen in the same tick.
    }
    public static void renderFadeout(GuiGraphics context, int width, int height, Minecraft client) {
        if (BlurInfo.start >= 0 && !BlurInfo.screenHasBlur && BlurInfo.prevScreenHasBlur) { // Fade out in non-blurred screens
            client.gameRenderer.processBlurEffect();

            if (BlurInfo.prevScreenHasBackground && BlurConfig.useGradient) Blur.renderRotatedGradient(context, width, height);
        }
    }

    public static void onScreenChange() {
        if (screenHasBlur) {
            if (doFade) {
                start = System.currentTimeMillis();
                doFade = false;
            }
        } else if (prevScreenHasBlur && BlurConfig.fadeOutTimeMillis > 0) {
            start = System.currentTimeMillis();
            doFade = true;
        } else {
            start = -1;
            doFade = true;
        }
    }

    public static void updateProgress(boolean fadeIn) {
        double x;
        if (fadeIn) {
            x = Math.min((System.currentTimeMillis() - start) / (double) BlurConfig.fadeTimeMillis, 1);
        }
        else {
            x = Math.max(1 + (start - System.currentTimeMillis()) / (double) BlurConfig.fadeOutTimeMillis, 0);
            if (x <= 0) {
                start = -1;
            }
        }
        x = BlurConfig.animationCurve.apply(x, fadeIn);
        x = Math.clamp(0, 1, x);

        progress = Double.valueOf(x).floatValue();
    }

    public static int getBackgroundColor(boolean second) {
        int a = second ? BlurConfig.gradientEndAlpha : BlurConfig.gradientStartAlpha;
        var col = MidnightColorUtil.hex2Rgb(second ? BlurConfig.gradientEnd : BlurConfig.gradientStart);
        if (BlurConfig.rainbowMode) col = second ? Color.getHSBColor(hue, 1, 1) : Color.getHSBColor(hue2, 1, 1);
        int r = (col.getRGB() >> 16) & 0xFF;
        int b = (col.getRGB() >> 8) & 0xFF;
        int g = col.getRGB() & 0xFF;
        float prog = progress;
        a = (int) (prog * a);
        r = (int) (prog * r);
        g = (int) (prog * g);
        b = (int) (prog * b);
        return a << 24 | r << 16 | b << 8 | g;
    }
    public static int getRotation() {
        if (BlurConfig.rainbowMode) return RainbowColor.rotation;
        return BlurConfig.gradientRotation;
    }
    public static void renderRotatedGradient(GuiGraphics context, int width, int height) {
        float diagonal = Math.sqrt((float) width*width + height*height);
        int smallestDimension = Math.min(width, height);

        context.pose().pushMatrix();
        Matrix3x2f posMatrix = context.pose();
        posMatrix.rotate(Math.toRadians(getRotation()));
        posMatrix.setTranslation(width / 2f, height / 2f); // Make the gradient's center the pivot point
        posMatrix.scale(diagonal / smallestDimension); // Scales the gradient to the maximum diagonal value needed
        context.fillGradient(-width / 2, -height / 2, width / 2, height / 2, Blur.getBackgroundColor(false), Blur.getBackgroundColor(true)); // Actually draw the gradient
        context.pose().popMatrix();
    }
}
