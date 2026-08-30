package com.poptiers.mixin;

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
public class PlayerListHudMixin {

    @Inject(method = "getPlayerName", at = @At("RETURN"), cancellable = true)
    private void injectTierToHud(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (entry != null && entry.getProfile() != null) {
            String name = entry.getProfile().getName();
            String tier = PopTiersDownloader.getTierForPlayer(name);
            if (tier != null) {
                Text currentText = cir.getReturnValue();
                Text baseText = (currentText != null) ? currentText : Text.literal(name);
                MutableText formatted = Text.literal(tier + " ").append(baseText);
                cir.setReturnValue(formatted);
            }
        }
    }
}
