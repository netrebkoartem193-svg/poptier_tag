package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Map;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @ModifyVariable(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), argsOnly = true)
    private Text injectTierToChat(Text message) {
        if (message == null || PopTiersDownloader.tiersMap.isEmpty()) {
            return message;
        }

        String rawContent = message.getString();

        for (Map.Entry<String, String> entry : PopTiersDownloader.tiersMap.entrySet()) {
            String playerName = entry.getKey();
            String tier = entry.getValue();

            // Проверяем наличие ника в сообщении без учёта регистра
            if (rawContent.toLowerCase().contains(playerName.toLowerCase())) {
                String formattedTier = PopTiersColor.getFormattedTier(tier);
                
                // Создаем модифицированный текст с префиксом тира
                MutableText prefix = Text.literal(formattedTier + " ");
                return prefix.append(message);
            }
        }

        return message;
    }
}
