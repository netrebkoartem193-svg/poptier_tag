package com.poptiers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class PopTiersClient implements ClientModInitializer {
    private int ticks = 0;

    @Override
    public void onInitializeClient() {
        PopTiersDownloader.fetchTiers();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ticks++;
            if (ticks >= 1200) { // каждые 60 секунд
                ticks = 0;
                PopTiersDownloader.fetchTiers();
            }
        });
    }
}
