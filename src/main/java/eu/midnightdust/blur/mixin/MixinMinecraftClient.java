package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {

    @Inject(method = "setScreen",
            at = @At(value = "FIELD",
                     target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;",
                     opcode = Opcodes.PUTFIELD))
    private void blur$onScreenOpen(Screen newScreen, CallbackInfo info) {
        Blur.onScreenChange(newScreen);
    }
}
