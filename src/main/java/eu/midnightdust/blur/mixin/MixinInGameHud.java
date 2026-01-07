package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinInGameHud {
    @Final @Shadow private Minecraft minecraft;

    @Inject(at = @At("TAIL"), method = "render")
    public void blur$renderFadeOut(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) { // Adds a fade-out effect when a player is in a world and closes all screens
        if (minecraft.screen == null && minecraft.level != null) {
            Blur.onRender();
            Blur.blurAnimation.enabled = false;
            Blur.backgroundAnimation.enabled = false;
            Blur.renderBackground(context);
        }
    }
}
