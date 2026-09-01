package com.poptiers.mixin;

import com.poptiers.PopTiersColor;
import com.poptiers.PopTiersDownloader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void injectTierToHead(CallbackInfoReturnable<Text> cir) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        String username = player.getGameProfile().getName();
        String tier = PopTiersDownloader.getTierForPlayer(username);

        if (tier != null) {
            Text originalName = cir.getReturnValue();
            if (originalName == null) {
                originalName = Text.literal(username);
            }

            // НАД ГОЛОВОЙ: Тир В НАЧАЛЕ + Ник
            Text formattedText = Text.empty()
                    .append(Text.literal(PopTiersColor.getFormattedTier(tier)))
                    .append(Text.literal(" "))
                    .append(originalName);

            cir.setReturnValue(formattedText);
        }
    }
}
