package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
//? if > 1.21.5 {
import eu.midnightdust.blur.mixin.GuiGraphicsAccessor;
import eu.midnightdust.blur.mixin.GuiRenderStateAccessor;
//?}
import eu.midnightdust.blur.util.FadeAnimation;
import eu.midnightdust.blur.util.RainbowColor;
import eu.midnightdust.lib.util.MidnightColorUtil;
import net.minecraft.client.gui.screens.Screen;
import org.joml.Math;

import java.awt.Color;

import net.minecraft.client.gui.GuiGraphics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static eu.midnightdust.blur.util.RainbowColor.hue;
import static eu.midnightdust.blur.util.RainbowColor.hue2;

//? if > 1.21.5 {
import org.joml.Matrix3x2f;
//?} else {
/*import org.joml.Matrix4f;
import net.minecraft.client.Minecraft;
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
    public static final String MOD_ID = "blurperfected";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static void init() {
        BlurConfig.init(MOD_ID, BlurConfig.class);
    }

    public static FadeAnimation blurAnimation = new FadeAnimation();
    public static FadeAnimation backgroundAnimation = new FadeAnimation();
    public static boolean screenChangeProcessed = false;

    public static boolean canBlur(GuiGraphics graphics) {
        //? if > 1.21.5 {
        return ((GuiRenderStateAccessor) ((GuiGraphicsAccessor) graphics).getGuiRenderState()).getFirstStratumAfterBlur() == Integer.MAX_VALUE;
        //?} else {
        /*return true;
         *///?}
    }

    public static void onRender() {
        blurAnimation.setEasing(BlurConfig.blurAnimationCurve);
        blurAnimation.onRender();
        backgroundAnimation.setEasing(BlurConfig.backgroundAnimationCurve);
        backgroundAnimation.onRender();
    }

    public static void renderBlurredBackground(GuiGraphics context) {
        if (blurAnimation.fadeProgress < 0.001F) return; // there's no blur to apply

        //? if > 1.21.5 {
        if (Blur.canBlur(context))
            context.blurBeforeThisStratum();
        //?} else {
            /*Minecraft minecraft = Minecraft.getInstance();
            minecraft.gameRenderer.processBlurEffect(/^? if <= 1.21.1 {^/ /^minecraft.getTimer().getGameTimeDeltaTicks() ^//^?}^/);
        *///?}
    }

    public static void renderBackground(GuiGraphics context) {
        Blur.renderBlurredBackground(context);
        Blur.renderRotatedGradient(context, context.guiWidth(), context.guiHeight());
    }

    public static void onScreenChange(Screen newScreen) {
        if (newScreen != null) {
            Blur.LOGGER.debug("onScreenChange: {}", newScreen.getClass().getCanonicalName());
        } else {
            Blur.LOGGER.debug("onScreenChange: null");
        }
        screenChangeProcessed = false;

        if (newScreen != null && BlurConfig.forceEnabledScreens.contains(newScreen.getClass().getCanonicalName())) {
            blurAnimation.enabled = true;
            backgroundAnimation.enabled = true;
        } else {
            blurAnimation.enabled = false;
            backgroundAnimation.enabled = false;
        }
    }

    public static int getBackgroundColor(boolean second) {
        int a = second ? BlurConfig.gradientEndAlpha : BlurConfig.gradientStartAlpha;
        var col = MidnightColorUtil.hex2Rgb(second ? BlurConfig.gradientEnd : BlurConfig.gradientStart);
        if (BlurConfig.rainbowMode) col = second ? Color.getHSBColor(hue, 1, 1) : Color.getHSBColor(hue2, 1, 1);
        int r = (col.getRGB() >> 16) & 0xFF;
        int b = (col.getRGB() >> 8) & 0xFF;
        int g = col.getRGB() & 0xFF;
        float prog = backgroundAnimation.fadeProgress;
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
        if (!BlurConfig.useGradient || backgroundAnimation.fadeProgress < 0.001F) return;  // there's no gradient to draw

        float diagonal = Math.sqrt((float) width*width + height*height);
        int smallestDimension = Math.min(width, height);
        float rotation = Math.toRadians(getRotation());
        int first_color = Blur.getBackgroundColor(false);
        int second_color = Blur.getBackgroundColor(true);

        //? if > 1.21.5 {
        context.pose().pushMatrix();
        Matrix3x2f posMatrix = context.pose();
        posMatrix.rotate(rotation);
        posMatrix.setTranslation(width / 2f, height / 2f); // Make the gradient's center the pivot point
        posMatrix.scale(diagonal / smallestDimension); // Scales the gradient to the maximum diagonal value needed
        context.fillGradient(-width / 2, -height / 2, width / 2, height / 2, first_color, second_color); // Actually draw the gradient
        context.pose().popMatrix();
        //?} else {
        /*context.pose().pushPose();
        Matrix4f posMatrix = context.pose().last().pose();
        posMatrix.rotateZ(rotation);
        posMatrix.setTranslation(width / 2f, height / 2f, -1000); // Make the gradient's center the pivot point
        posMatrix.scale(diagonal / smallestDimension); // Scales the gradient to the maximum diagonal value needed
        context.fillGradient(-width / 2, -height / 2, width / 2, height / 2, first_color, second_color); // Actually draw the gradient
        context.pose().popPose();
        *///?}
    }

    public static void onRenderEnd(String screenName) {
        if (!screenChangeProcessed) {
            Blur.LOGGER.debug("processed screen: {}, has blur: {}, has background: {}", screenName, blurAnimation.enabled, backgroundAnimation.enabled);
            screenChangeProcessed = true;
        }
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
