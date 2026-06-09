package eu.midnightdust.blur.animations;

import org.jetbrains.annotations.Nullable;

public record AnimationState<E extends Enum<E> & IEnumAnimationTarget>(float timeState, float startValue, float currentValue, @Nullable E target) {

}
