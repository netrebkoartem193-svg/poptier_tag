package com.poptiers;

public class PopTiersColor {

    public static String getFormattedTier(String tier) {
        if (tier == null || tier.isEmpty()) {
            return "";
        }

        // Удаляем все лишние скобки и пробелы из исходной базы
        String cleanTier = tier.replaceAll("[\\[\\]]", "").trim().toUpperCase();

        String colorCode;

        if (cleanTier.contains("RLT")) {
            colorCode = "§7"; // Серый
        } else if (cleanTier.contains("LT")) {
            colorCode = "§b"; // Голубой
        } else if (cleanTier.contains("HT")) {
            colorCode = "§c"; // Красный
        } else if (cleanTier.contains("RHT")) {
            colorCode = "§8"; // Тёмно-серый
        } else {
            colorCode = "§f"; // Белый
        }

        return colorCode + "[" + cleanTier + "]§r";
    }
}
