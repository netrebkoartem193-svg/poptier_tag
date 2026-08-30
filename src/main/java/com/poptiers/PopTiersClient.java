package com.poptiers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class PopTiersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Первичная загрузка при старте
        PopTiersDownloader.fetchTiers();

        // Автоматическое обновление при каждом входе на сервер/в мир
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            PopTiersDownloader.fetchTiers();
        });
    }
}
