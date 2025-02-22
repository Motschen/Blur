package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MidnightConfig.MidnightConfigScreen.class)
public abstract class MixinMidnightConfigScreen {
    @Inject(at = @At(value = "INVOKE", target = "Lcom/google/gson/Gson;fromJson(Ljava/io/Reader;Ljava/lang/Class;)Ljava/lang/Object;", shift = At.Shift.AFTER), remap = false, method = "loadValues")
    private void blur$syncRadius(CallbackInfo ci) {
        BlurConfig.radius = MinecraftClient.getInstance().options.getMenuBackgroundBlurrinessValue();
    }
}