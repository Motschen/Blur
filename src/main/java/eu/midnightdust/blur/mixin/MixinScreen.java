package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
    @Shadow protected Minecraft minecraft;
    @Shadow public int width;
    @Shadow public int height;
    @Shadow protected abstract void renderBlurredBackground(/*? if > 1.21.5 {*/ GuiGraphics context /*?} else if <= 1.21.1 {*/ /*float delta *//*?}*/);

    @Inject(at = @At("HEAD"), method = "render")
    public void blur$processScreenChange(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Blur.onRender(context);
    }

    @Inject(at = @At("HEAD"), method = "renderBlurredBackground", cancellable = true)
    public void blur$getBlurEnabled(CallbackInfo ci) {
        if (BlurConfig.forceDisabledScreens.contains(this.getClass().getCanonicalName())) {
            ci.cancel(); return;
        }
        if (!BlurConfig.excludedScreens.contains(this.getClass().getCanonicalName())) {
            Blur.blurAnimation.enabled = true; // Test if the screen has blur
        }
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderMenuBackgroundTexture(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIFFII)V"), method = "renderMenuBackground(Lnet/minecraft/client/gui/GuiGraphics;IIII)V")
    private void blur$applyGradient(GuiGraphics context, ResourceLocation texture, int x, int y, float u, float v, int width, int height, Operation<Void> original) {
        if (BlurConfig.useGradient) {
            blur$renderGradient(context); // Replaces the background texture with a gradient
        } else original.call(context, texture, x, y, u, v, width, height);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V"), method = "renderTransparentBackground")
    public void blur$rotatedGradient(GuiGraphics context, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, Operation<Void> original) {
        if (BlurConfig.useGradient) {
            blur$renderGradient(context);
        } else original.call(context, startX, startY, endX, endY, colorStart, colorEnd);
    }
    @Unique
    private void blur$renderGradient(GuiGraphics context) {
        Blur.backgroundAnimation.enabled = true; // Test if the screen has background

        if (BlurConfig.forceEnabledScreens.contains(this.getClass().getCanonicalName()) && Blur.canBlur(context))
            this.renderBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*minecraft.getTimer().getGameTimeDeltaTicks() *//*?}*/);

        Blur.renderRotatedGradient(context, width, height); // Replaces the default gradient with our rotated one
    }
}
