package eu.midnightdust.blur.mixin;

//? if > 1.21.5 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//~ if >= 26.1 'net.minecraft.client.gui.render.state.GuiRenderState' -> 'net.minecraft.client.renderer.state.gui.GuiRenderState'
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GuiRenderState.class)
public class GuiRenderStateMixin {
    @Shadow
    private int firstStratumAfterBlur;

    @WrapMethod(method = "blurBeforeThisStratum")
    void blur$dontPanicOverMultipleBlurLayers(Operation<Void> original) {
        // Minecraft doesn't allow blurBeforeThisStratum to be called multiple times per frame,
        // and crashes when this function is called multiple times.
        //
        // we change the behavior, so when blurBeforeThisStratum is called multiple times, it only applies the last blur
        // layer and ignores the ones below it, resulting in the closest final image to what the programmer expects.
        //
        // Fixes all the "can only blur once per frame" crashes

        // reset firstStratumAfterBlur
        firstStratumAfterBlur = Integer.MAX_VALUE;

        // call the original function for compatibility with other mods that modify it
        original.call();
    }
}
//?} else {
/*import eu.midnightdust.core.MidnightLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MidnightLib.class)
public interface GuiRenderStateMixin {
}
*///?}
