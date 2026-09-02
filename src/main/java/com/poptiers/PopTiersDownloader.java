package com.poptiers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PopTiersDownloader {

    public static final Map<String, String> tiersMap = new ConcurrentHashMap<>();
    private static final String NPOINT_URL = "https://api.npoint.io/0270be7978d3ebfe4412";

    public static void loadTiers() {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(NPOINT_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
                connection.setConnectTimeout(8000);
                connection.setReadTimeout(8000);

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    StringBuilder sb = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }
                    }

                    String json = sb.toString();
                    tiersMap.clear();

                    // Регулярное выражение надежно ищет любые "key": "value" в JSON
                    Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");
                    Matcher matcher = pattern.matcher(json);

                    while (matcher.find()) {
                        String username = matcher.group(1).trim().toLowerCase();
                        String tier = matcher.group(2).trim();
                        if (!username.isEmpty() && !tier.isEmpty()) {
                            tiersMap.put(username, tier);
                        }
                    }

                    System.out.println("[PopTiers] Успешно загружено тиров: " + tiersMap.size());
                } else {
                    System.err.println("[PopTiers] Ошибка HTTP от npoint: " + responseCode);
                }
            } catch (Exception e) {
                System.err.println("[PopTiers] Исключение при скачивании тиров:");
                e.printStackTrace();
            }
        });
    }

    public static String getTierForPlayer(String username) {
        if (username == null || username.isEmpty()) return null;
        return tiersMap.get(username.trim().toLowerCase());
    }
}
