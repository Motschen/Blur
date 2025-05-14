package eu.midnightdust.blur.config;

import com.google.common.collect.Lists;
import eu.midnightdust.blur.Blur;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

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
    public static boolean blurTitleScreen = false;
    @Condition(requiredOption = "blurTitleScreen", visibleButLocked = true)
    @Entry(category = SCREENS)
    public static boolean darkenTitleScreen = false;
    @Comment(category = SCREENS, centered = true)
    public static Comment _advanced;
    @Entry(category = SCREENS) // Screens where Blur+ should not apply transition effects (mostly dynamically blurred screens)
    public static List<String> excludedScreens = Lists.newArrayList("net.irisshaders.iris.gui.screen.ShaderPackScreen");
    @Entry(category = SCREENS) // Screens where the vanilla blur effect should be force enabled
    public static List<String> forceEnabledScreens = Lists.newArrayList("dev.emi.emi.screen.RecipeScreen");
    @Entry(category = SCREENS) // Screens where the vanilla blur effect should be force disabled
    public static List<String> forceDisabledScreens = Lists.newArrayList();

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
    public static BlurConfig.Easing animationCurve = Easing.FLAT;

    public enum Easing {
        // Based on https://gist.github.com/dev-hydrogen/21a66f83f0386123e0c0acf107254843
        // Thank you very much!

        FLAT(x -> x,                                    x -> x),
        SINE(x -> 1 - cos(x * PI) / 2,                  x -> sin(x * PI) / 2),
        QUAD(x -> x * x,                                x -> 1 - (1 - x) * (1 - x)),
        CUBIC(x -> x * x * x,                           x -> 1 - pow(1 - x, 3)),
        QUART(x -> x * x * x * x,                       x -> 1 - pow(1 - x, 4)),
        QUINT(x -> x * x * x * x * x,                   x -> 1 - pow(1 - x, 5)),
        EXPO(x -> x == 0 ? 0 : pow(2, 10 * x - 10),     x -> x == 1 ? 1 : 1 - pow(2, -10 * x)),
        CIRC(x -> 1 - sqrt(1 - pow(x, 2)),              x -> sqrt(1 - pow(x - 1, 2))),
        BACK(x -> 2.70158 * x * x * x - 1.70158 * x * x,x -> 1 + 2.70158 * pow(x - 1, 3) + 1.70158 * pow(x - 1, 2)),
        ELASTIC(x -> x == 0 ? 0 : x == 1 ? 1 : -pow(2, 10 * x - 10) * sin((x * 10 - 10.75) * ((2 * PI) / 3)), x -> x == 0 ? 0 : x == 1 ? 1 : pow(2, -10 * x) * sin((x * 10 - 0.75) * ((2 * PI) / 3)) + 1);

        final Function<Double, Number> functionIn;
        final Function<Double, Number> functionOut;

        Easing(Function<Double, Number> functionIn, Function<Double, Number> functionOut) {
            this.functionIn = functionIn;
            this.functionOut = functionOut;
        }
        public Double apply(Double x, boolean in) {
            if (in) return functionIn.apply(x).doubleValue();
            return functionOut.apply(x).doubleValue();
        }
    }
    private static GameOptions options;

    @Override
    public void onTabInit(String tabName, MidnightConfigListWidget list, MidnightConfigScreen screen) {
        options = MinecraftClient.getInstance().options;
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

            TextIconButtonWidget resetButton = TextIconButtonWidget.builder(Text.translatable("controls.reset"), (button -> {
                options.getMenuBackgroundBlurriness().setValue(5);
                screen.updateList();
            }), true).texture(Identifier.of("midnightlib","icon/reset"), 12, 12).dimension(20, 20).build();
            resetButton.setPosition(screen.width - 205 + 150 + 25, 0);
            slider.resetButton = resetButton;
            slider.updateMessage();

            list.addButton(Lists.newArrayList(), Text.translatable("blur.midnightconfig._blur"), centered);
            list.addButton(Lists.newArrayList(slider, resetButton), Text.translatable("blur.midnightconfig.radius"), new EntryInfo(null, Blur.MOD_ID));
        }
    }

    public static class RadiusSliderWidget extends SliderWidget {
        TextIconButtonWidget resetButton;
        public RadiusSliderWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Text.empty(), options.getMenuBackgroundBlurrinessValue() / 20d);
        }
        public void updateMessage() {
            this.setMessage(Text.of(String.valueOf(options.getMenuBackgroundBlurrinessValue())));
            if (resetButton != null) resetButton.active = options.getMenuBackgroundBlurrinessValue() != 5;
        }

        public void applyValue() {
            options.getMenuBackgroundBlurriness().setValue(Double.valueOf(this.value * 20).intValue());
        }
    }
}