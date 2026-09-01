package com.poptiers;

public class PopTiersColor {

    public static String getFormattedTier(String tier) {
        if (tier == null || tier.isEmpty()) {
            return "";
        }

        String cleanTier = tier.replaceAll("[\\[\\]]", "").trim().toUpperCase();

        String colorCode;

        // 1. Retired High Tiers (RHT1 - RHT5)
        if (cleanTier.contains("RHT1") || cleanTier.contains("RHT2")) {
            colorCode = "§d"; // Светло-фиолетовый (Высшие RHT)
        } else if (cleanTier.contains("RHT3") || cleanTier.contains("RHT4") || cleanTier.contains("RHT5")) {
            colorCode = "§5"; // Тёмно-фиолетовый (Низшие RHT)
        } else if (cleanTier.contains("RHT")) {
            colorCode = "§d"; // Запасной фиолетовый
        }
        
        // 2. Retired Low Tiers (RLT1 - RLT5)
        else if (cleanTier.contains("RLT1") || cleanTier.contains("RLT2")) {
            colorCode = "§7"; // Серый (Высшие RLT)
        } else if (cleanTier.contains("RLT3") || cleanTier.contains("RLT4") || cleanTier.contains("RLT5")) {
            colorCode = "§8"; // Тёмно-серый (Низшие RLT)
        } else if (cleanTier.contains("RLT")) {
            colorCode = "§7"; // Запасной серый
        }

        // 3. Активные тиры по лестнице силы (LT5 -> HT5 -> ... -> LT1 -> HT1)
        else if (cleanTier.contains("HT1")) {
            colorCode = "§4"; // Тёмно-красный (ТОП-1)
        } else if (cleanTier.contains("LT1")) {
            colorCode = "§c"; // Красный
        } else if (cleanTier.contains("HT2")) {
            colorCode = "§6"; // Оранжевый
        } else if (cleanTier.contains("LT2")) {
            colorCode = "§e"; // Жёлтый
        } else if (cleanTier.contains("HT3")) {
            colorCode = "§a"; // Светло-зелёный
        } else if (cleanTier.contains("LT3")) {
            colorCode = "§2"; // Тёмно-зелёный
        } else if (cleanTier.contains("HT4")) {
            colorCode = "§b"; // Голубой
        } else if (cleanTier.contains("LT4")) {
            colorCode = "§3"; // Бирюзовый
        } else if (cleanTier.contains("HT5")) {
            colorCode = "§9"; // Синий
        } else if (cleanTier.contains("LT5")) {
            colorCode = "§1"; // Тёмно-синий (Низший)
        } else {
            colorCode = "§f"; // Белый
        }

        return colorCode + "[" + cleanTier + "]§r";
    }
}
