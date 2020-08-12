package com.hungteen.pvz.model.entity.zombie.grassday;

import com.hungteen.pvz.entity.zombie.grassday.BucketHeadZombieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;

// Made with Blockbench 3.6.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class BucketHeadZombieModel extends EntityModel<BucketHeadZombieEntity> {
	private final ModelRenderer total;
	private final ModelRenderer head;
	private final ModelRenderer bar;
	private final ModelRenderer right_hand;
	private final ModelRenderer left_hand;
	private final ModelRenderer body;
	private final ModelRenderer right_leg;
	private final ModelRenderer left_leg;

	public BucketHeadZombieModel() {
		textureWidth = 256;
		textureHeight = 256;

		total = new ModelRenderer(this);
		total.setRotationPoint(0.0F, 24.0F, 0.0F);
		

		head = new ModelRenderer(this);
		head.setRotationPoint(0.0F, -49.0F, 0.0F);
		total.addChild(head);
		head.setTextureOffset(16, 96).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);
		head.setTextureOffset(208, 244).addBox(-9.0F, -15.0F, -9.0F, 18.0F, 2.0F, 1.0F, 0.0F, false);
		head.setTextureOffset(208, 224).addBox(-9.0F, -15.0F, 8.0F, 18.0F, 2.0F, 1.0F, 0.0F, false);
		head.setTextureOffset(217, 185).addBox(-9.0F, -15.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, true);
		head.setTextureOffset(214, 156).addBox(8.0F, -15.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, false);
		head.setTextureOffset(174, 105).addBox(-9.0F, -31.0F, -9.0F, 18.0F, 16.0F, 18.0F, 0.0F, false);

		bar = new ModelRenderer(this);
		bar.setRotationPoint(0.0F, -14.0F, 0.0F);
		head.addChild(bar);
		setRotationAngle(bar, 0.5236F, 0.0F, 0.0F);
		bar.setTextureOffset(10, 174).addBox(-10.0F, -1.0F, -15.0F, 20.0F, 1.0F, 1.0F, 0.0F, false);
		bar.setTextureOffset(28, 203).addBox(-10.0F, -1.0F, -14.0F, 1.0F, 1.0F, 14.0F, 0.0F, false);
		bar.setTextureOffset(116, 131).addBox(9.0F, -1.0F, -14.0F, 1.0F, 1.0F, 14.0F, 0.0F, false);

		right_hand = new ModelRenderer(this);
		right_hand.setRotationPoint(8.0F, -48.0F, 0.0F);
		total.addChild(right_hand);
		right_hand.setTextureOffset(96, 0).addBox(-24.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		left_hand = new ModelRenderer(this);
		left_hand.setRotationPoint(-8.0F, -48.0F, 0.0F);
		total.addChild(left_hand);
		left_hand.setTextureOffset(96, 60).addBox(16.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		body = new ModelRenderer(this);
		body.setRotationPoint(0.0F, -25.0F, 0.0F);
		total.addChild(body);
		body.setTextureOffset(0, 41).addBox(-8.0F, -23.0F, -4.0F, 16.0F, 24.0F, 8.0F, 0.0F, false);

		right_leg = new ModelRenderer(this);
		right_leg.setRotationPoint(4.0F, -24.0F, 0.0F);
		total.addChild(right_leg);
		right_leg.setTextureOffset(44, 0).addBox(-12.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		left_leg = new ModelRenderer(this);
		left_leg.setRotationPoint(-4.0F, -24.0F, 0.0F);
		total.addChild(left_leg);
		left_leg.setTextureOffset(0, 0).addBox(4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(BucketHeadZombieEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		this.head.rotateAngleY = netHeadYaw / (180F / (float)Math.PI);
        this.head.rotateAngleX = headPitch / (180F / (float)Math.PI);
        this.right_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.left_leg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        this.right_hand.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.left_hand.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		total.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}