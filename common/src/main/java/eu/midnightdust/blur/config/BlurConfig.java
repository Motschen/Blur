package eu.midnightdust.blur.config;

import com.google.common.collect.Lists;
import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;
import java.util.function.Function;

import static java.lang.Math.*;

public class BlurConfig extends MidnightConfig {
    public static final String ANIMATIONS = "animations";
    public static final String STYLE = "style";
    public static final String SCREENS = "screens";
    @Entry @Hidden public static int configVersion = 2;

    @Entry(category = SCREENS)
    public static boolean blurContainers = true;
    @Entry(category = ANIMATIONS, min = 0, max = 2000, isSlider = true)
    public static int fadeTimeMillis = 300;
    @Entry(category = ANIMATIONS, min = 0, max = 2000, isSlider = true)
    public static int fadeOutTimeMillis = 300;
    @Entry(category = ANIMATIONS)
    public static BlurConfig.Easing animationCurve = Easing.FLAT;
    @Entry(category = STYLE)
    public static boolean useGradient = true;
    @Entry(category = STYLE, isColor = true, width = 7, min = 7)
    public static String gradientStart = "#000000";
    @Entry(category = STYLE, isSlider = true, min = 0, max = 255)
    public static int gradientStartAlpha = 75;
    @Entry(category = STYLE, isColor = true, width = 7, min = 7)
    public static String gradientEnd = "#000000";
    @Entry(category = STYLE, isSlider = true, min = 0, max = 255)
    public static int gradientEndAlpha = 75;
    @Entry(category = STYLE, isSlider = true, min = 0, max = 360)
    public static int gradientRotation = 0;
    @Entry(category = STYLE)
    public static boolean rainbowMode = false;
    @Entry(category = SCREENS) // Screens where Blur+ should not apply transition effects (mostly dynamically blurred screens)
    public static List<String> excludedScreens = Lists.newArrayList("net.irisshaders.iris.gui.screen.ShaderPackScreen");
    @Entry(category = SCREENS) // Screens where the vanilla blur effect should be force enabled
    public static List<String> forceEnabledScreens = Lists.newArrayList();
    @Entry(category = SCREENS) // Screens where the vanilla blur effect should be force disabled
    public static List<String> forceDisabledScreens = Lists.newArrayList();

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
}