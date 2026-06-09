package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.animations.impl.BackgroundAlphaAnimationTarget;
import eu.midnightdust.blur.animations.impl.BlurRadiusAnimationTarget;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Component title) {
        super(title);
    }

    // force the TitleScreen to also render a blurred background and menu background, but don't fade-in animations
    // unless blurTitleScreen or darkenTitleScreen is enabled
    //~ if >= 26.1 'render' -> 'extract' {
    @Inject(method = "extractBackground", at = @At(value = "HEAD"))
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    private void blur$extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (Blur.canBlur(context)) super.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
        Blur.blurRadiusAnimation.setTarget(BlurConfig.blurTitleScreen ? BlurRadiusAnimationTarget.FadeIn : BlurRadiusAnimationTarget.FadeOut);
        if (BlurConfig.useGradient) super.extractMenuBackground(context);
        Blur.backgroundAlphaAnimation.setTarget(BlurConfig.darkenTitleScreen ? BackgroundAlphaAnimationTarget.FadeIn : BackgroundAlphaAnimationTarget.FadeOut);
    }
    //~}
}
