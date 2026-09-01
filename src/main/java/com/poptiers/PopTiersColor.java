package com.poptiers;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PopTiersColor {

    public static Text getFormattedTierText(String tier) {
        if (tier == null || tier.isEmpty()) {
            return Text.empty();
        }
        
        // Очищаем от случайных скобок из базы npoint
        String cleanTier = tier.replaceAll("[\\[\\]]", "").trim().toUpperCase();
        
        Formatting color;

        if (cleanTier.contains("RLT")) {
            color = Formatting.GRAY;
        } else if (cleanTier.contains("LT")) {
            color = Formatting.AQUA;
        } else if (cleanTier.contains("HT")) {
            color = Formatting.RED;
        } else if (cleanTier.contains("RHT")) {
            color = Formatting.DARK_GRAY;
        } else {
            color = Formatting.WHITE;
        }

        // Возвращаем правильный цветной компонент Minecraft Text
        return Text.literal("[" + cleanTier + "]").formatted(color);
    }
}
