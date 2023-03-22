package com.tterrag.blur.mixin;

import com.tterrag.blur.Blur;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud extends DrawableHelper {
    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow private MinecraftClient client;
    @Inject(at = @At("HEAD"), method = "render")
    public void blur$onRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (client.currentScreen == null && client.world != null && Blur.start > 0) {
            this.fillGradient(matrices, 0, 0, this.scaledWidth, this.scaledHeight, Blur.getBackgroundColor(false, null), Blur.getBackgroundColor(true, null));
        }
    }
}
