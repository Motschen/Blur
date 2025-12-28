package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.GuiGraphics;
//? if > 1.21.5 {
import eu.midnightdust.blur.mixin.GuiGraphicsAccessor;
import eu.midnightdust.blur.mixin.GuiRenderStateAccessor;
//?}

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

    public static boolean canBlur(GuiGraphics graphics) {
        //? if > 1.21.5 {
        return ((GuiRenderStateAccessor) ((GuiGraphicsAccessor) graphics).getGuiRenderState()).getFirstStratumAfterBlur() == Integer.MAX_VALUE;
        //?} else {
        //return true;
        //?}
    }
}
