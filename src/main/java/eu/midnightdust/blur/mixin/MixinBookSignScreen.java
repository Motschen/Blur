package eu.midnightdust.blur.mixin;
//? if > 1.21.5 {
import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookSignScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BookSignScreen.class)
public class MixinBookSignScreen extends Screen {
    protected MixinBookSignScreen(Component title) {
        super(title);
    }

    // forces book sign screen (1.21.5+) to also render a blurred background
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
    //~ if >= 26.1 'render' -> 'extract' {
    @Inject(
            method = "extractBackground",
            at = @At(
                    value = "INVOKE",
                    target = /*? if > 1.21.8 {*/ "Lnet/minecraft/client/gui/screens/Screen;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V" /*?} else {*/ /*"Lnet/minecraft/client/gui/screens/inventory/BookSignScreen;extractTransparentBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V" *//*?}*/
            )
    )
    private void blur$extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (BlurConfig.blurBooks && Blur.canBlur(context)) this.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
    }
    //~}
    //~}
}
//?} else {
/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public interface MixinBookSignScreen {
}
*///?}