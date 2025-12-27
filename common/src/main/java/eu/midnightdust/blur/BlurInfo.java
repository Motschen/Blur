package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.screens.Screen;

public class BlurInfo {
    public static long start;
    public static float progress;

    public static boolean prevScreenHasBlur;
    public static boolean screenHasBlur;

    public static boolean prevScreenHasBackground;
    public static boolean screenHasBackground;

    public static boolean doTest = true;
    public static boolean screenChanged = true;
    public static long lastScreenChange = System.currentTimeMillis();

    public static void reset(Screen newScreen) {
        // Here, we reset all tests, to check if the new screen has blur and/or a background
        if (newScreen != null && BlurConfig.excludedScreens.contains(newScreen.getClass().getCanonicalName())) return;
        prevScreenHasBlur = screenHasBlur;
        prevScreenHasBackground = screenHasBackground;
        screenHasBlur = false;
        screenHasBackground = false;
        doTest = true;
        screenChanged = true;
        start = -1;
        lastScreenChange = System.currentTimeMillis();
    }
}
