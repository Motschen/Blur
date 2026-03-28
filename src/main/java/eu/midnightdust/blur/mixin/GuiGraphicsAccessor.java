package eu.midnightdust.blur.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
//? if > 1.21.5 {
//~ if >= 26.1 'net.minecraft.client.gui.render.state.GuiRenderState' -> 'net.minecraft.client.renderer.state.gui.GuiRenderState'
import net.minecraft.client.renderer.state.gui.GuiRenderState;
//?}

//~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
@Mixin(GuiGraphicsExtractor.class)
public interface GuiGraphicsAccessor {
    //? if > 1.21.5 {
    @Accessor @Final
    GuiRenderState getGuiRenderState();
    //?}
}
