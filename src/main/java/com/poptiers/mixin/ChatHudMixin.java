package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.gui.hud.ChatHud;
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

        String messageContent = message.getString();

        for (Map.Entry<String, String> entry : PopTiersDownloader.tiersMap.entrySet()) {
            String playerName = entry.getKey();
            String tier = entry.getValue();

            if (messageContent.toLowerCase().contains(playerName.toLowerCase())) {
                // В ЧАТЕ: Тир В НАЧАЛЕ + Сообщение
                return Text.empty()
                        .append(Text.literal(PopTiersColor.getFormattedTier(tier)))
                        .append(Text.literal(" "))
                        .append(message);
            }
        }

        return message;
    }
}
