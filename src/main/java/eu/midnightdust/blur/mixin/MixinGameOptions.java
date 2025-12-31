package eu.midnightdust.blur.mixin;

import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Options.class)
public abstract class MixinGameOptions {
    @Shadow @Final private OptionInstance<Integer> menuBackgroundBlurriness;
    @Shadow @Final private OptionInstance<Double> chatLineSpacing;

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/OptionInstance$IntRange", ordinal = /*? if > 1.21.10 {*/ /*5 *//*?} else if > 1.21.5 {*/ 3/*?} else {*/ /*2 *//*?}*/))
    private OptionInstance.IntRange blur$increaseMaxBlurriness(int minInclusive, int maxInclusive) {
        if (this.menuBackgroundBlurriness == null && this.chatLineSpacing != null)
            return new OptionInstance.IntRange(minInclusive, 20);
        return new OptionInstance.IntRange(minInclusive, maxInclusive);
    }
}
