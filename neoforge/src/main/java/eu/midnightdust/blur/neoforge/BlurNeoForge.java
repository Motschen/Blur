package eu.midnightdust.blur.neoforge;

import eu.midnightdust.blur.Blur;
import eu.midnightdust.blur.util.RainbowColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@Mod(value = Blur.MOD_ID, dist = Dist.CLIENT)
public class BlurNeoForge {
    public BlurNeoForge() {
        Blur.init();
    }

    @EventBusSubscriber(modid = Blur.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
    public static class ClientGameEvents {
        @SubscribeEvent
        public static void endClientTick(ClientTickEvent.Post event) {
            RainbowColor.tick();
        }
    }
}
