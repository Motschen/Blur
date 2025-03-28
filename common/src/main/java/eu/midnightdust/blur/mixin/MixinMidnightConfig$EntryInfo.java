package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(value = MidnightConfig.EntryInfo.class, remap = false)
public abstract class MixinMidnightConfig$EntryInfo {
    @Shadow @Final public String modid;
    @Shadow @Final public String fieldName;
    @Shadow Object value;

    @Inject(at = @At(value = "TAIL"), method = "updateFieldValue")
    private void blur$instantlyApplyRadius(CallbackInfo ci) {
        if (Objects.equals(modid, Blur.MOD_ID) && Objects.equals(fieldName, "radius"))
            MinecraftClient.getInstance().options.getMenuBackgroundBlurriness().setValue((int) value);
    }
}
