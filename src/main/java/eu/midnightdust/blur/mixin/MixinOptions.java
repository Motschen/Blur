package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import eu.midnightdust.blur.Blur;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Options.class)
public abstract class MixinOptions {
    @Shadow @Final private OptionInstance<Integer> menuBackgroundBlurriness;
    @Shadow @Final private OptionInstance<Double> chatLineSpacing;

    // increases the menu blurriness slider's maximum allowed value
    @Redirect(
            method = "<init>",
            at = @At(value = "NEW",
                    target = "net/minecraft/client/OptionInstance$IntRange",
                    ordinal = /*? if > 1.21.10 {*/ 5 /*?} else if > 1.21.5 {*/ /*3*//*?} else {*/ /*2 *//*?}*/
            )
    )
    private OptionInstance.IntRange blur$increaseMaxBlurriness(int minInclusive, int maxInclusive) {
        if (this.menuBackgroundBlurriness == null && this.chatLineSpacing != null)  // do we need this condition?
            return new OptionInstance.IntRange(minInclusive, 20);
        return new OptionInstance.IntRange(minInclusive, maxInclusive);
    }


    // applies our blur radius coefficient to getMenuBackgroundBlurriness method
    @WrapMethod(method = "getMenuBackgroundBlurriness")
    private int blur$applyMenuBackgroundBlurCoefficient(Operation<Integer> original) {
        return (int) (original.call() * Blur.blurAnimation.progress);
    }
}
