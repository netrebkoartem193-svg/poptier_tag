package com.poptiers;

import net.fabricmc.api.ClientModInitializer;

public class PopTiersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PopTiersDownloader.loadTiers();
    }
}
