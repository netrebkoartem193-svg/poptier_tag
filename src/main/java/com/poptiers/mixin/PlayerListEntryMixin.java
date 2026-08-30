package com.poptiers.mixin;

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
    private void injectTabTierPrefix(CallbackInfoReturnable<Text> cir) {
        try {
            PlayerListEntry entry = (PlayerListEntry) (Object) this;
            if (entry.getProfile() != null) {
                String name = entry.getProfile().getName();
                String tier = PopTiersDownloader.getTierForPlayer(name);
                
                if (tier != null) {
                    Text current = cir.getReturnValue();
                    // Если сервер не прислал DisplayName, берем чистый ник игрока
                    Text baseName = (current != null) ? current : Text.literal(name);
                    
                    // Формируем итоговый текст с префиксом
                    MutableText formatted = Text.literal(tier + " ").append(baseName);
                    cir.setReturnValue(formatted);
                }
            }
        } catch (Exception ignored) {
        }
    }
}
