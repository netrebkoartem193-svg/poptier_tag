package com.poptiers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class PopTiersDownloader {
    public static Map<String, String> tiersMap = new HashMap<>();
    private static final String RENTRY_URL = "https://rentry.co/poptier123/raw";

    public static void fetchTiers() {
        new Thread(() -> {
            try {
                URL url = new URI(RENTRY_URL).toURL();
                InputStreamReader reader = new InputStreamReader(url.openStream());
                Type type = new TypeToken<Map<String, String>>() {}.getType();
                Map<String, String> result = new Gson().fromJson(reader, type);
                if (result != null) {
                    tiersMap = result;
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
