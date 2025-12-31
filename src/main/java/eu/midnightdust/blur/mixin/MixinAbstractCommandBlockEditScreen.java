package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.GuiGraphics;
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

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void blur$renderContainerBlur(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) { // Applies the blur effect in containers (Inventory, Chest, etc.)
        if (BlurConfig.blurCommandBlocks && Blur.canBlur(context)) this.renderBlurredBackground(/*? if > 1.21.5 {*/ context /*?} else if <= 1.21.1 {*/ /*delta *//*?}*/);
    }
}
