package com.tterrag.blur.mixin;

import com.tterrag.blur.config.BlurConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.tterrag.blur.Blur;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Shadow @Nullable protected MinecraftClient client;

    @Shadow protected TextRenderer textRenderer;

    @Inject(at = @At("HEAD"), method = "tick")
    private void blur$reloadShader(CallbackInfo ci) {
        if (this.getClass().toString().toLowerCase(Locale.ROOT).contains("midnightconfigscreen") && this.client != null) {
            Blur.INSTANCE.onScreenChange(this.client.currentScreen);
        }
    }

    @ModifyConstant(
            method = "Lnet/minecraft/class_437;method_25420(Lnet/minecraft/class_4587;)V",
            constant = @Constant(intValue = -1072689136))
    private int blur$getFirstBackgroundColor(int color) {
        return Blur.INSTANCE.getBackgroundColor(false);
    }

    @ModifyConstant(
            method = "Lnet/minecraft/class_437;method_25420(Lnet/minecraft/class_4587;)V",
            constant = @Constant(intValue = -804253680))
    private int blur$getSecondBackgroundColor(int color) {
        return Blur.INSTANCE.getBackgroundColor(true);
    }
}
