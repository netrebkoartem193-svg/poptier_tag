package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void injectTierToTab(CallbackInfoReturnable<Text> cir) {
        PlayerListEntry entry = (PlayerListEntry) (Object) this;
        if (entry.getProfile() == null) {
            return;
        }

        String username = entry.getProfile().getName();
        if (username == null || username.isEmpty()) {
            return;
        }

        String tier = PopTiersDownloader.getTierForPlayer(username);

        if (tier != null) {
            // Если сервер не задал displayName (вернулся null), берем обычный ник
            Text original = cir.getReturnValue();
            Text baseName = (original != null) ? original : Text.literal(username);

            String formattedTier = PopTiersColor.getFormattedTier(tier);
            MutableText fullDisplayName = Text.literal(formattedTier + " ").append(baseName.copy());

            cir.setReturnValue(fullDisplayName);
        }
    }
}
