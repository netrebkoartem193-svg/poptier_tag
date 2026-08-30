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
public class EntityRendererMixin {

    @ModifyVariable(method = "renderLabelIfPresent", at = @At("HEAD"), argsOnly = true)
    private Text modifyPlayerNametag(Text text, Entity entity) {
        if (entity instanceof PlayerEntity player) {
            String playerName = player.getGameProfile().getName();
            
            if (PopTiersDownloader.tiersMap.containsKey(playerName)) {
                String tier = PopTiersDownloader.tiersMap.get(playerName);
                return Text.literal(tier + " " + playerName);
            }
        }
        return text;
    }
}
