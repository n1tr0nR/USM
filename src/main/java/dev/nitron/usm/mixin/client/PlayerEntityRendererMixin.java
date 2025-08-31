package dev.nitron.usm.mixin.client;

import dev.nitron.usm.content.cca.SpacePlayerComponent;
import dev.nitron.usm.content.cca.USMEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
    , at = @At("HEAD"), cancellable = true)
    private void usm$zerogmovement(AbstractClientPlayerEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci){
        this.model.child = livingEntity.isBaby();
        float l = livingEntity.getScale();

        matrixStack.scale(l, l, l);
        SpacePlayerComponent component = USMEntityComponents.PLAYER.get(livingEntity);

        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-livingEntity.getYaw()));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(livingEntity.getPitch()));

        float n = this.getAnimationProgress(livingEntity, g);
        this.setupTransforms(livingEntity, matrixStack, n, 0, g, l);
        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(livingEntity, matrixStack, g);
        boolean bl = this.isVisible(livingEntity);
        boolean bl2 = !bl && !livingEntity.isInvisibleTo(MinecraftClient.getInstance().player);

        livingEntity.limbAnimator.updateLimbs((float) livingEntity.getVelocity().lengthSquared(), 1.0F);
        float o = 0.0F;
        float p = 0.0F;
        if (!livingEntity.hasVehicle() && livingEntity.isAlive()) {
            o = livingEntity.limbAnimator.getSpeed(g);
            p = livingEntity.limbAnimator.getPos(g);
            if (livingEntity.isBaby()) {
                p *= 3.0F;
            }

            if (o > 1.0F) {
                o = 1.0F;
            }
        }

        this.model.animateModel(livingEntity, p, o, g);
        this.model.setAngles(livingEntity, p, o, n, 0, 0);

        RenderLayer renderLayer = this.getRenderLayer(livingEntity, bl, bl2, false);
        if (renderLayer != null) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
            int q = getOverlay(livingEntity, this.getAnimationCounter(livingEntity, g));
            this.model.render(matrixStack, vertexConsumer, i, q, bl2 ? 654311423 : -1);
        }

        if (!livingEntity.isSpectator()) {
            for(FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> featureRenderer : this.features) {
                featureRenderer.render(matrixStack, vertexConsumerProvider, i, livingEntity, p, o, g, n, 0, 0);
            }
        }

        ci.cancel();
    }
}


