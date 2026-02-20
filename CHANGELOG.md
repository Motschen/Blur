# Blur+ v6.2.0
- REI and JEI recipe screens will now be blurred by default
- Fix gradient colors being decoded incorrectly

Huge thanks to [@amiralimollaei](https://github.com/amiralimollaei) for submitting the following changes as a [pull request](https://github.com/Motschen/Blur/pull/157):
- Fix background gradient fading out on some screens that shouldn't fade out (world creation screen, statistics screen, etc.)
- refactor all animations with more abstractions that makes it possible to have more than 2 states in the animations in the future
- Removed `AnimationHandler.java` and `RainbowColor.java` as we now implement all animations in the `animations.impl` package
- Removed `TimingHandler.java` as we now use Minecraft's built-in timer for all animations
- Added `AbstractAnimationHandler.java`, `AnimationState.java`, and `IAnimationHandler.java`, and their implementations `FadeAnimationState.java`, `GradientAnimationState.java`, `FadeAnimationHandler.java` and `GradientAnimationHandler.java`
- Added `DebugHudRenderer.java` for rendering debug lines
- Added a new animation for enabling or disabling rainbow mode in the config
- Added a debug HUD (option in the configuration screen) that shows the ID of the current screen in the top left corner of the game window

# Blur+ v6.1.0
Huge thanks to [@amiralimollaei](https://github.com/amiralimollaei) for submitting these changes as a [pull request](https://github.com/Motschen/Blur/pull/156), once again!

Changes So Far:
- Fixed screen flickering & flashing in edge cases
- Every property about a screen is now gathered within a single frame,
  previous versions would not be able to determine the properties of a screen within one frame and
  thus had slight flickering issues where the fade-out animation would be triggered for one frame where it shouldn't
  (e.g. switching between two screens that both have blurred backgrounds),
  likewise the fade animations all happened with one frame of delay, this is now fixed,
  improving responsiveness as well.
- `Blur.onRender` and `Blur.onRenderEnd` are now only called once per render pass, and log useful information when something is wrong
- `Blur.onScreenChange` and it's Mixin is deleted as we don't need that information anymore
- The timer for fade animation now uses time in nanoseconds, to be able to more accurately work with higher frame rates, the timer is also shared between all animations instead of being instantiated per animation
- Fixed a bug where the background gradient color would dim to black when fading out, which would cause flickers with bright background colors
- Added more informative comments, renamed variables and classes for better clearance and refactored parts of the code for better readability
- Try to not render the background gradient more than once per frame, even if the screen calls `renderBackground` multiple times.
- Fix the menu blurriness slider not showing an accurate value sometimes
- don't replace the entire background texture of screens, only replace the darkening textures.
- uses a chain-able mixin for applying menu blur radius coefficient

# Blur+ v6.0.0
Huge thanks to [@amiralimollaei](https://github.com/amiralimollaei) for submitting these changes as a [pull request](https://github.com/Motschen/Blur/pull/149)!  
Many longstanding issues have now finally been fixed – Blur is now smoother than ever :D

**Key Improvements & Fixes:**
*   **Revamped Blur Animation:** Complete overhaul of the blur animation calculation logic, including runtime detection of blurred screens for improved compatibility.
*   **Stabilized Fade Animations:** Resolved an issue where fade animations desynchronized during rapid screen transitions (e.g., holding E or Esc).  This ensures smooth and consistent fade-in/out effects.
*   **Eliminated Frame Skipping/Resets:** Fixed bugs that caused frames of the fade animation to be skipped or animations to unexpectedly reset. Now provides a consistently continuous animation.
*   **Independent Blur & Gradient Control:** Decoupled the background blurriness animation and background gradient animation, allowing for separate and independent control over each.
*   **"Force Disabled" Screens:** Deprecated the "excluded screens" configuration, Use "force disabled screens" for clearer and more intuitive control.
*   **New Animations for Title Screens:** Added animations for the "Blur Title Screen" and "Darken Title Screen" options, leveraging the new decoupled animation functionality.
*   **Enabled Blur on Ignored Screens:** Added blur to the book edit/view/sign screens, command block edit screen, and sign edit screen, with configurable options in the settings.
*   **Performance Optimization:**  Improved performance by preventing unnecessary drawing when animations are completely faded out.
*   **Improved Mod Compatibility:** Increased compatibility with other mods and modded scenarios by utilizing simpler, more chainable Mixins.
*   **Fixed Blur Slider Issue on 1.21.11:** Fixed a bug where the blur slider didn't work correctly past 10 in version 1.21.11.
*   **Truly Configure Gradient:**  Ensures that disabling the gradient completely removes any instance where Blur+'s special background gradient would be drawn, instead falling back to the original screen background.
*   **Eliminated Flickering:** Fixed all known instances of flickering, including the title screen, excluded screens, force-disabled screens, and force-enabled screens.

### Blur+ v5.3.2
- **always** check if blur effect is applicable
  - Should resolve all remainng instances of the `Can only blur once per frame` crash

### Blur+ v5.3.1
- Fix crash `java.lang.IllegalStateException: Can only blur once per frame` that occurred in edge cases

## Blur+ v5.3.0
- Switch to Stonecutter build system, which allows us to support multiple versions of Minecraft at the same time
  - Currently supported versions: 1.21.1, 1.21.5, 1.21.8, 1.21.10, 1.21.11 (on Fabric & NeoForge)
- Migrate to the official Mojang mappings, making the codebase future-proof
- Fix config not being saved correctly
  - This was hard to pinpoint, but I finally did it :)  
    Sorry for the long waiting time!