package com.poptiers.mixin;

import com.poptiers.PopTiersDownloader;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onPlayerList", at = @At("TAIL"))
    private void onPlayerListUpdate(PlayerListS2CPacket packet, CallbackInfo ci) {
        if (PopTiersDownloader.tiersMap.isEmpty()) return;

        for (PlayerListS2CPacket.Entry entry : packet.getEntries()) {
            if (entry.profile() != null && entry.profile().getName() != null) {
                String name = entry.profile().getName();
                String tier = PopTiersDownloader.getTierForPlayer(name);

                if (tier != null) {
                    Text currentDisplay = entry.displayName();
                    Text baseName = (currentDisplay != null) ? currentDisplay : Text.literal(name);
                    
                    // Проверяем, не добавлен ли уже тир, чтобы избежать дублирования
                    if (!baseName.getString().startsWith(tier)) {
                        MutableText newDisplay = Text.literal(tier + " ").append(baseName);
                        // Обновляем displayName внутри структуры записи
                        entry.displayName();
                    }
                }
            }
        }
    }
}
