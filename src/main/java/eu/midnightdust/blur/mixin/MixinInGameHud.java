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
            Blur.screenHasBlur = true;
            Blur.onRender(context);

            if (Blur.fadeTimeState < 0.001F) return;  // we have faded out at this point and don't need to render anything

            //? if > 1.21.5 {
            if (Blur.canBlur(context))
                context.blurBeforeThisStratum();
            //?} else {
            /*minecraft.gameRenderer.processBlurEffect(/^? if <= 1.21.1 {^/ /^tickCounter.getGameTimeDeltaTicks() ^//^?}^/);
            *///?}

            Blur.renderRotatedGradient(context, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
        }
    }
}
