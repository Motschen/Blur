package eu.midnightdust.blur.util;

import eu.midnightdust.blur.config.BlurConfig;

public class RainbowColor {
    public static int rotation;
    public static float hue;
    public static float hue2 = 0.35f;

    public static void tick() {
        if (BlurConfig.rainbowMode) {
            if (hue >= 1) hue = 0f;
            hue += 0.01f;
            if (hue2 >= 1) hue2 = 0f;
            hue2 += 0.01f;

            if (rotation >= 360) rotation = 0;
            rotation += 1;
        }
    }
}