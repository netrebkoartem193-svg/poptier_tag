package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudRenderMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void injectTierToTab(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (entry == null || entry.getProfile() == null) {
            return;
        }

        String username = entry.getProfile().getName();
        if (username == null || username.isEmpty()) {
            return;
        }

        String tier = PopTiersDownloader.getTierForPlayer(username);

        if (tier != null) {
            Text originalText = cir.getReturnValue();
            Text baseName = (originalText != null) ? originalText : Text.literal(username);

            String formattedTier = PopTiersColor.getFormattedTier(tier);
            String suffixWithSpace = " " + formattedTier;

            // Если тир уже стоит в конце строки — ничего не дублируем
            if (baseName.getString().endsWith(suffixWithSpace) || baseName.getString().endsWith(" " + tier)) {
                return;
            }

            // Добавляем тир в самый конец текста: [Префикс Сервера] [Ник] [Тир]
            MutableText fullDisplayName = baseName.copy().append(Text.literal(suffixWithSpace));

            cir.setReturnValue(fullDisplayName);
        }
    }
}
