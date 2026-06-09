package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.midnightdust.blur.animations.impl.BackgroundAlphaAnimationTarget;
import eu.midnightdust.blur.animations.impl.BlurRadiusAnimationTarget;
import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.blur.util.DebugHudRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import eu.midnightdust.blur.Blur;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Screen.class)
public abstract class MixinScreen {
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
    @Shadow protected Minecraft minecraft;
    //~ if >= 26.1 'render' -> 'extract'
    @Shadow protected abstract void extractBlurredBackground(/*? if > 1.21.5 {*/ GuiGraphicsExtractor context /*?} else if <= 1.21.1 {*/ /*float delta *//*?}*/);


    @Inject(
            at = @At("TAIL"),
            //? if >= 26.1 {
            method = "extractRenderStateWithTooltipAndSubtitles"
            //?} else if > 1.21.8 {
            /*method = "renderWithTooltipAndSubtitles"
            *///?} else if > 1.21.1 {
             /*method = "renderWithTooltip"
            *///?} else {
             /*method = "render"
            *///?}
    )
    public void blur$renderDebugHud(GuiGraphicsExtractor context, int i, int j, float f, CallbackInfo ci) {
        if (BlurConfig.showScreenID && minecraft.screen != null) {
            DebugHudRenderer.renderLine(context, minecraft.font, minecraft.screen.getClass().getCanonicalName(), 2, new Color(0xff80dfff), true);
        }
    }

    //~ if >= 26.1 'render' -> 'extract'
    @Inject(at = @At("HEAD"), method = "extractBlurredBackground")
    public void blur$onRenderBlurredBackground(CallbackInfo ci) {
        // if the screen tries to call `renderBlurredBackground` we can determine the screen had a blurred background,
        // and we can set the blur animation to fade-in mode
        Blur.blurRadiusAnimation.setTarget(BlurRadiusAnimationTarget.FadeIn);
    }

    @Inject(
            at = @At("HEAD"),
            //~ if >= 26.1 'render' -> 'extract'
            method = "extractTransparentBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V" // used by screens while in a level
    )
    private void blur$onScreenBackground(GuiGraphicsExtractor context, CallbackInfo ci) {
        blur$onRenderBackground(context);
    }

    @Inject(
            at = @At("HEAD"),
            //~ if >= 26.1 'render' -> 'extract'
            method = "extractMenuBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIII)V" // used by screens while not in a level
    )
    private void blur$onScreenBackground(GuiGraphicsExtractor context, int i, int j, int k, int l, CallbackInfo ci) {
        blur$onRenderBackground(context);
    }

    @Unique
    private void blur$onRenderBackground(GuiGraphicsExtractor context) {
        // if the screen tries to call this function we can determine the screen had a background, and we can set the
        // background animation to fade-in mode
        Blur.backgroundAlphaAnimation.setTarget(BackgroundAlphaAnimationTarget.FadeIn);

        // also draw a blurred background for forceEnabledScreens that are not also in forceDisabledScreens and only if
        // we can apply blur at all, this must be before we draw the background
        if (BlurConfig.forceEnabledScreens.contains(this.getClass().getCanonicalName()) &&
                !BlurConfig.forceDisabledScreens.contains(this.getClass().getCanonicalName())
                && Blur.canBlur(context)
        ) {
            //~ if >= 26.1 'render' -> 'extract'
            this.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*minecraft.getTimer().getGameTimeDeltaTicks() *//*?}*/);
        }
    }

    //~ if >= 26.1 'render' -> 'extract' {
    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/Screen;extractMenuBackgroundTexture(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;IIFFII)V"),
            method = "extractMenuBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIII)V"
    )
    //~}
    private static void blur$replaceMenuBackground(GuiGraphicsExtractor context, Identifier identifier, int i, int j, float k, float l, int m, int n, Operation<Void> original) {
        if (BlurConfig.useGradient) {
            Blur.renderRotatedGradient(context);  // draw our gradient as background
        } else {
            original.call(context, identifier, i, j, k, l, m, n);  // draw the original background
        }
    }

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fillGradient(IIIIII)V"
            ),
            //~ if >= 26.1 'render' -> 'extract'
            method = "extractTransparentBackground"
    )
    private static void blur$replaceTransparentBackground(GuiGraphicsExtractor context, int i, int j, int k, int l, int m, int n, Operation<Void> original) {
        if (BlurConfig.useGradient) {
            Blur.renderRotatedGradient(context);  // draw our gradient as background
        } else {
            original.call(context, i, j, k, l, m, n);  // draw the original background
        }
    }
    //~}
}
