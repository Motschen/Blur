package eu.midnightdust.blur.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.BlurInfo;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;getMenuBackgroundBlurriness()I"))
    private int blur$modifyRadius(Options instance, Operation<Integer> original) {
        int radius = instance.getMenuBackgroundBlurriness();
        if (!BlurInfo.screenChanged && BlurInfo.start >= 0) // Only update the progress after all tests have been completed
            Blur.updateProgress(BlurInfo.screenHasBlur);
        return (int) (radius * BlurInfo.progress);
    }
}
