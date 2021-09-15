package com.hungteen.pvz.common.entity.plant.arma;

import java.util.List;
import java.util.Optional;

import com.hungteen.pvz.common.core.PlantType;
import com.hungteen.pvz.common.entity.bullet.ButterEntity;
import com.hungteen.pvz.common.entity.bullet.KernelEntity;
import com.hungteen.pvz.common.entity.bullet.PultBulletEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantPultEntity;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.item.spawn.card.ImitaterCardItem;
import com.hungteen.pvz.common.item.spawn.card.PlantCardItem;
import com.hungteen.pvz.register.EffectRegister;
import com.hungteen.pvz.utils.EntityUtil;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class KernelPultEntity extends PlantPultEntity {

	private static final DataParameter<Integer> CURRENT_BULLET = EntityDataManager.defineId(KernelPultEntity.class, DataSerializers.INT);
	private static final int BUTTER_CHANCE = 10;
	
	public KernelPultEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(CURRENT_BULLET, CornTypes.KERNEL.ordinal());
	}
	
	@Override
	public ActionResultType interactAt(PlayerEntity player, Vector3d vec3d, Hand hand) {
		if(! level.isClientSide && hand == Hand.MAIN_HAND && ! EntityUtil.canTargetEntity(this, player)) {
//			ItemStack stack = player.getItemInHand(hand);
//			if(stack.getItem() instanceof PlantCardItem) {
//				PlantCardItem item = (PlantCardItem) stack.getItem();
//				if(item.plantType == PVZPlants.COB_CANNON) {
//					Optional<KernelPultEntity> pult = this.getNearByPult(player);
//					if(pult.isPresent()) {
//						PlantCardItem.checkSunAndSummonPlant(player, stack, item, blockPosition(), (plantEntity) -> {
//							this.onPlantUpgrade(plantEntity);
//							plantEntity.plantSunCost += pult.get().plantSunCost;
//							pult.get().remove();
//						});
//					}
//				} else if(item instanceof ImitaterCardItem && ((ImitaterCardItem) item).isPlantTypeEqual(stack, PVZPlants.COB_CANNON)) {
//					Optional<KernelPultEntity> pult = this.getNearByPult(player);
//					if(pult.isPresent()) {
//					    ImitaterCardItem.checkSunAndSummonImitater(player, stack, item, blockPosition(), (imitater) -> {
//						    imitater.targetPlantEntity = Optional.of(this);
//						    imitater.plantSunCost += pult.get().plantSunCost;
//						    pult.get().remove();
//					    });
//					}
//				}
//			}
		}
		return super.interactAt(player, vec3d, hand);
	}
	
	private Optional<KernelPultEntity> getNearByPult(PlayerEntity player){
		final float range = 1.5F;
		List<KernelPultEntity> list = level.getEntitiesOfClass(KernelPultEntity.class, EntityUtil.getEntityAABB(this, range, range), pult -> {
			return ! pult.is(this) && pult.getPlantType() == PVZPlants.KERNEL_PULT && ! EntityUtil.canTargetEntity(pult, player);
		});
		if(list.isEmpty()) return Optional.empty();
		return Optional.ofNullable(list.get(0));
	}
	
	@Override
	public void startPultAttack() {
		super.startPultAttack();
		this.changeBullet();
	}
	
	/**
	 * switch butter or kernel.
	 */
	protected void changeBullet() {
		if(this.isPlantInSuperMode() && ! this.isSuperOut) {
			this.setCurrentBullet(CornTypes.BUTTER);
			return ;
		}
		this.setCurrentBullet(this.getRandom().nextInt(BUTTER_CHANCE) == 0 ? CornTypes.BUTTER : CornTypes.KERNEL);
	}
	
	@Override
	public void performPult(LivingEntity target1) {
		super.performPult(target1);
		this.setCurrentBullet(CornTypes.KERNEL); 
	}
	
	@Override
	protected PultBulletEntity createBullet() {
		if(this.isPlantInSuperMode() || this.getCurrentBullet() == CornTypes.BUTTER) {
			return new ButterEntity(level, this);
		}
		return new KernelEntity(level, this);
	}
	
	@Override
	public float getSuperDamage() {
		return 2 * this.getAttackDamage();
	};
	
	public EffectInstance getButterEffect() {
		return new EffectInstance(EffectRegister.BUTTER_EFFECT.get(), this.getButterDuration(), 1, false, false);
	}
	
	public int getButterDuration() {
		return 100;
	}
	
	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return EntitySize.scalable(0.8F, 1F);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("current_bullet_type")) {
			this.setCurrentBullet(CornTypes.values()[compound.getInt("current_bullet_type")]);
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("current_bullet_type", this.getCurrentBullet().ordinal());
	}
	
	public void setCurrentBullet(CornTypes type) {
		this.entityData.set(CURRENT_BULLET, type.ordinal());
	}
	
	public CornTypes getCurrentBullet() {
		return CornTypes.values()[this.entityData.get(CURRENT_BULLET)];
	}
	
	@Override
	public PlantType getPlantType() {
		return PVZPlants.KERNEL_PULT;
	}

	public static enum CornTypes{
		KERNEL,
		BUTTER,
		ROCKET
	}
	
}
