package com.tterrag.blur.mixin;

import net.minecraft.client.MinecraftClient;
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

    @Inject(at = @At("HEAD"), method = "tick")
    private void blur$reloadShader(CallbackInfo ci) {
        if (this.getClass().toString().toLowerCase(Locale.ROOT).contains("midnightconfigscreen") && this.client != null) {
            Blur.onScreenChange(this.client.currentScreen);
        }
    }

    @ModifyConstant(
            method = "Lnet/minecraft/class_437;method_25420(Lnet/minecraft/class_4587;)V",
            constant = @Constant(intValue = -1072689136))
    private int blur$getFirstBackgroundColor(int color) {
        return Blur.getBackgroundColor(false, client.currentScreen);
    }

    @ModifyConstant(
            method = "Lnet/minecraft/class_437;method_25420(Lnet/minecraft/class_4587;)V",
            constant = @Constant(intValue = -804253680))
    private int blur$getSecondBackgroundColor(int color) {
        return Blur.getBackgroundColor(true, client.currentScreen);
    }
}
