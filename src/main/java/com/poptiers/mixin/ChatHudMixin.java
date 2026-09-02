package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;
import java.util.regex.Pattern;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), argsOnly = true)
    private Text modifyChatMessage(Text message) {
        if (message == null || PopTiersDownloader.tiersMap.isEmpty()) {
            return message;
        }

        String rawString = message.getString();
        String updatedString = rawString;
        boolean changed = false;

        for (Map.Entry<String, String> entry : PopTiersDownloader.tiersMap.entrySet()) {
            String username = entry.getKey();
            String tier = entry.getValue();

            if (updatedString.toLowerCase().contains(username.toLowerCase())) {
                String formattedTier = PopTiersColor.getFormattedTier(tier);
                updatedString = updatedString.replaceAll("(?i)\\b" + Pattern.quote(username) + "\\b", formattedTier + " $0");
                changed = true;
            }
        }

        return changed ? Text.literal(updatedString) : message;
    }
}
