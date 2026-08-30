package com.poptiers.mixin;

import com.poptiers.PopTiersDownloader;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void modifyTabDisplayName(CallbackInfoReturnable<Text> cir) {
        try {
            PlayerListEntry entry = (PlayerListEntry) (Object) this;
            if (entry.getProfile() != null) {
                String playerName = entry.getProfile().getName();
                if (playerName != null && PopTiersDownloader.tiersMap.containsKey(playerName)) {
                    String tier = PopTiersDownloader.tiersMap.get(playerName);
                    if (tier != null) {
                        Text currentName = cir.getReturnValue();
                        Text prefix = Text.literal(tier + " ");
                        cir.setReturnValue(prefix.copy().append(currentName != null ? currentName : Text.literal(playerName)));
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }
}
