package com.example.fishplayermod;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("fishplayermod")
public class FishPlayerMod {

    public FishPlayerMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PlayerEntity player = event.getPlayer();
        // 条件を簡単に true にして常に鮭表示
        if (!player.world.isRemote) return; // サーバー側は処理しない

        event.setCanceled(true); // プレイヤーの描画をキャンセル

        SalmonEntity salmon = new SalmonEntity(EntityType.SALMON, player.world);
        salmon.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
        salmon.rotationYaw = player.rotationYaw;
        salmon.rotationPitch = player.rotationPitch;

        EntityRendererManager renderManager = Minecraft.getInstance().getRenderManager();
        EntityRenderer<? super SalmonEntity> renderer = renderManager.getRenderer(salmon);

        if (renderer != null) {
            MatrixStack matrixStack = event.getMatrixStack();
            IRenderTypeBuffer buffer = event.getBuffers();
            int light = event.getLight();
            float partialTicks = event.getPartialRenderTick();

            renderer.render(salmon, partialTicks, matrixStack, buffer, light);
        }
    }
}
