package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignEditScreen.class)
public class MixinAbstractSignEditScreen extends Screen {
    protected MixinAbstractSignEditScreen(Component title) {
        super(title);
    }

    // forces sign edit screen to also render a blurred background
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
    @Inject(method = /*? if >= 26.1 {*/"extractRenderState"/*?} else if > 1.21.5 {*//*"render"*//*?} else {*/ /*"renderBackground" *//*?}*/, at = @At(value = "TAIL"))
    //~ if >= 26.1 'render' -> 'extract' {
    private void blur$extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (BlurConfig.reduceInGameBlur) Blur.reducedBlur = true;
        if (BlurConfig.blurSigns && Blur.canBlur(context)) this.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
    }
    //~}
    //~}
}
