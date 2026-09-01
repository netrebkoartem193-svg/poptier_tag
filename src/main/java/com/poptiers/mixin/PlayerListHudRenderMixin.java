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
    private void injectTierToTabRender(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (entry == null || entry.getProfile() == null) {
            return;
        }

        // Чистый ник из Mojang/Netty профиля
        String cleanUsername = entry.getProfile().getName();
        if (cleanUsername == null || cleanUsername.isEmpty()) {
            return;
        }

        // Ищем тир в базе (поиск теперь регистронезависимый)
        String tier = PopTiersDownloader.getTierForPlayer(cleanUsername);

        if (tier != null) {
            Text original = cir.getReturnValue();
            
            // Если оригинального текста нет (обычные игроки), берём чистый ник
            Text baseName = (original != null) ? original : Text.literal(cleanUsername);

            String formattedTier = PopTiersColor.getFormattedTier(tier);

            MutableText result = Text.literal(formattedTier + " ").append(baseName.copy());
            cir.setReturnValue(result);
        }
    }
}
