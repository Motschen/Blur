package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractCommandBlockEditScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractCommandBlockEditScreen.class)
public class MixinAbstractCommandBlockEditScreen extends Screen {
    protected MixinAbstractCommandBlockEditScreen(Component title) {
        super(title);
    }

    @Inject(method = /*? if >= 26.1 {*/"extractRenderState"/*?} else if > 1.21.5 {*//*"render"*//*?} else {*/ /*"renderBackground" *//*?}*/, at = @At(value = "TAIL"))
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
    //~ if >= 26.1 'render' -> 'extract' {
    private void blur$extractContainerBlur(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta, CallbackInfo ci) { // Applies the blur effect in containers (Inventory, Chest, etc.)
        if (BlurConfig.reduceInGameBlur) Blur.reducedBlur = true;
        if (BlurConfig.blurCommandBlocks && Blur.canBlur(context)) this.extractBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
    }
    //~}
    //~}
}
