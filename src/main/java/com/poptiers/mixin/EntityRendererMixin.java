package com.poptiers.mixin;

import com.poptiers.PopTiersDownloader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void injectTierPrefix(CallbackInfoReturnable<Text> cir) {
        try {
            PlayerEntity player = (PlayerEntity) (Object) this;
            String name = player.getGameProfile() != null ? player.getGameProfile().getName() : null;

            if (name != null && PopTiersDownloader.tiersMap != null && PopTiersDownloader.tiersMap.containsKey(name)) {
                String tier = PopTiersDownloader.tiersMap.get(name);
                if (tier != null) {
                    Text original = cir.getReturnValue();
                    MutableText formatted = Text.literal(tier + " ").append(original != null ? original : Text.literal(name));
                    cir.setReturnValue(formatted);
                }
            }
        } catch (Exception ignored) {
        }
    }
}
