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
            String prefixWithSpace = formattedTier + " ";

            // Проверяем ТОЛЬКО начало строки. Ник Player1 больше не будет ломать логику.
            if (baseName.getString().startsWith(prefixWithSpace) || baseName.getString().startsWith(tier + " ")) {
                return;
            }

            MutableText fullDisplayName = Text.literal(prefixWithSpace).append(baseName.copy());
            cir.setReturnValue(fullDisplayName);
        }
    }
}
