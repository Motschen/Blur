package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import eu.midnightdust.blur.Blur;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Shadow @Final protected Component title;
    @Shadow @Final protected Minecraft minecraft;
    @Shadow public int width;
    @Shadow public int height;
    @Shadow protected abstract void renderBlurredBackground(/*? if > 1.21.5 {*/ GuiGraphics context /*?} else if <= 1.21.1 {*/ /*float delta *//*?}*/);

    @Inject(at = @At("HEAD"), method = "render")
    public void blur$onRenderStart(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Blur.onRender();
    }

    @Inject(at = @At("TAIL"), method = "render")
    public void blur$onRenderEnd(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Blur.onRenderEnd(this.getClass().getCanonicalName());
    }

    @Inject(at = @At("HEAD"), method = "renderBlurredBackground", cancellable = true)
    public void blur$onRenderBlurredBackground(CallbackInfo ci) {
        if (BlurConfig.forceDisabledScreens.contains(this.getClass().getCanonicalName())) {
            ci.cancel(); return;
        }
        if (!BlurConfig.excludedScreens.contains(this.getClass().getCanonicalName())) {
            Blur.blurAnimation.enabled = true; // Test if the screen has blur
        }
    }

    @WrapMethod(method = {
            "renderMenuBackground(Lnet/minecraft/client/gui/GuiGraphics;)V", // used by screens while not in a level
            "renderTransparentBackground(Lnet/minecraft/client/gui/GuiGraphics;)V"  // used by screens while in a level
    })
    private void blur$replaceMenuBackground(GuiGraphics context, Operation<Void> original) {
        if (!BlurConfig.useGradient || BlurConfig.forceDisabledScreens.contains(this.getClass().getCanonicalName())) {
            original.call(context); return; // draw the original background
        }
        if (!BlurConfig.excludedScreens.contains(this.getClass().getCanonicalName())) {
            Blur.backgroundAnimation.enabled = true; // Test if the screen has background
            blur$renderRotatedGradient(context); // draw our gradient as background
        }
    }

    @Unique
    private void blur$renderRotatedGradient(GuiGraphics context) {
        if (BlurConfig.forceEnabledScreens.contains(this.getClass().getCanonicalName()))
            this.renderBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*minecraft.getTimer().getGameTimeDeltaTicks() *//*?}*/);
        Blur.renderRotatedGradient(context, width, height); // Replaces the default gradient with our rotated one
    }
}
