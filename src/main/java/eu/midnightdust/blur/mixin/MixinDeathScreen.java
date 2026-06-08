package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class MixinDeathScreen extends Screen {
    protected MixinDeathScreen(Component title) {
        super(title);
    }

    // forces death screen to also render a blurred background
    //~ if >= 26.1 'render' -> 'extract' {
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
    @Inject(
            method = "extractBackground",
            at = @At(
                    value = "HEAD"
            )
    )
    private void blur$extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (BlurConfig.blurDeathScreen && Blur.canBlur(context)) this.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
    }
    //~}
    //~}

    @Mixin(DeathScreen.TitleConfirmScreen.class)
    public static class MixinTitleConfirmScreen extends ConfirmScreen {
        public MixinTitleConfirmScreen(BooleanConsumer callback, Component title, Component message) {
            super(callback, title, message);
        }

        // forces death screen to also render a blurred background
        //~ if >= 26.1 'render' -> 'extract' {
        //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
        @Inject(
                method = "extractBackground",
                at = @At(
                        value = "HEAD"
                )
        )
        private void blur$extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
            if (BlurConfig.blurDeathScreen && Blur.canBlur(context)) this.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
        }
        //~}
        //~}
    }
}
