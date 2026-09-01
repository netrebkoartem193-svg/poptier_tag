package com.poptiers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PopTiersDownloader {

    private static final String TIERS_URL = "https://api.npoint.io/e43fdbada1a4dce0fb88";
    
    // Переменная называется tiersMap, как требуют миксины
    public static final Map<String, String> tiersMap = new HashMap<>();

    public static void loadTiers() {
        new Thread(() -> {
            try {
                URL url = new URL(TIERS_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestProperty("User-Agent", "Mozilla/5.0");

                if (conn.getResponseCode() == 200) {
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    Gson gson = new Gson();
                    Type type = new TypeToken<Map<String, String>>(){}.getType();
                    
                    Map<String, String> fetchedMap = gson.fromJson(reader, type);
                    reader.close();

                    tiersMap.clear();
                    if (fetchedMap != null) {
                        for (Map.Entry<String, String> entry : fetchedMap.entrySet()) {
                            tiersMap.put(entry.getKey().toLowerCase(), entry.getValue());
                        }
                        System.out.println("[PopTiers] Успешно загружено тиров: " + tiersMap.size());
                    }
                } else {
                    System.out.println("[PopTiers] Ошибка сервера: HTTP код " + conn.getResponseCode());
                }
            } catch (Exception e) {
                System.out.println("[PopTiers] Ошибка при загрузке данных: " + e.getMessage());
            }
        }).start();
    }

    public static String getTierForPlayer(String playerName) {
        if (playerName == null) return null;
        return tiersMap.get(playerName.toLowerCase());
    }
}
