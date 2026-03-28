package eu.midnightdust.blur.util;

import com.google.common.base.Strings;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.awt.*;
import java.util.Objects;

/// [source](https://github.com/amiralimollaei/Vibrant-F3/blob/7e3bd29dcae3ca6d41d672c11eef856d2c08995a/src/client/java/io/github/amitalimollaei/mods/vibrantf3/debug/DebugLine.java#L53C1-L69C6)
public class DebugHudRenderer {
    public static final int DEFAULT_COLOR = 0x90E0E0E0;
    public static final int BACKGROUND_ALPHA = 0x90;
    public static final int LINE_HEIGHT = 9;

    //~ if >= 26.1 'GuiGraphics' -> 'GuiGraphicsExtractor'
    public static void renderLine(GuiGraphicsExtractor graphics, Font font, String text, int lineY, Color color, boolean left) {
        if (Strings.isNullOrEmpty(text)) return;
        Objects.requireNonNull(font);

        int lineWidth = font.width(text);
        int lineX = left ? 2 : graphics.guiWidth() - 2 - lineWidth;
        Color lineColor = new Color(color == null ? DEFAULT_COLOR : color.getRGB());
        Color backgroundColor = new Color(
                (int) Math.max(lineColor.getRed() * 0.25F, 0),
                (int) Math.max(lineColor.getGreen() * 0.25F, 0),
                (int) Math.max(lineColor.getBlue() * 0.25F, 0),
                BACKGROUND_ALPHA
        );
        graphics.fill(lineX - 1, lineY - 1, lineX + lineWidth + 1, lineY + LINE_HEIGHT - 1, backgroundColor.getRGB());
        //~ if >= 26.1 '.drawString' -> '.text'
        graphics.text(font, text, lineX, lineY, lineColor.getRGB(), false);
    }
}
