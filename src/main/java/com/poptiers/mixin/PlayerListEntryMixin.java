package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {

    @Shadow
    public abstract com.mojang.authlib.GameProfile getProfile();

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void injectTierToTab(CallbackInfoReturnable<Text> cir) {
        if (getProfile() == null) {
            return;
        }

        String username = getProfile().getName();
        if (username == null) {
            return;
        }

        String tier = PopTiersDownloader.getTierForPlayer(username);

        if (tier != null) {
            Text original = cir.getReturnValue();
            
            // Если getDisplayName() равен null, берем чистый ник из профиля
            MutableText baseText = (original != null) ? original.copy() : Text.literal(username);

            String formattedTier = PopTiersColor.getFormattedTier(tier);
            
            // Формируем итоговый текст с тиром перед ником
            MutableText result = Text.literal(formattedTier + " ").append(baseText);

            cir.setReturnValue(result);
        }
    }
}
