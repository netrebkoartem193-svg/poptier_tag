package com.poptiers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public class PopTiersDownloader {

    public static final Map<String, String> tiersMap = new ConcurrentHashMap<>();
    
    // Твоя прямая ссылка на npoint.io
    private static final String NPOINT_URL = "https://api.npoint.io/0270be7978d3ebfe4412";

    public static void loadTiers() {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(NPOINT_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        tiersMap.clear();
                        StringBuilder jsonBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            jsonBuilder.append(line);
                        }

                        // Разбираем JSON {"nick": "tier", ...}
                        String rawJson = jsonBuilder.toString().trim();
                        if (rawJson.startsWith("{") && rawJson.endsWith("}")) {
                            rawJson = rawJson.substring(1, rawJson.length() - 1);
                            String[] entries = rawJson.split(",");

                            for (String entry : entries) {
                                String[] keyValue = entry.split(":");
                                if (keyValue.length == 2) {
                                    String username = keyValue[0].replace("\"", "").trim().toLowerCase();
                                    String tier = keyValue[1].replace("\"", "").trim();
                                    if (!username.isEmpty() && !tier.isEmpty()) {
                                        tiersMap.put(username, tier);
                                    }
                                }
                            }
                        }
                    }
                    System.out.println("[PopTiers] Успешно загружено тиров с npoint: " + tiersMap.size());
                } else {
                    System.err.println("[PopTiers] Ошибка npoint! Код ответа: " + responseCode);
                }
            } catch (Exception e) {
                System.err.println("[PopTiers] Ошибка при загрузке тиров:");
                e.printStackTrace();
            }
        });
    }

    public static String getTierForPlayer(String username) {
        if (username == null || username.isEmpty()) return null;
        return tiersMap.get(username.trim().toLowerCase());
    }
}
