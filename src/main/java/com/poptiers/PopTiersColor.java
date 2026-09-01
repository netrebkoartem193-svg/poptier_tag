package com.poptiers;

public class PopTiersColor {

    public static String getFormattedTier(String tier) {
        if (tier == null) return "";
        
        String cleanTier = tier.trim().toUpperCase();
        String colorCode;

        switch (cleanTier) {
            // --- RETIRED LOW TIERS (RLT5 - RLT1) — Серо-голубые оттенки ---
            case "RLT5": colorCode = "§8"; break; // Тёмно-серый
            case "RLT4": colorCode = "§7"; break; // Серый
            case "RLT3": colorCode = "§7"; break; // Серый
            case "RLT2": colorCode = "§7"; break; // Серый
            case "RLT1": colorCode = "§b"; break; // Светло-голубой (приглушенный)

            // --- LOW TIERS (LT5 - LT1) — от светло-голубого к синему/фиолетовому ---
            case "LT5":  colorCode = "§b"; break; // Светло-голубой (Aqua)
            case "LT4":  colorCode = "§3"; break; // Тёмно-аквамариновый
            case "LT3":  colorCode = "§9"; break; // Светло-синий
            case "LT2":  colorCode = "§1"; break; // Тёмно-синий
            case "LT1":  colorCode = "§d"; break; // Светло-фиолетовый / Пурпурный

            // --- HIGH TIERS (HT5 - HT1) — от жёлтого к тёмно-красному ---
            case "HT5":  colorCode = "§e"; break; // Жёлтый
            case "HT4":  colorCode = "§6"; break; // Оранжевый / Золотой
            case "HT3":  colorCode = "§c"; break; // Светло-красный
            case "HT2":  colorCode = "§4"; break; // Тёмно-красный
            case "HT1":  colorCode = "§4§l"; break; // Тёмно-красный жирный

            // --- RETIRED HIGH TIERS (RHT5 - RHT1) — Серо-красные / Тёмно-серые оттенки ---
            case "RHT5": colorCode = "§7"; break; // Серый
            case "RHT4": colorCode = "§7"; break; // Серый
            case "RHT3": colorCode = "§8"; break; // Тёмно-серый
            case "RHT2": colorCode = "§8"; break; // Тёмно-серый
            case "RHT1": colorCode = "§8§l"; break; // Тёмно-серый жирный

            // Старый общий вариант RHT (на всякий случай)
            case "RHT":  colorCode = "§7"; break;

            // Если тир неизвестен
            default:     colorCode = "§f"; break; // Белый
        }

        return colorCode + "[" + cleanTier + "]§r";
    }
}
