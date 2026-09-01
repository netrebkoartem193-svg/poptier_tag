package com.poptiers;

import java.util.HashMap;
import java.util.Map;

public class PopTiersDownloader {
    // Храним ники всегда в нижнем регистре
    private static final Map<String, String> playerTiers = new HashMap<>();

    public static void savePlayerTier(String username, String tier) {
        if (username != null && tier != null) {
            playerTiers.put(username.trim().toLowerCase(), tier.trim());
        }
    }

    public static String getTierForPlayer(String username) {
        if (username == null) return null;
        // При поиске тоже переводим входной ник в нижний регистр
        return playerTiers.get(username.trim().toLowerCase());
    }
}
