package com.poptiers.mixin;

import com.poptiers.PopTiersDownloader;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), argsOnly = true)
    private Text modifyChatMessage(Text message) {
        if (message == null || PopTiersDownloader.tiersMap.isEmpty()) {
            return message;
        }

        String rawText = message.getString();

        for (Map.Entry<String, String> entry : PopTiersDownloader.tiersMap.entrySet()) {
            String playerName = entry.getKey();
            String tier = entry.getValue();

            if (containsIgnoreCase(rawText, playerName)) {
                MutableText prefix = Text.literal(tier + " ");
                return prefix.append(message);
            }
        }

        return message;
    }

    private boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0) return false;

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp) continue;

            if (src.regionMatches(true, i, what, 0, length)) {
                return true;
            }
        }
        return false;
    }
}
