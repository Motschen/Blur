package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Component title) {
        super(title);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/TitleScreen;renderPanorama(Lnet/minecraft/client/gui/GuiGraphics;F)V"))
    private void blur$renderTitleBlur(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (BlurConfig.blurTitleScreen) {
            Blur.updateFadeAnimation(context);
            if (Blur.canBlur(context)) this.renderBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
            if (BlurConfig.darkenTitleScreen) this.renderMenuBackground(context);
        }
    }
}
