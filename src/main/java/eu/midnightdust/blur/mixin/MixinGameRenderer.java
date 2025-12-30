package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if > 1.21.5 {
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Options;
//?} else {
/*import org.spongepowered.asm.mixin.injection.ModifyVariable;
*///?}

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    //? if > 1.21.5 {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getMenuBackgroundBlurriness()I"))
    private int blur$modifyRadius(Options instance, Operation<Integer> original) {
    //?} else {
    /*@ModifyVariable(method = "processBlurEffect", at = @At("STORE"), ordinal = /^? if > 1.21.1 {^/ 0 /^?} else {^/ /^1 ^//^?}^/)
    private float blur$modifyRadius(float radius) {
    *///?}
        //? if > 1.21.5
        int radius = instance.getMenuBackgroundBlurriness();
        return /*? if > 1.21.5 {*/ (int) /*?}*/ (radius * Blur.blurAnimation.fadeProgress);
    }
}