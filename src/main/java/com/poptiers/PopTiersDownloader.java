package com.poptiers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
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
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                StringBuilder builder = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line).append("\n");
                    }
                }

                String rawContent = builder.toString().trim();
                tiersMap.clear();

                // Попытка 1: Парсим как JSON
                if (rawContent.startsWith("{")) {
                    Type type = new TypeToken<Map<String, String>>() {}.getType();
                    Map<String, String> result = new Gson().fromJson(rawContent, type);
                    if (result != null) {
                        for (Map.Entry<String, String> entry : result.entrySet()) {
                            if (entry.getKey() != null && entry.getValue() != null) {
                                tiersMap.put(entry.getKey().toLowerCase(), entry.getValue());
                            }
                        }
                    }
                } else {
                    // Попытка 2: Парсим строчный текст вида "Ник: Тир" или "Ник=Тир"
                    String[] lines = rawContent.split("\n");
                    for (String l : lines) {
                        if (l.contains(":")) {
                            String[] parts = l.split(":", 2);
                            tiersMap.put(parts[0].trim().toLowerCase(), parts[1].trim());
                        } else if (l.contains("=")) {
                            String[] parts = l.split("=", 2);
                            tiersMap.put(parts[0].trim().toLowerCase(), parts[1].trim());
                        }
                    }
                }

                System.out.println("[PopTiers] Успешно загружено тиров: " + tiersMap.size());

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
