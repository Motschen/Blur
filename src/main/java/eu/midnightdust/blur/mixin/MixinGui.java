package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;

//? if >= 26.2 {
import net.minecraft.client.gui.Gui;
//?} else {
/*import net.minecraft.client.renderer.GameRenderer;
*///?}

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >= 26.2 {

@Mixin(Gui.class)
/*?} else {*/
/*@Mixin(GameRenderer.class)
*//*?}*/
public class MixinGui {
    @Shadow
    @Final
    private Minecraft minecraft;

    // calls `onRender` at the start of a render pass
    @Inject(
            at = @At("HEAD"),
            method = /*? if < 26.1 {*//* "render" *//*? } else if < 26.2 {*/ /*"extract" *//*? } else { */"extractRenderState" /*?}*/
    )
    public void blur$onRender(DeltaTracker deltaTracker, /*? if >= 26.2 { */ boolean shouldRenderLevel, /*? }*/ boolean resourcesLoaded, CallbackInfo ci) {
        Blur.onRender();
    }

    // calls `onRenderEnd` at the end of a render pass
    @Inject(
            at = @At("TAIL"),
            method = /*? if < 26.1 {*//* "render" *//*? } else if < 26.2 {*/ /*"extract" *//*? } else { */"extractRenderState" /*?}*/
    )
    public void blur$onRenderEnd(DeltaTracker deltaTracker, /*? if >= 26.2 { */ boolean shouldRenderLevel, /*? }*/ boolean resourcesLoaded, CallbackInfo ci) {
        Blur.onRenderEnd();
    }

    // before beginning to render a screen, if we're in a level and there's no screens, we draw our own background
    @ModifyVariable(
            method = /*? if < 26.1 {*//* "render" *//*? } else if < 26.2 {*/ /*"extractGui" *//*? } else { */"extractRenderState" /*?}*/,
            at = @At(
                    value = "FIELD",
                    target = /*? if >= 26.2 {*/
                                     "Lnet/minecraft/client/gui/Gui;screen:Lnet/minecraft/client/gui/screens/Screen;",
                             /*? } else { */
                                     /*"Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;",
                             *//*? }*/
                    shift = At.Shift.BEFORE,  // do we need this shift?
                    opcode = Opcodes.GETFIELD
            ),
            ordinal = 0
    )
    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    public GuiGraphicsExtractor blur$beforeRenderScreen1(GuiGraphicsExtractor context) {
        if (Blur.getCurrentScreen() == null && minecraft.level != null) {
            Blur.renderBackground(context);
        }
        return context;
    }
}