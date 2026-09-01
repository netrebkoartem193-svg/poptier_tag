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
public abstract class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void injectTierToTabRender(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (entry == null || entry.getProfile() == null) {
            return;
        }

        String username = entry.getProfile().getName();
        if (username == null) {
            return;
        }

        String tier = PopTiersDownloader.getTierForPlayer(username);

        if (tier != null) {
            // cir.getReturnValue() содержит УЖЕ полностью отрендеренное сервером имя (со скорбордами, командами и префиксами)
            Text originalName = cir.getReturnValue();
            
            String formattedTier = PopTiersColor.getFormattedTier(tier);
            
            // Склеиваем тир с уже сформированным именем
            MutableText result = Text.literal(formattedTier + " ").append(originalName.copy());

            cir.setReturnValue(result);
        }
    }
}
