package eu.midnightdust.blur.mixin;

import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
//? if > 1.21.5
import net.minecraft.client.gui.render.state.GuiRenderState;

@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    //? if > 1.21.5 {
    @Accessor @Final
    GuiRenderState getGuiRenderState();
    //?}
}
