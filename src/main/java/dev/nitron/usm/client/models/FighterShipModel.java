package dev.nitron.usm.client.models;

import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class FighterShipModel extends EntityModel<Entity> {
	private final ModelPart fs;
	private final ModelPart langing_gear;
	private final ModelPart langing_gear2;
	private final ModelPart langing_gear3;
	public FighterShipModel(ModelPart root) {
		this.fs = root.getChild("fs");
		this.langing_gear = this.fs.getChild("langing_gear");
		this.langing_gear2 = this.fs.getChild("langing_gear2");
		this.langing_gear3 = this.fs.getChild("langing_gear3");
	}
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData fs = modelPartData.addChild("fs", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -9.0F, -38.0F, 18.0F, 9.0F, 69.0F, new Dilation(0.0F))
		.uv(62, 256).cuboid(-7.0F, -7.0F, -54.0F, 14.0F, 7.0F, 16.0F, new Dilation(0.0F))
		.uv(148, 78).cuboid(11.0F, -10.0F, -27.0F, 8.0F, 11.0F, 56.0F, new Dilation(0.0F))
		.uv(0, 78).cuboid(9.0F, -5.0F, -27.0F, 18.0F, 0.0F, 56.0F, new Dilation(0.0F))
		.uv(180, 212).cuboid(27.0F, -14.0F, -3.0F, 0.0F, 19.0F, 24.0F, new Dilation(0.0F))
		.uv(0, 234).cuboid(10.0F, -17.0F, 9.0F, 10.0F, 7.0F, 21.0F, new Dilation(0.0F))
		.uv(148, 145).cuboid(-19.0F, -10.0F, -27.0F, 8.0F, 11.0F, 56.0F, new Dilation(0.0F))
		.uv(180, 255).cuboid(-20.0F, -17.0F, 9.0F, 10.0F, 7.0F, 21.0F, new Dilation(0.0F))
		.uv(228, 212).cuboid(-27.0F, -14.0F, -3.0F, 0.0F, 19.0F, 24.0F, new Dilation(0.0F))
		.uv(0, 134).cuboid(-27.0F, -5.0F, -27.0F, 18.0F, 0.0F, 56.0F, new Dilation(0.0F))
		.uv(174, 41).cuboid(-8.0F, -18.0F, 9.0F, 16.0F, 9.0F, 28.0F, new Dilation(0.0F))
		.uv(174, 0).cuboid(-7.0F, -22.0F, 6.0F, 14.0F, 5.0F, 36.0F, new Dilation(0.0F))
		.uv(242, 255).cuboid(-6.0F, -16.0F, -9.0F, 12.0F, 7.0F, 18.0F, new Dilation(0.0F))
		.uv(262, 68).cuboid(-8.0F, -9.0F, 31.0F, 16.0F, 6.0F, 3.0F, new Dilation(0.0F))
		.uv(90, 190).cuboid(-5.0F, -15.0F, 34.0F, 10.0F, 10.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

		ModelPartData cube_r1 = fs.addChild("cube_r1", ModelPartBuilder.create().uv(274, 0).cuboid(0.0F, 0.0F, -12.0F, 0.0F, 3.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(-27.0F, 5.0F, 9.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData cube_r2 = fs.addChild("cube_r2", ModelPartBuilder.create().uv(0, 262).cuboid(0.0F, -9.0F, -12.0F, 0.0F, 9.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(-27.0F, -14.0F, 9.0F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r3 = fs.addChild("cube_r3", ModelPartBuilder.create().uv(90, 212).cuboid(-4.0F, -3.5F, -18.5F, 8.0F, 7.0F, 37.0F, new Dilation(0.0F)), ModelTransform.of(-19.0F, -4.5F, 6.5F, 0.0F, -0.1745F, 0.0F));

		ModelPartData cube_r4 = fs.addChild("cube_r4", ModelPartBuilder.create().uv(0, 190).cuboid(-4.0F, -3.5F, -18.5F, 8.0F, 7.0F, 37.0F, new Dilation(0.0F)), ModelTransform.of(19.0F, -4.5F, 6.5F, 0.0F, 0.1745F, 0.0F));

		ModelPartData cube_r5 = fs.addChild("cube_r5", ModelPartBuilder.create().uv(262, 41).cuboid(0.0F, 0.0F, -12.0F, 0.0F, 3.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(27.0F, 5.0F, 9.0F, 0.0F, 0.0F, 0.5236F));

		ModelPartData cube_r6 = fs.addChild("cube_r6", ModelPartBuilder.create().uv(122, 256).cuboid(0.0F, -9.0F, -12.0F, 0.0F, 9.0F, 24.0F, new Dilation(0.0F)), ModelTransform.of(27.0F, -14.0F, 9.0F, 0.0F, 0.0F, -0.5236F));

		ModelPartData langing_gear = fs.addChild("langing_gear", ModelPartBuilder.create().uv(62, 234).cuboid(0.0F, 0.0F, -0.25F, 0.0F, 9.0F, 11.0F, new Dilation(0.0F))
		.uv(274, 27).cuboid(-2.0F, 8.0F, -1.25F, 4.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, -37.75F));

		ModelPartData langing_gear2 = fs.addChild("langing_gear2", ModelPartBuilder.create().uv(276, 103).cuboid(0.0F, 0.0F, -0.25F, 0.0F, 9.0F, 11.0F, new Dilation(0.0F))
		.uv(276, 77).cuboid(-2.0F, 8.0F, -1.25F, 4.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(15.0F, 0.0F, 13.25F));

		ModelPartData langing_gear3 = fs.addChild("langing_gear3", ModelPartBuilder.create().uv(276, 123).cuboid(0.0F, 0.0F, -0.25F, 0.0F, 9.0F, 11.0F, new Dilation(0.0F))
		.uv(276, 90).cuboid(-2.0F, 8.0F, -1.25F, 4.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(-15.0F, 0.0F, 13.25F));
		return TexturedModelData.of(modelData, 512, 512);
	}
	@Override
	public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
		fs.render(matrices, vertices, light, overlay, color);
	}
}