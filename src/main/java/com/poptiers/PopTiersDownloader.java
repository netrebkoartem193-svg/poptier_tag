package com.poptiers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PopTiersDownloader {
    public static final Map<String, String> tiersMap = new ConcurrentHashMap<>();
    private static final String RENTRY_URL = "https://rentry.co/poptier123/raw";

    public static void fetchTiers() {
        Thread thread = new Thread(() -> {
            try {
                URL url = new URI(RENTRY_URL).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                
                // Устанавливаем User-Agent, чтобы Rentry не блокировал запрос (403 Forbidden)
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                try (InputStreamReader reader = new InputStreamReader(connection.getInputStream())) {
                    Type type = new TypeToken<Map<String, String>>() {}.getType();
                    Map<String, String> result = new Gson().fromJson(reader, type);
                    
                    if (result != null) {
                        tiersMap.clear();
                        for (Map.Entry<String, String> entry : result.entrySet()) {
                            if (entry.getKey() != null && entry.getValue() != null) {
                                tiersMap.put(entry.getKey().toLowerCase(), entry.getValue());
                            }
                        }
                        System.out.println("[PopTiers] Успешно загружено тиров: " + tiersMap.size());
                    }
                }
            } catch (Exception e) {
                System.err.println("[PopTiers] Ошибка при загрузке тиров: " + e.getMessage());
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public static String getTierForPlayer(String playerName) {
        if (playerName == null || tiersMap.isEmpty()) return null;
        return tiersMap.get(playerName.toLowerCase());
    }
}
