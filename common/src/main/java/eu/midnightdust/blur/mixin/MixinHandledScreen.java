package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.lib.util.PlatformFunctions;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public class MixinHandledScreen extends Screen {
    protected MixinHandledScreen(Component title) {
        super(title);
    }

    @Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V", shift = At.Shift.BEFORE))
    private void blur$renderContainerBlur(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) { // Applies the blur effect in containers (Inventory, Chest, etc.)
        if (BlurConfig.blurContainers) this.renderBlurredBackground(context);
    }
    @Inject(at = @At("HEAD"), method = "render")
    public void blur$processScreenChange(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (PlatformFunctions.getPlatformName().equals("neoforge")) Blur.onRender();
    }
}
