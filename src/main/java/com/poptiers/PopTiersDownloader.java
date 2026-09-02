package com.poptiers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PopTiersDownloader {

    // Сделано public static final, чтобы ссылки в миксинах на tiersMap работали прямо
    public static final Map<String, String> tiersMap = new HashMap<>();
    
    // URL списка Rentry
    private static final String RENTRY_URL = "https://rentry.co/my-poptiers-list/raw";

    public static void loadTiers() {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(RENTRY_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                if (connection.getResponseCode() == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        tiersMap.clear();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            line = line.trim();
                            if (line.isEmpty() || line.startsWith("#")) continue;

                            String[] parts = line.split(":");
                            if (parts.length >= 2) {
                                String username = parts[0].trim().toLowerCase();
                                String tier = parts[1].trim();
                                tiersMap.put(username, tier);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static String getTierForPlayer(String username) {
        if (username == null) return null;
        return tiersMap.get(username.trim().toLowerCase());
    }
}
