package com.poptiers.mod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PopTiersDownloader {

    // Твоя прямая API-ссылка с npoint.io
    private static final String TIERS_URL = "https://api.npoint.io/e43fdbada1a4dce0fb88";
    
    // Хранилище загруженных тиров (Никнейм -> Префикс/Тир)
    public static final Map<String, String> TIERS_MAP = new HashMap<>();

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

                    TIERS_MAP.clear();
                    if (fetchedMap != null) {
                        for (Map.Entry<String, String> entry : fetchedMap.entrySet()) {
                            // Сохраняем ник в нижнем регистре для удобной проверки
                            TIERS_MAP.put(entry.getKey().toLowerCase(), entry.getValue());
                        }
                        System.out.println("[PopTiers] Успешно загружено тиров: " + TIERS_MAP.size());
                    }
                } else {
                    System.out.println("[PopTiers] Ошибка сервера: HTTP код " + conn.getResponseCode());
                }
            } catch (Exception e) {
                System.out.println("[PopTiers] Ошибка при загрузке данных: " + e.getMessage());
            }
        }).start();
    }

    // Метод для получения префикса по нику
    public static String getTierForPlayer(String playerName) {
        if (playerName == null) return null;
        return TIERS_MAP.get(playerName.toLowerCase());
    }
}
