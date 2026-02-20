package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import eu.midnightdust.blur.Blur;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Options.class)
public abstract class MixinOptions {

    // increases the menu blurriness slider's maximum allowed value
    @ModifyArg(
            method = "<init>",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/OptionInstance$IntRange;<init>(II)V",
                    ordinal = /*? if > 1.21.10 {*/ 3 /*?} else {*/ /*2 *//*?}*/
            ),
            index = 1  // to modify the 2nd integer (e.g. the max value)
    )
    private int blur$increaseMaxBlurriness(int maxInclusive) {
        return 20;
    }


    // applies our blur radius coefficient to getMenuBackgroundBlurriness method
    @ModifyReturnValue(method = "getMenuBackgroundBlurriness", at = @At(value = "RETURN"))
    private int blur$applyMenuBackgroundBlurCoefficient(int original) {
        return (int) (original * Blur.blurRadiusAnimation.getProgress());
    }
}
