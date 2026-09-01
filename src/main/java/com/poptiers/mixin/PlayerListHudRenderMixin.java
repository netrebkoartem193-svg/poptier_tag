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

    @Inject(
        method = "getPlayerName", 
        at = @At("RETURN"), 
        cancellable = true,
        require = 1
    )
    private void injectTierToTabRender(PlayerListEntry entry, CallbackInfoReturnable<Text> cir) {
        if (entry == null || entry.getProfile() == null) {
            return;
        }

        String username = entry.getProfile().getName();
        if (username == null || username.isEmpty()) {
            return;
        }

        String tier = PopTiersDownloader.getTierForPlayer(username);

        if (tier != null) {
            Text originalName = cir.getReturnValue();
            
            // Если оригинальное имя почему-то null, используем стандартное имя из профиля
            Text baseName = (originalName != null) ? originalName : Text.literal(username);
            
            String formattedTier = PopTiersColor.getFormattedTier(tier);

            MutableText result = Text.literal(formattedTier + " ").append(baseName.copy());
            cir.setReturnValue(result);
        }
    }
}
