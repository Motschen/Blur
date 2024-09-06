package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.BlurInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Final @Shadow private MinecraftClient client;

    @Inject(at = @At("TAIL"), method = "render")
    public void blur$renderFadeOut(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) { // Adds a fade-out effect when a player is in a world and closes all screens
        if (client.currentScreen == null && client.world != null && BlurInfo.start >= 0 && BlurInfo.prevScreenHasBlur) {
            BlurInfo.doTest = false;
            BlurInfo.screenChanged = false;
            this.client.gameRenderer.renderBlur(tickCounter.getTickDelta(true));
            this.client.getFramebuffer().beginWrite(false);

            if (BlurInfo.prevScreenHasBackground) Blur.renderRotatedGradient(context, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        }
    }
}
