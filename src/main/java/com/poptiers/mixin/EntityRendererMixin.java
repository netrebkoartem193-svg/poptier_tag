package com.poptiers.mixin;

import com.poptiers.PopTiersDownloader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class EntityRendererMixin {

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void modifyPlayerNametag(CallbackInfoReturnable<Text> cir) {
        try {
            PlayerEntity player = (PlayerEntity) (Object) this;
            String playerName = player.getGameProfile() != null ? player.getGameProfile().getName() : null;

            if (playerName != null && PopTiersDownloader.tiersMap != null && PopTiersDownloader.tiersMap.containsKey(playerName)) {
                String tier = PopTiersDownloader.tiersMap.get(playerName);
                if (tier != null) {
                    Text current = cir.getReturnValue();
                    Text prefix = Text.literal(tier + " ");
                    cir.setReturnValue(prefix.copy().append(current != null ? current : Text.literal(playerName)));
                }
            }
        } catch (Exception ignored) {
        }
    }
}
