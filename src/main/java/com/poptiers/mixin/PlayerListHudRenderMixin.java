package com.poptiers.mixin;

import com.poptiers.PopTiersDownloader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerListHud.class)
public class PlayerListHudRenderMixin {

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"
        )
    )
    private int redirectDrawText(DrawContext context, net.minecraft.client.font.TextRenderer textRenderer, Text text, int x, int y, int color) {
        if (text != null && !PopTiersDownloader.tiersMap.isEmpty()) {
            String rawString = text.getString();
            
            for (var entry : PopTiersDownloader.tiersMap.entrySet()) {
                String playerName = entry.getKey();
                String tier = entry.getValue();
                
                // Если строчка ТАБа содержит ник из Rentry, пририсовываем тир спереди
                if (rawString.toLowerCase().contains(playerName)) {
                    Text newText = Text.literal(tier + " ").append(text);
                    return context.drawTextWithShadow(textRenderer, newText, x, y, color);
                }
            }
        }
        return context.drawTextWithShadow(textRenderer, text, x, y, color);
    }
}
