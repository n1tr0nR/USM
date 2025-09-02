package dev.nitron.usm.client.render.entity;

import dev.nitron.usm.USM;
import dev.nitron.usm.client.USMClient;
import dev.nitron.usm.client.models.FighterShipModel;
import dev.nitron.usm.content.entity.FighterShipEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class FighterShipEntityRenderer extends EntityRenderer<FighterShipEntity> {
    public final FighterShipModel model;

    public FighterShipEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        this.model = new FighterShipModel(ctx.getPart(USMClient.FIGHTER_SHIP));
    }

    @Override
    public void render(FighterShipEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.scale(1.0F, -1.0F, -1.0F);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(entity.getYaw()));
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(entity.getPitch()));

        matrices.translate(0, -1, 0);

        this.model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(this.getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
        matrices.pop();
    }

    @Override
    public Identifier getTexture(FighterShipEntity entity) {
        return Identifier.of(USM.MOD_ID, "textures/ships/fighter_ship.png");
    }

    @Override
    public boolean shouldRender(FighterShipEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
