package eu.midnightdust.blur;

import eu.midnightdust.blur.animations.impl.FadeAnimationState;
import eu.midnightdust.blur.animations.impl.GradientAnimationState;
import eu.midnightdust.blur.animations.impl.GradientAnimationHandler;
import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.blur.animations.impl.FadeAnimationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.awt.*;

import org.joml.Math;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if > 1.21.5 {
import eu.midnightdust.blur.mixin.GuiGraphicsAccessor;
import eu.midnightdust.blur.mixin.GuiRenderStateAccessor;
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
import net.neoforged.fml.common.Mod;
*///?}


public class Blur {
    public static final String MOD_ID = "blur";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static void init() {
        BlurConfig.init(MOD_ID, BlurConfig.class);
        if (BlurConfig.configVersion < 3) {
            BlurConfig.forceEnabledScreens.add("mezz.jei.gui.recipes.RecipesGui");
            BlurConfig.forceEnabledScreens.add("me.shedaniel.rei.impl.client.gui.screen.DefaultDisplayViewingScreen");
            BlurConfig.configVersion = 3;
            BlurConfig.write(MOD_ID);
        }
    }

    public static Minecraft minecraft = Minecraft.getInstance();

    public static final FadeAnimationHandler blurRadiusAnimation = new FadeAnimationHandler();
    public static final FadeAnimationHandler backgroundAlphaAnimation = new FadeAnimationHandler();
    public static final GradientAnimationHandler gradientAnimation = new GradientAnimationHandler();

    public static boolean isProcessingRenderPass = false;
    public static boolean forceRenderedBackground = false;

    public static float getGameTimeDeltaTicks() {
        //? if >= 1.21.5 {
        return minecraft.getDeltaTracker().getGameTimeDeltaTicks();
        //?} else {
        /*return minecraft.getTimer().getGameTimeDeltaTicks();
        *///?}
    }

    public static float getRealtimeDeltaTicks() {
        //? if >= 1.21.5 {
        return minecraft.getDeltaTracker().getRealtimeDeltaTicks();
        //?} else {
        /*return minecraft.getTimer().getRealtimeDeltaTicks();
         *///?}
    }

    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    public static boolean canBlur(GuiGraphicsExtractor graphics) {
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
            blurRadiusAnimation.setState(FadeAnimationState.FadeOut);
            backgroundAlphaAnimation.setState(FadeAnimationState.FadeOut);
            forceRenderedBackground = false;
        } else {
            Blur.LOGGER.debug("onRender has been called multiple times in one render pass: {}, " +
                            "blur radius animation state: {}, background alpha animation state: {}",
                    minecraft.screen, blurRadiusAnimation.getState(), backgroundAlphaAnimation.getState()
            );
        }
    }

    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    public static void renderBlurredBackground(GuiGraphicsExtractor context) {
        if (blurRadiusAnimation.getProgress() < 0.001F) return; // there's no blur to apply

        //? if > 1.21.5 {
        if (Blur.canBlur(context))
            context.blurBeforeThisStratum();
        //?} else {
            /*minecraft.gameRenderer.processBlurEffect(/^? if <= 1.21.1 {^/ /^getGameTimeDeltaTicks() ^//^?}^/);
            /^? if <= 1.21.1 {^/ /^minecraft.getMainRenderTarget().bindWrite(false); ^//^?}^/
        *///?}
    }

    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    public static void renderBackground(GuiGraphicsExtractor context) {
        //? if > 1.21.5 {
        context.nextStratum();  // actually draw the background on EVERYTHING behind it
        //?}
        if (!forceRenderedBackground) {
            Blur.renderBlurredBackground(context);
            Blur.renderRotatedGradient(context);
            forceRenderedBackground = true;
        } else {
            Blur.LOGGER.debug("renderBackground has been called multiple times in one render pass: {}, " +
                            "blur radius animation state: {}, background alpha animation state: {}",
                    minecraft.screen, blurRadiusAnimation.getState(), backgroundAlphaAnimation.getState()
            );
        }
    }

    public static int getBackgroundGradiantColor(boolean second) {
        Color color = second ? gradientAnimation.getSecoundColor() : gradientAnimation.getFirstColor();
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        int alpha = (int) (backgroundAlphaAnimation.getProgress() * color.getAlpha());
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static int getBackgroundGradiantRotation() {
        return (int) gradientAnimation.getRotation();
    }

    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    public static void renderRotatedGradient(GuiGraphicsExtractor context) {
        if (!BlurConfig.useGradient || backgroundAlphaAnimation.getProgress() < 0.001F) return;  // there's no gradient to draw

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

    public static void updateAnimations() {
        float deltaTimeSeconds = getRealtimeDeltaTicks() / 20F;
        blurRadiusAnimation.updateAnimation(deltaTimeSeconds, BlurConfig.blurAnimationCurve);
        backgroundAlphaAnimation.updateAnimation(deltaTimeSeconds, BlurConfig.backgroundAnimationCurve);
        gradientAnimation.updateAnimation(deltaTimeSeconds, BlurConfig.backgroundAnimationCurve); // TODO: should we define another curve?
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
            Blur.LOGGER.debug("processed render pass: {}," +
                            "blur radius animation state: {}, background alpha animation state: {}",
                    minecraft.screen, blurRadiusAnimation.getState(), backgroundAlphaAnimation.getState()
            );
            String screenName = null;
            if (minecraft.screen != null) {
                screenName = minecraft.screen.getClass().getCanonicalName();
            }

            // force a background fade-in animation for forceEnabledScreens
            if (screenName != null &&BlurConfig.forceEnabledScreens.contains(screenName)) {
                blurRadiusAnimation.setState(FadeAnimationState.FadeIn);
                backgroundAlphaAnimation.setState(FadeAnimationState.FadeIn);
            }

            // force a background fade-out animation for forceDisabledScreens
            if (screenName != null && BlurConfig.forceDisabledScreens.contains(screenName)) {
                blurRadiusAnimation.setState(FadeAnimationState.FadeOut);
                backgroundAlphaAnimation.setState(FadeAnimationState.FadeOut);
            }

            // update gradientAnimation state from config
            gradientAnimation.setState(BlurConfig.rainbowMode ? GradientAnimationState.Rainbow : GradientAnimationState.Fixed);

            updateAnimations();

            isProcessingRenderPass = false;
        }  else {
            Blur.LOGGER.debug("onRenderEnd has been called multiple times in one render pass: {}," +
                            "blur radius animation state: {}, background alpha animation state: {}",
                    minecraft.screen, blurRadiusAnimation.getState(), backgroundAlphaAnimation.getState()
            );
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
            Blur.init();
        }
    }
    //?} else if neoforge {
    /*@Mod(value = Blur.MOD_ID, dist = Dist.CLIENT)
    public static class BlurNeoForge {
        public BlurNeoForge() {
            Blur.init();
        }
    }
    *///?}
}
