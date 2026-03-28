package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookViewScreen.class)
public class MixinBookViewScreen extends Screen {
    protected MixinBookViewScreen(Component title) {
        super(title);
    }

    // forces book view screen to also render a blurred background
    //~ if >= 26.1 'render' -> 'extract' {
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
    @Inject(
            method = "extractBackground",
            at = @At(
                    value = "INVOKE",
                    target = /*? if > 1.21.8 {*/ "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V" /*?} else {*/ /*"Lnet/minecraft/client/gui/screens/inventory/BookViewScreen;extractTransparentBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V" *//*?}*/
            )
    )
    private void blur$extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (BlurConfig.blurBooks && Blur.canBlur(context)) this.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
    }
    //~}
    //~}
}
