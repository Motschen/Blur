package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if >= 26.1 {
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
//?} else {
/*import eu.midnightdust.blur.Blur;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
*///?}

@Mixin(AbstractContainerScreen.class)
public class MixinAbstractContainerScreen extends Screen {
    protected MixinAbstractContainerScreen(Component title) {
        super(title);
    }

    // forces containers to also render a blurred background (Inventory, Chest, etc.)
    //? if < 26.1 {
    /*@Inject(method = "renderBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/AbstractContainerScreen;renderBg(Lnet/minecraft/client/gui/GuiGraphics;FII)V", shift = At.Shift.BEFORE))
    private void blur$renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (BlurConfig.blurContainers && Blur.canBlur(context)) this.renderBlurredBackground(/^? if > 1.21.5 {^/ context /^?} else if <= 1.21.1 {^/ /^delta ^//^?}^/);
    }
    *///?} else {
    @ModifyReturnValue(method = "isInGameUi", at = @At("RETURN"))
    public boolean blur$isInGameUi(boolean original) {
        if (BlurConfig.blurContainers) return false;
        else return original;
    }
    //?}
}
