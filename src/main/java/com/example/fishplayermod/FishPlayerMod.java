package com.example.fishplayermod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.fish.SalmonEntity;
import net.minecraft.util.math.vector.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("fishplayermod")
public class FishPlayerMod {

    public FishPlayerMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;

        // クライアント側でのみ処理
        if (player.level.isClientSide) {
            renderFishOnPlayer(player);
        }
    }

    private void renderFishOnPlayer(PlayerEntity player) {
        // サーモンの生成
        SalmonEntity salmon = new SalmonEntity(EntityType.SALMON, player.level);
        salmon.setPos(player.getX(), player.getY(), player.getZ());
        salmon.setYRot(player.getYRot());
        salmon.setXRot(player.getXRot());

        // レンダリング関連の取得
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super SalmonEntity> renderer = dispatcher.getRenderer(salmon);

        // 描画準備
        MatrixStack matrixStack = new MatrixStack();
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().renderBuffers().bufferSource();
        float partialTicks = Minecraft.getInstance().getFrameTime();
        int light = 15728880; // 通常の明るさ

        // 描画実行
        renderer.render(salmon, salmon.getYRot(), partialTicks, matrixStack, buffer, light);

        buffer.endBatch();
    }
}
