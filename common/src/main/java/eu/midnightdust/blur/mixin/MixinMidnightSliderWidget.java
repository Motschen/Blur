package eu.midnightdust.blur.mixin;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MidnightConfig.MidnightSliderWidget.class)
public abstract class MixinMidnightSliderWidget {
    @Shadow(remap = false) @Final private MidnightConfig.EntryInfo info;

    @Inject(at = @At(value = "TAIL"), method = "applyValue")
    private void blur$instantlyApplyRadius(CallbackInfo ci) {
        // TODO: Make more fields in MidnightLib protected instead of private and improve this
        MinecraftClient.getInstance().options.getMenuBackgroundBlurriness().setValue(Integer.parseInt(this.info.toTemporaryValue()));
    }
}
