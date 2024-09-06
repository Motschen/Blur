package eu.midnightdust.blur.fabric;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.util.RainbowColor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class BlurFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Blur.init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> RainbowColor.tick());
    }
}
