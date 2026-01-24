package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
//? if > 1.21.5 {
import eu.midnightdust.blur.mixin.GuiGraphicsAccessor;
import eu.midnightdust.blur.mixin.GuiRenderStateAccessor;
//?}
import eu.midnightdust.blur.util.AnimationHandler;
import eu.midnightdust.blur.util.RainbowColor;
import eu.midnightdust.blur.util.TimingHandler;
import eu.midnightdust.lib.util.MidnightColorUtil;
import net.minecraft.client.Minecraft;
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
    public static final String MOD_ID = "blur";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static void init() {
        BlurConfig.init(MOD_ID, BlurConfig.class);
    }

    public static Minecraft minecraft = Minecraft.getInstance();

    private static final TimingHandler timingHandler = new TimingHandler();
    public static final AnimationHandler blurAnimation = new AnimationHandler();
    public static final AnimationHandler backgroundAnimation = new AnimationHandler();
    public static boolean isProcessingRenderPass = false;
    public static boolean forceRenderedBackground = false;

    public static boolean canBlur(GuiGraphics graphics) {
        //? if > 1.21.5 {
        return ((GuiRenderStateAccessor) ((GuiGraphicsAccessor) graphics).getGuiRenderState()).getFirstStratumAfterBlur() == Integer.MAX_VALUE;
        //?} else {
        /*return true;
         *///?}
    }

    public static void onRender() {
        // by this time we have no idea whether the screen has a background or not, so we assume we should fade out all
        // animations
        if (minecraft == null) {
            minecraft = Minecraft.getInstance();
        };

        if (minecraft.screen != null) {
            Blur.LOGGER.debug("onRender: {}", minecraft.screen.getClass().getCanonicalName());
        } else {
            Blur.LOGGER.debug("onRender: null");
        }
        if (!isProcessingRenderPass) {
            isProcessingRenderPass = true;
            blurAnimation.enabled = false;
            backgroundAnimation.enabled = false;
            forceRenderedBackground = false;
        } else {
            Blur.LOGGER.warn("onRender has been called multiple times in one render pass: {}, has blur: {}, has background: {}", minecraft.screen, blurAnimation.enabled, backgroundAnimation.enabled);
        }
    }

    public static void renderBlurredBackground(GuiGraphics context) {
        if (blurAnimation.progress < 0.001F) return; // there's no blur to apply

        //? if > 1.21.5 {
        if (Blur.canBlur(context))
            context.blurBeforeThisStratum();
        //?} else {
            /*minecraft.gameRenderer.processBlurEffect(/^? if <= 1.21.1 {^/ /^minecraft.getTimer().getGameTimeDeltaTicks() ^//^?}^/);
            /^? if <= 1.21.1 {^/ /^minecraft.getMainRenderTarget().bindWrite(false); ^//^?}^/
        *///?}
    }

    public static void renderBackground(GuiGraphics context) {
        if (!forceRenderedBackground) {
            Blur.renderBlurredBackground(context);
            Blur.renderRotatedGradient(context);
            forceRenderedBackground = true;
        } else {
            Blur.LOGGER.warn("renderBackground has been called multiple times in one render pass: {}, has blur: {}, has background: {}", minecraft.screen, blurAnimation.enabled, backgroundAnimation.enabled);
        }
    }

    public static int getBackgroundGradiantColor(boolean second) {
        int alpha = (int) (backgroundAnimation.progress * (second ? BlurConfig.gradientEndAlpha : BlurConfig.gradientStartAlpha));
        Color color;
        if (BlurConfig.rainbowMode) {
            color = Color.getHSBColor(second ? hue: hue2, 1, 1);
        } else {
            color = MidnightColorUtil.hex2Rgb(second ? BlurConfig.gradientEnd : BlurConfig.gradientStart);
        }
        int red = color.getRed();
        int blue = color.getBlue();
        int green = color.getGreen();
        return alpha << 24 | red << 16 | blue << 8 | green;
    }

    public static int getBackgroundGradiantRotation() {
        if (BlurConfig.rainbowMode) return RainbowColor.rotation;
        return BlurConfig.gradientRotation;
    }

    public static void renderRotatedGradient(GuiGraphics context) {
        if (!BlurConfig.useGradient || backgroundAnimation.progress < 0.001F) return;  // there's no gradient to draw

        int width = context.guiWidth();
        int height = context.guiHeight();

        float diagonal = Math.sqrt((float) width*width + height*height);
        int smallestDimension = Math.min(width, height);
        float rotation = Math.toRadians(getBackgroundGradiantRotation());
        int first_color = getBackgroundGradiantColor(false);
        int second_color = getBackgroundGradiantColor(true);

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

    public static void onRenderEnd() {
        if (minecraft == null) {
            minecraft = Minecraft.getInstance();
        };

        if (minecraft.screen != null) {
            Blur.LOGGER.debug("onRenderEnd: {}", minecraft.screen.getClass().getCanonicalName());
        } else {
            Blur.LOGGER.debug("onRenderEnd: null");
        }
        // by this time we must have determined whether the screen had a background or not, so we can handle the fade
        // animation calculations for this screen
        if (isProcessingRenderPass) {
            Blur.LOGGER.debug("processed render pass: {}, has blur: {}, has background: {}", minecraft.screen, blurAnimation.enabled, backgroundAnimation.enabled);

            String screenName = null;
            if (minecraft.screen != null) {
                screenName = minecraft.screen.getClass().getCanonicalName();
            }

            // force a background fade-in animation for forceEnabledScreens
            if (screenName != null &&BlurConfig.forceEnabledScreens.contains(screenName)) {
                blurAnimation.enabled = true;
                backgroundAnimation.enabled = true;
            }

            // force a background fade-out animation for forceDisabledScreens
            if (screenName != null && BlurConfig.forceDisabledScreens.contains(screenName)) {
                blurAnimation.enabled = false;
                backgroundAnimation.enabled = false;
            }

            long deltaTime = timingHandler.getDeltaTimeNanos();
            blurAnimation.updateAnimation(deltaTime, BlurConfig.blurAnimationCurve);
            backgroundAnimation.updateAnimation(deltaTime, BlurConfig.backgroundAnimationCurve);

            isProcessingRenderPass = false;
        }  else {
            Blur.LOGGER.warn("onRenderEnd has been called multiple times in one render pass: {}, has blur: {}, has background: {}", minecraft.screen, blurAnimation.enabled, backgroundAnimation.enabled);
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
