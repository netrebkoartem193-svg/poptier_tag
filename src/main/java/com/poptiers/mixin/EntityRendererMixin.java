package com.poptiers.mixin;

import com.poptiers.PopTiersDownloader;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {

    @ModifyVariable(
        method = "renderLabelIfPresent",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Text modifyPlayerNametag(Text text, Entity entity) {
        if (entity instanceof PlayerEntity player) {
            try {
                String playerName = player.getGameProfile() != null ? player.getGameProfile().getName() : null;
                if (playerName != null && PopTiersDownloader.tiersMap.containsKey(playerName)) {
                    String tier = PopTiersDownloader.tiersMap.get(playerName);
                    if (tier != null) {
                        Text prefix = Text.literal(tier + " ");
                        return text != null ? prefix.copy().append(text) : prefix.copy().append(player.getDisplayName());
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return text;
    }
}
