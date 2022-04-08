package com.tterrag.blur;

import com.tterrag.blur.config.BlurConfig;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class Blur implements ClientModInitializer {

    public static final String MODID = "blur";
    public static List<String> defaultExclusions = new ArrayList<>();

    private long start;
    public int colorFirst, colorSecond;

    private final ManagedShaderEffect blur = ShaderEffectManager.getInstance().manage(new Identifier(MODID, "shaders/post/fade_in_blur.json"),
            shader -> shader.setUniformValue("Radius", (float) BlurConfig.radius));
    private final Uniform1f blurProgress = blur.findUniform1f("Progress");

    public static final Blur INSTANCE = new Blur();

    @Override
    public void onInitializeClient() {
        defaultExclusions.add(ChatScreen.class.getName());
        defaultExclusions.add("com.replaymod.lib.de.johni0702.minecraft.gui.container.AbstractGuiOverlay$UserInputGuiScreen");
        defaultExclusions.add("ai.arcblroth.projectInception.client.InceptionInterfaceScreen");
        defaultExclusions.add("net.optifine.gui.GuiChatOF");
        defaultExclusions.add("io.github.darkkronicle.advancedchatcore.chat.AdvancedChatScreen");
        BlurConfig.init("blur", BlurConfig.class);

        ShaderEffectRenderCallback.EVENT.register((deltaTick) -> {
            if (start > 0) {
                blurProgress.set(getProgress());
                blur.render(deltaTick);
            }
        });
    }

    private boolean doFade = false;
    public void onScreenChange(Screen newGui) {
        if (MinecraftClient.getInstance().world != null) {
            boolean excluded = newGui == null || BlurConfig.blurExclusions.contains(newGui.getClass().getName());
            if (!excluded) {
                blur.setUniformValue("Radius", (float) BlurConfig.radius);
                colorFirst = Integer.parseUnsignedInt(String.valueOf(BlurConfig.gradientStartColor), 16);
                colorSecond = Integer.parseUnsignedInt(String.valueOf(BlurConfig.gradientEndColor), 16);
                if (doFade) {
                    start = System.currentTimeMillis();
                    doFade = false;
                }
            } else {
                start = -1;
                doFade = true;
            }
        }
    }

    private float getProgress() {
        return Math.min((System.currentTimeMillis() - start) / (float) BlurConfig.fadeTimeMillis, 1);
    }

    public int getBackgroundColor(boolean second) {
        int color = second ? colorSecond : colorFirst;
        int a = color >>> 24;
        int r = (color >> 16) & 0xFF;
        int b = (color >> 8) & 0xFF;
        int g = color & 0xFF;
        float prog = INSTANCE.getProgress();
        a *= prog;
        r *= prog;
        g *= prog;
        b *= prog;
        return a << 24 | r << 16 | b << 8 | g;
    }
}
