package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.client.network.PlayerListEntry;
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
        String username = getProfile().getName();
        String tier = PopTiersDownloader.getTierForPlayer(username);

        if (tier != null) {
            Text originalName = cir.getReturnValue();
            if (originalName == null) {
                originalName = Text.literal(username);
            }

            String formattedTier = PopTiersColor.getFormattedTier(tier);
            Text result = Text.empty().append(originalName).append(Text.literal(" " + formattedTier));

            cir.setReturnValue(result);
        }
    }
}
