package com.tterrag.blur;

import com.tterrag.blur.config.BlurConfig;
import eu.midnightdust.lib.util.MidnightColorUtil;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

public class Blur implements ClientModInitializer {

    public static final String MODID = "blur";
    public static long start;
    public static long fadeOutProgress;

    private static final ManagedShaderEffect blur = ShaderEffectManager.getInstance().manage(new Identifier(MODID, "shaders/post/fade_in_blur.json"),
            shader -> shader.setUniformValue("Radius", (float) BlurConfig.radius));
    private static final Uniform1f blurProgress = blur.findUniform1f("Progress");

    @Override
    public void onInitializeClient() {
        BlurConfig.init("blur", BlurConfig.class);

        ShaderEffectRenderCallback.EVENT.register((deltaTick) -> {
            if (start > 0) {
                blurProgress.set(getProgress(MinecraftClient.getInstance().currentScreen != null));
                blur.render(deltaTick);
            }
        });
    }

    private static boolean doFade = false;

    public static void onScreenChange(Screen newGui) {
        if (MinecraftClient.getInstance().world != null) {
            boolean excluded = newGui == null || BlurConfig.blurExclusions.stream().anyMatch(exclusion -> newGui.getClass().getName().contains(exclusion));
            if (!excluded) {
                if (BlurConfig.showScreenTitle) System.out.println(newGui.getClass().getName());
                blur.setUniformValue("Radius", (float) BlurConfig.radius);
                if (doFade) {
                    start = System.currentTimeMillis();
                    doFade = false;
                }
                fadeOutProgress = BlurConfig.fadeOutTimeMillis;
            } else if (newGui == null && fadeOutProgress > 0) {
                blur.setUniformValue("Radius", (float) BlurConfig.radius);
                start = System.currentTimeMillis();
            } else {
                start = -1;
                doFade = true;
            }
        }
    }

    private static float getProgress(boolean fadeIn) {
        if (fadeIn) {
            float x = Math.min((System.currentTimeMillis() - start) / (float) BlurConfig.fadeTimeMillis, 1);
            if (BlurConfig.ease) x *= (2 - x);  // easeInCubic
            return x;
        }
        else {
            float x = Math.min((System.currentTimeMillis() - start) / (float) BlurConfig.fadeOutTimeMillis, 1);
            if (BlurConfig.ease) x *= (2 - x);  // easeOutCubic
            return -x + BlurConfig.fadeOutTimeMillis;
        }
    }

    public static int getBackgroundColor(boolean second, Screen screen) {
        int a = second ? BlurConfig.gradientEndAlpha : BlurConfig.gradientStartAlpha;
        var col = MidnightColorUtil.hex2Rgb(second ? BlurConfig.gradientEnd : BlurConfig.gradientStart);
        int r = (col.getRGB() >> 16) & 0xFF;
        int b = (col.getRGB() >> 8) & 0xFF;
        int g = col.getRGB() & 0xFF;
        float prog = getProgress(screen != null);
        a *= prog;
        r *= prog;
        g *= prog;
        b *= prog;
        return a << 24 | r << 16 | b << 8 | g;
    }
}
