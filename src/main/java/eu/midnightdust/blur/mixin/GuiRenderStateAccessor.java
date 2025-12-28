package eu.midnightdust.blur.mixin;

//? if > 1.21.5 {
import net.minecraft.client.gui.render.state.GuiRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GuiRenderState.class)
public interface GuiRenderStateAccessor {
    @Accessor
    int getFirstStratumAfterBlur();
}
//?} else {
//import eu.midnightdust.core.MidnightLib;
//import org.spongepowered.asm.mixin.Mixin;
//
//@Mixin(MidnightLib.class)
//public interface GuiRenderStateAccessor {
//}
//?}