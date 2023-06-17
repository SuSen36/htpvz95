package com.hungteen.pvz.client.model.entity.zombie.grass;

import com.hungteen.pvz.client.model.entity.zombie.PVZZombieModel;
import com.hungteen.pvz.common.entity.zombie.grass.FlagZombieEntity;

import net.minecraft.client.renderer.model.ModelRenderer;

import java.util.Optional;

// Made with Blockbench 3.6.5
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


public class FlagZombieModel extends PVZZombieModel<FlagZombieEntity> {

	private final ModelRenderer total;
	private final ModelRenderer left_leg;
	private final ModelRenderer right_leg;
	private final ModelRenderer up;
	private final ModelRenderer body;
	private final ModelRenderer left_hand;
	private final ModelRenderer right_hand;
	private final ModelRenderer head;
	private final ModelRenderer bucket;
	private final ModelRenderer defence1;
	private final ModelRenderer bar;
	private final ModelRenderer defence2;
	private final ModelRenderer defence3;
	private final ModelRenderer getZombieLeftHand;
	private final ModelRenderer getZombieRightHand;
	private final ModelRenderer getZombieLeftLeg;
	private final ModelRenderer getZombieRightLeg;
	private final ModelRenderer getZombieHead;
	private final ModelRenderer getZombieUpBody;
	private final ModelRenderer getZombieWholeBody;

	public FlagZombieModel() {
		texWidth = 256;
		texHeight = 256;

		total = new ModelRenderer(this);
		total.setPos(0.0F, 24.0F, 0.0F);


		left_leg = new ModelRenderer(this);
		left_leg.setPos(4.0F, -24.0F, 0.0F);
		total.addChild(left_leg);
		left_leg.texOffs(0, 0).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		right_leg = new ModelRenderer(this);
		right_leg.setPos(-5.0F, -24.0F, 0.0F);
		total.addChild(right_leg);
		right_leg.texOffs(44, 0).addBox(-3.0F, 0.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		up = new ModelRenderer(this);
		up.setPos(0.0F, -24.0F, 0.0F);
		total.addChild(up);


		body = new ModelRenderer(this);
		body.setPos(0.0F, 0.0F, 0.0F);
		up.addChild(body);
		body.texOffs(0, 41).addBox(-8.0F, -24.0F, -4.0F, 16.0F, 24.0F, 8.0F, 0.0F, false);

		left_hand = new ModelRenderer(this);
		left_hand.setPos(12.0F, -20.0F, 0.0F);
		up.addChild(left_hand);
		left_hand.texOffs(96, 60).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);

		right_hand = new ModelRenderer(this);
		right_hand.setPos(-12.0F, -20.0F, 0.0F);
		up.addChild(right_hand);
		setRotationAngle(right_hand, -1.5708F, 0.0F, 0.0F);
		right_hand.texOffs(96, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 24.0F, 8.0F, 0.0F, false);
		right_hand.texOffs(3, 207).addBox(-1.0F, 20.0F, -18.0F, 2.0F, 2.0F, 26.0F, 0.0F, false);
		right_hand.texOffs(83, 157).addBox(-1.0F, -6.0F, -36.0F, 1.0F, 28.0F, 18.0F, 0.0F, false);

		head = new ModelRenderer(this);
		head.setPos(0.0F, -25.0F, 0.0F);
		up.addChild(head);
		head.texOffs(16, 96).addBox(-8.0F, -15.0F, -8.0F, 16.0F, 16.0F, 16.0F, 0.0F, false);

		bucket = new ModelRenderer(this);
		bucket.setPos(0.0F, -13.0F, 0.0F);
		head.addChild(bucket);
		setRotationAngle(bucket, -0.0873F, 0.0F, 0.0F);


		defence1 = new ModelRenderer(this);
		defence1.setPos(0.0F, 0.0F, 0.0F);
		bucket.addChild(defence1);
		defence1.texOffs(137, 2).addBox(-9.0F, -2.0F, -9.0F, 18.0F, 2.0F, 1.0F, 0.0F, false);
		defence1.texOffs(138, 8).addBox(-9.0F, -2.0F, 8.0F, 18.0F, 2.0F, 1.0F, 0.0F, false);
		defence1.texOffs(138, 15).addBox(-9.0F, -2.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, true);
		defence1.texOffs(138, 38).addBox(8.0F, -2.0F, -8.0F, 1.0F, 2.0F, 16.0F, 0.0F, false);
		defence1.texOffs(184, 2).addBox(-9.0F, -7.0F, -9.0F, 18.0F, 5.0F, 18.0F, 0.0F, false);

		bar = new ModelRenderer(this);
		bar.setPos(0.0F, -2.0F, -1.0F);
		defence1.addChild(bar);
		setRotationAngle(bar, 0.7854F, 0.0F, 0.0F);
		bar.texOffs(182, 31).addBox(-11.0F, -1.0F, -15.0F, 22.0F, 1.0F, 1.0F, 0.0F, false);
		bar.texOffs(138, 60).addBox(-11.0F, -1.0F, -14.0F, 1.0F, 1.0F, 16.0F, 0.0F, false);
		bar.texOffs(137, 82).addBox(10.0F, -1.0F, -14.0F, 1.0F, 1.0F, 16.0F, 0.0F, false);
		bar.texOffs(181, 38).addBox(9.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);
		bar.texOffs(195, 38).addBox(-10.0F, -2.0F, 0.0F, 1.0F, 3.0F, 3.0F, 0.0F, false);

		defence2 = new ModelRenderer(this);
		defence2.setPos(0.0F, 0.0F, 0.0F);
		bucket.addChild(defence2);
		defence2.texOffs(181, 48).addBox(-9.0F, -14.0F, -9.0F, 18.0F, 7.0F, 18.0F, 0.0F, false);

		defence3 = new ModelRenderer(this);
		defence3.setPos(0.0F, 0.0F, 0.0F);
		bucket.addChild(defence3);
		defence3.texOffs(182, 76).addBox(-9.0F, -20.0F, -9.0F, 18.0F, 6.0F, 18.0F, 0.0F, false);

		getZombieLeftHand = new ModelRenderer(this);
		getZombieLeftHand.setPos(0.0F, 0.0F, 0.0F);


		getZombieRightHand = new ModelRenderer(this);
		getZombieRightHand.setPos(0.0F, 0.0F, 0.0F);


		getZombieLeftLeg = new ModelRenderer(this);
		getZombieLeftLeg.setPos(0.0F, 0.0F, 0.0F);


		getZombieRightLeg = new ModelRenderer(this);
		getZombieRightLeg.setPos(0.0F, 0.0F, 0.0F);


		getZombieHead = new ModelRenderer(this);
		getZombieHead.setPos(0.0F, 0.0F, 0.0F);


		getZombieUpBody = new ModelRenderer(this);
		getZombieUpBody.setPos(0.0F, 0.0F, 0.0F);


		getZombieWholeBody = new ModelRenderer(this);
		getZombieWholeBody.setPos(0.0F, 0.0F, 0.0F);

		this.rightHandOriginAngel = - 90;
	}
	
	@Override
	public void refreshAnim() {
		this.right_hand.xRot = -1.5708F;
	}
	
	@Override
	public void updateFreeParts(FlagZombieEntity entity) {
		super.updateFreeParts(entity);
		this.isRightHandFree = false;
		this.defence3.visible = entity.hasBucketHead(3);
		this.defence2.visible = entity.hasBucketHead(2);
		this.defence1.visible = entity.hasBucketHead(1);
	}
	
	@Override
	public ModelRenderer getZombieLeftHand() {
		return this.left_hand;
	}

	@Override
	public ModelRenderer getZombieRightHand() {
		return this.right_hand;
	}

	@Override
	public ModelRenderer getZombieLeftLeg() {
		return this.left_leg;
	}

	@Override
	public ModelRenderer getZombieRightLeg() {
		return this.right_leg;
	}

	@Override
	public ModelRenderer getZombieHead() {
		return this.head;
	}
	
	@Override
	public ModelRenderer getZombieUpBody() {
		return this.up;
	}

	@Override
	public ModelRenderer getZombieWholeBody() {
		return this.total;
	}

	@Override
	public Optional<ModelRenderer> getHelmet() {
		return Optional.ofNullable(this.bucket);
	}


}