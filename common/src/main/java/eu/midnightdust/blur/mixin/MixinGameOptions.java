package eu.midnightdust.blur.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameOptions.class)
public abstract class MixinGameOptions {
    @Shadow @Final private SimpleOption<Integer> menuBackgroundBlurriness;
    @Shadow @Final private SimpleOption<Double> chatLineSpacing;

    @Redirect(method = "<init>", at = @At(value = "NEW", target = "net/minecraft/client/option/SimpleOption$ValidatingIntSliderCallbacks", ordinal = 2))
    private SimpleOption.ValidatingIntSliderCallbacks blur$increaseMaxBlurriness(int minInclusive, int maxInclusive) {
        if (this.menuBackgroundBlurriness == null && this.chatLineSpacing != null)
            return new SimpleOption.ValidatingIntSliderCallbacks(minInclusive, 20);
        return new SimpleOption.ValidatingIntSliderCallbacks(minInclusive, maxInclusive);
    }
}
