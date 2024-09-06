package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.BlurInfo;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "setScreen",
            at = @At(value = "FIELD",
                     target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;",
                     opcode = Opcodes.PUTFIELD))
    private void blur$onScreenOpen(Screen newScreen, CallbackInfo info) {
        if (BlurInfo.lastScreenChange < System.currentTimeMillis() - 100) { // For some reason, in certain scenarios the screen is set to a new one multiple times in a tick. We want to avoid that.

            // Here, we reset all tests, to check if the new screen has blur and/or a background
            BlurInfo.reset();

            // Manually activate the onScreenChange method when all screens are closed (in-game)
            if (newScreen == null) Blur.onScreenChange();
        }
    }
}
