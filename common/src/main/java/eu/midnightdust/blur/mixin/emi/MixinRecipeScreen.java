package eu.midnightdust.blur.mixin.emi;

import dev.emi.emi.screen.RecipeScreen;
import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipeScreen.class)
public class MixinRecipeScreen extends Screen {

    protected MixinRecipeScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("HEAD"), method = "render")
    public void blur$addBlurEffect(DrawContext raw, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (BlurConfig.blurContainers) this.applyBlur(delta);
    }

}
