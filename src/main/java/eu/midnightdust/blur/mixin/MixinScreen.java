package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import eu.midnightdust.blur.Blur;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Shadow protected Minecraft minecraft;
    @Shadow protected abstract void renderBlurredBackground(/*? if > 1.21.5 {*/ GuiGraphics context /*?} else if <= 1.21.1 {*/ /*float delta *//*?}*/);

    @Inject(at = @At("HEAD"), method = "renderBlurredBackground")
    public void blur$onRenderBlurredBackground(CallbackInfo ci) {
        // if the screen tries to call `renderBlurredBackground` we can determine the screen had a blurred background,
        // and we can set the blur animation to fade-in mode
        Blur.blurAnimation.enabled = true;
    }

    @WrapMethod(method = {
            "renderMenuBackground(Lnet/minecraft/client/gui/GuiGraphics;)V", // used by screens while not in a level
            "renderTransparentBackground(Lnet/minecraft/client/gui/GuiGraphics;)V"  // used by screens while in a level
    })
    private void blur$replaceScreenBackground(GuiGraphics context, Operation<Void> original) {
        // if the screen tries to call this function we can determine the screen had a background, and we can set the
        // background animation to fade-in mode
        Blur.backgroundAnimation.enabled = true;

        // also draw a blurred background for forceEnabledScreens that are not also in forceDisabledScreens and only if
        // we can apply blur at all, this must be before we draw the background
        if (BlurConfig.forceEnabledScreens.contains(this.getClass().getCanonicalName()) &&
                !BlurConfig.forceDisabledScreens.contains(this.getClass().getCanonicalName())
                && Blur.canBlur(context)
        ) {
            this.renderBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*minecraft.getTimer().getGameTimeDeltaTicks() *//*?}*/);
        }

        if (BlurConfig.useGradient) {
            Blur.renderRotatedGradient(context); // draw our gradient as background
        } else {
            original.call(context); // draw the original background
        }
    }
}
