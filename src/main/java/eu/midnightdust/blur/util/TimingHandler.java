package eu.midnightdust.blur.util;

public class TimingHandler {
    public long prevNanoTime = -1;

    public long getDeltaTimeNanos() {
        // calculate deltaTime
        long currentNanoTime = System.nanoTime();
        long deltaTime = 0;
        if (prevNanoTime > 0) deltaTime = System.nanoTime() - prevNanoTime;
        prevNanoTime = currentNanoTime;
        return deltaTime;
    }
}
