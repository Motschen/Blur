package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.midnightdust.blur.BlurInfo;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import eu.midnightdust.blur.Blur;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {
    @Shadow @Final protected Text title;
    @Shadow protected MinecraftClient client;
    @Shadow public int width;
    @Shadow public int height;
    @Shadow protected abstract void applyBlur();

    @Inject(at = @At("HEAD"), method = "render")
    public void blur$processScreenChange(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        Blur.onRender(context, width, height, this.client, delta);
    }
    @Inject(at = @At("HEAD"), method = "applyBlur", cancellable = true)
    public void blur$getBlurEnabled(CallbackInfo ci) {
        if (BlurConfig.forceDisabledScreens.contains(this.getClass().getCanonicalName())) {
            ci.cancel(); return;
        }
        if (!BlurConfig.excludedScreens.contains(this.getClass().getCanonicalName()))
            BlurInfo.screenHasBlur = true; // Test if the screen has blur
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/Screen;renderBackgroundTexture(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;IIFFII)V"), method = "renderDarkening(Lnet/minecraft/client/gui/DrawContext;IIII)V")
    private void blur$applyGradient(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, Operation<Void> original) {
        if (BlurConfig.useGradient) {
            blur$renderGradient(context); // Replaces the background texture with a gradient
        } else original.call(context, texture, x, y, u, v, width, height);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fillGradient(IIIIII)V"), method = "renderInGameBackground")
    public void blur$rotatedGradient(DrawContext context, int startX, int startY, int endX, int endY, int colorStart, int colorEnd, Operation<Void> original) {
        blur$renderGradient(context);
    }
    @Unique
    private void blur$renderGradient(DrawContext context) {
        BlurInfo.screenHasBackground = true; // Test if the screen has a background
        if (BlurConfig.forceEnabledScreens.contains(this.getClass().getCanonicalName()))
            this.applyBlur();

        Blur.renderRotatedGradient(context, width, height); // Replaces the default gradient with our rotated one
    }
}
