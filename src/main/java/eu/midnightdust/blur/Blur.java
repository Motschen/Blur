package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.blur.mixin.GuiGraphicsAccessor;
import eu.midnightdust.blur.mixin.GuiRenderStateAccessor;
import eu.midnightdust.blur.util.RainbowColor;
import eu.midnightdust.lib.util.MidnightColorUtil;
import org.joml.Math;

import java.awt.Color;
import java.lang.Double;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import static eu.midnightdust.blur.BlurInfo.*;
import static eu.midnightdust.blur.util.RainbowColor.hue;
import static eu.midnightdust.blur.util.RainbowColor.hue2;

//? if > 1.21.5 {
import org.joml.Matrix3x2f;
//?} else {
/*import org.joml.Matrix4f;
*///?}

//? fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
//?} else if neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
*///?}


public class Blur {
    public static final String MOD_ID = "blur-perfected";
    public static void init() {
        BlurConfig.init(MOD_ID, BlurConfig.class);
    }

    public static long lastRender = -1;
    public static long deltaTime = -1;
    public static float fadeTimeState = 1.0F;
    public static float fadeProgress = 1.0F;
    public static boolean screenHasBlur = false;
    public static boolean screenHasBackground = false;

    public static boolean canBlur(GuiGraphics graphics) {
        //? if > 1.21.5 {
        return ((GuiRenderStateAccessor) ((GuiGraphicsAccessor) graphics).getGuiRenderState()).getFirstStratumAfterBlur() == Integer.MAX_VALUE;
        //?} else {
        /*return true;
         *///?}
    }

    public static void onRender(GuiGraphics context) {
        long currentTime = System.currentTimeMillis();
        if (lastRender <= 0) {
            lastRender = currentTime;
            deltaTime = 0;
        } else {
            deltaTime = System.currentTimeMillis() - lastRender;
            lastRender = currentTime;
        }

        Blur.updateFadeAnimation(context);
    }

    public static void onScreenChange() {
        screenHasBlur = false;
        screenHasBackground = false;
    }

    public static void updateFadeAnimation(GuiGraphics context) {
        if (screenHasBlur) {
            fadeTimeState += deltaTime / (float) BlurConfig.fadeTimeMillis;
        }
        else {
            fadeTimeState -= deltaTime / (float) BlurConfig.fadeOutTimeMillis;
        }
        fadeTimeState = Math.clamp(0, 1, fadeTimeState);
        fadeProgress = BlurConfig.animationCurve.apply((double) fadeTimeState).floatValue();
    }

    public static int getBackgroundColor(boolean second) {
        int a = second ? BlurConfig.gradientEndAlpha : BlurConfig.gradientStartAlpha;
        var col = MidnightColorUtil.hex2Rgb(second ? BlurConfig.gradientEnd : BlurConfig.gradientStart);
        if (BlurConfig.rainbowMode) col = second ? Color.getHSBColor(hue, 1, 1) : Color.getHSBColor(hue2, 1, 1);
        int r = (col.getRGB() >> 16) & 0xFF;
        int b = (col.getRGB() >> 8) & 0xFF;
        int g = col.getRGB() & 0xFF;
        float prog = fadeProgress;
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

        //? if > 1.21.5 {
        context.pose().pushMatrix();
        Matrix3x2f posMatrix = context.pose();
        posMatrix.rotate(Math.toRadians(getRotation()));
        posMatrix.setTranslation(width / 2f, height / 2f); // Make the gradient's center the pivot point
        posMatrix.scale(diagonal / smallestDimension); // Scales the gradient to the maximum diagonal value needed
        context.fillGradient(-width / 2, -height / 2, width / 2, height / 2, Blur.getBackgroundColor(false), Blur.getBackgroundColor(true)); // Actually draw the gradient
        context.pose().popMatrix();
        //?} else {
        /*context.pose().pushPose();
        Matrix4f posMatrix = context.pose().last().pose();
        posMatrix.rotateZ(Math.toRadians(getRotation()));
        posMatrix.setTranslation(width / 2f, height / 2f, -1000); // Make the gradient's center the pivot point
        posMatrix.scale(diagonal / smallestDimension); // Scales the gradient to the maximum diagonal value needed
        context.fillGradient(-width / 2, -height / 2, width / 2, height / 2, Blur.getBackgroundColor(false), Blur.getBackgroundColor(true)); // Actually draw the gradient
        context.pose().popPose();
        *///?}
    }


    //? fabric {
    public static class BlurFabric implements ModInitializer, ClientModInitializer {
        @Override
        public void onInitialize() {
            Blur.init();
        }
        @Override
        public void onInitializeClient() {
            ClientTickEvents.END_CLIENT_TICK.register(client -> RainbowColor.tick());
        }
    }
    //?} else if neoforge {
    /*@Mod(value = Blur.MOD_ID, dist = Dist.CLIENT)
    public static class BlurNeoForge {
        public BlurNeoForge() {
            Blur.init();
        }

        @EventBusSubscriber(modid = Blur.MOD_ID, value = Dist.CLIENT)
        public static class ClientGameEvents {
            @SubscribeEvent
            public static void endClientTick(ClientTickEvent.Post event) {
                RainbowColor.tick();
            }
        }
    }
    *///?}
}
