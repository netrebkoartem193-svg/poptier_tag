package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListHud.class)
public abstract class PlayerListHudRenderMixin {

    @Shadow
    public abstract Text getPlayerName(PlayerListEntry entry);

    @Inject(method = "getPlayerName", at = @At("HEAD"), cancellable = true)
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
            // 1. Сначала отменяем выполнения стандартной логики
            // 2. Получаем официальное имя (или фоллбэк на чистый ник из профиля)
            Text originalText = entry.getDisplayName();
            Text baseName = (originalText != null) ? originalText : Text.literal(username);

            // 3. Форматируем тир и склеиваем в один итоговый MutableText
            String formattedTier = PopTiersColor.getFormattedTier(tier);
            MutableText fullDisplayName = Text.literal(formattedTier + " ").append(baseName.copy());

            // 4. Возвращаем результат в точку HEAD (до того, как игра успеет вернуть null)
            cir.setReturnValue(fullDisplayName);
        }
    }
}
