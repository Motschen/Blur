package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Shadow
    @Final
    private Minecraft minecraft;

    // calls `onRender` at the start of a render pass
    @Inject(at = @At("HEAD"), method = "render")
    public void blur$onRender(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        Blur.onRender();
    }

    // calls `onRenderEnd` at the end of a render pass
    @Inject(at = @At("TAIL"), method = "render")
    public void blur$onRenderEnd(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        Blur.onRenderEnd();
    }

    // before beginning to render a screen, if we're in a level and there's no screens, we draw our own background
    @ModifyVariable(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;",
                    shift = At.Shift.BEFORE,  // do we need this shift?
                    opcode = Opcodes.GETFIELD
            ),
            ordinal = 0
    )
    public GuiGraphics blur$beforeRenderScreen1(GuiGraphics context) {
        if (minecraft.screen == null && minecraft.level != null) {
            //? if > 1.21.5 {
            context.nextStratum();  // actually draw the background on EVERYTHING behind it
            //?}
            Blur.renderBackground(context);
        }
        return context;
    }
}