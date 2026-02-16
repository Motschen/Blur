package eu.midnightdust.blur.config;

import com.google.common.collect.Lists;
import eu.midnightdust.blur.Blur;
import eu.midnightdust.lib.config.*;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static java.lang.Math.*;

public class BlurConfig extends MidnightConfig {
    public static final String ANIMATIONS = "animations";
    public static final String STYLE = "style";
    public static final String SCREENS = "screens";
    @Entry @Hidden public static int configVersion = 2;

    @Comment(category = SCREENS, centered = true)
    public static Comment _general;
    @Entry(category = SCREENS)
    public static boolean blurContainers = true;
    @Entry(category = SCREENS)
    public static boolean blurBooks = true;
    @Entry(category = SCREENS)
    public static boolean blurSigns = true;
    @Entry(category = SCREENS)
    public static boolean blurCommandBlocks = true;
    @Entry(category = SCREENS)
    public static boolean blurTitleScreen = false;
    @Entry(category = SCREENS)
    public static boolean darkenTitleScreen = false;
    @Comment(category = SCREENS, centered = true)
    public static Comment _advanced;
    @Entry(category = SCREENS)
    public static boolean showScreenID = false;
    @Entry(category = SCREENS) // Screens where the vanilla blur effect should be force enabled
    public static List<String> forceEnabledScreens = Lists.newArrayList("dev.emi.emi.screen.RecipeScreen");
    @Entry(category = SCREENS) // Screens where the vanilla blur effect should be force disabled
    public static List<String> forceDisabledScreens = Lists.newArrayList("net.irisshaders.iris.gui.screen.ShaderPackScreen");

    @Comment(category = STYLE, centered = true)
    public static Comment _gradient;
    @Entry(category = STYLE)
    public static boolean useGradient = true;
    @Condition(requiredOption = "useGradient", visibleButLocked = true)
    @Entry(category = STYLE, isColor = true, width = 7, min = 7)
    public static String gradientStart = "#000000";
    @Condition(requiredOption = "useGradient", visibleButLocked = true)
    @Entry(category = STYLE, isSlider = true, min = 0, max = 255)
    public static int gradientStartAlpha = 75;
    @Condition(requiredOption = "useGradient", visibleButLocked = true)
    @Entry(category = STYLE, isColor = true, width = 7, min = 7)
    public static String gradientEnd = "#000000";
    @Condition(requiredOption = "useGradient", visibleButLocked = true)
    @Entry(category = STYLE, isSlider = true, min = 0, max = 255)
    public static int gradientEndAlpha = 75;
    @Condition(requiredOption = "useGradient", visibleButLocked = true)
    @Entry(category = STYLE, isSlider = true, min = 0, max = 360)
    public static int gradientRotation = 0;
    @Entry(category = STYLE)
    public static boolean rainbowMode = false;

    @Comment(category = ANIMATIONS, centered = true)
    public static Comment _animations;
    @Entry(category = ANIMATIONS, min = 0, max = 2000, isSlider = true)
    public static int fadeTimeMillis = 300;
    @Entry(category = ANIMATIONS, min = 0, max = 2000, isSlider = true)
    public static int fadeOutTimeMillis = 300;
    @Entry(category = ANIMATIONS)
    public static BlurConfig.Easing blurAnimationCurve = Easing.FLAT;
    @Entry(category = ANIMATIONS)
    public static BlurConfig.Easing backgroundAnimationCurve = Easing.FLAT;

    public enum Easing {
        // Based on https://gist.github.com/dev-hydrogen/21a66f83f0386123e0c0acf107254843
        // Thank you very much!

        FLAT(x -> x),
        SINE(x -> 0.5 - cos(x * PI) / 2),
        QUAD(x -> x * x),
        CUBIC(x -> x * x * x),
        QUART(x -> x * x * x * x),
        QUINT(x -> x * x * x * x * x),
        EXPO(x -> x == 0 ? 0 : pow(2, 10 * x - 10)),
        CIRC(x -> 1 - sqrt(1 - pow(x, 2))),
        BACK(x -> 2.70158 * x * x * x - 1.70158 * x * x),
        ELASTIC(x -> x == 0 ? 0 : x == 1 ? 1 : -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * ((2 * PI) / 3)));

        final Function<Double, Number> function;

        Easing(Function<Double, Number> function) {
            this.function = function;
        }
        public Double apply(Double x) {
            return function.apply(x).doubleValue();
        }
    }

    @Override
    public void onTabInit(String tabName, MidnightConfigListWidget list, MidnightConfigScreen screen) {
        if (Objects.equals(tabName, STYLE)) {
            EntryInfo centered = new EntryInfo(null, Blur.MOD_ID);
            centered.comment = new Comment(){
                @Override
                public boolean centered() {
                    return true;
                }
                public Class<? extends Annotation> annotationType() {return null;}
                public String category() {return "";}
                public String name() {return "";}
                public String url() {return "";}
                public String requiredMod() {return "";}
            };
            RadiusSliderWidget slider = new RadiusSliderWidget(screen.width - 185, 0, 150, 20);

            SpriteIconButton resetButton = SpriteIconButton.builder(Component.translatable("controls.reset"), (button -> {
                Minecraft.getInstance().options.menuBackgroundBlurriness().set(5);
                screen.updateList();
            }), true).sprite(Identifier.fromNamespaceAndPath("midnightlib","icon/reset"), 12, 12).size(20, 20).build();
            resetButton.setPosition(screen.width - 205 + 150 + 25, 0);
            slider.resetButton = resetButton;
            slider.updateMessage();

            list.addButton(Lists.newArrayList(), Component.translatable("%s.midnightconfig._blur".formatted(Blur.MOD_ID)), centered);
            list.addButton(Lists.newArrayList(slider, resetButton), Component.translatable("%s.midnightconfig.radius".formatted(Blur.MOD_ID)), new EntryInfo(null, Blur.MOD_ID));
        }
    }

    public static class RadiusSliderWidget extends AbstractSliderButton {
        SpriteIconButton resetButton;
        public RadiusSliderWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Component.empty(), Minecraft.getInstance().options.menuBackgroundBlurriness().get() / 20d);
        }
        public void updateMessage() {
            this.setMessage(Component.nullToEmpty(String.valueOf(Minecraft.getInstance().options.menuBackgroundBlurriness().get())));
            if (resetButton != null) resetButton.active = Minecraft.getInstance().options.menuBackgroundBlurriness().get() != 5;
        }

        public void applyValue() {
            Minecraft.getInstance().options.menuBackgroundBlurriness().set(Double.valueOf(this.value * 20).intValue());
        }
    }
}