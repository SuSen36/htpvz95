package com.hungteen.pvz.common.entity.plant.appease;

import com.hungteen.pvz.utils.MathUtil;
import com.hungteen.pvz.utils.PlantUtil;
import com.hungteen.pvz.utils.enums.Plants;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class SplitPeaEntity extends PeaShooterEntity{

	private static final DataParameter<Integer> ROUND_TICK = EntityDataManager.defineId(SplitPeaEntity.class, DataSerializers.INT);  
	public static final int MAX_ROUND_TIME = 20;
	
	public SplitPeaEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ROUND_TICK, 0);
	}
	
	@Override
	protected void plantTick() {
		super.plantTick();
		if(! level.isClientSide) {
			if(this.getRoundTick() != 0 && this.getRoundTick() != MAX_ROUND_TIME / 2) {
				this.rotateFacing();
			}
		}
	}
	
	@Override
	public void shootBullet() {
		if(this.isPlantInSuperMode()) {
			final int cnt = this.getSuperShootCount();
			for(int i = 0; i < cnt; ++ i) {
				final float offset = MathUtil.getRandomFloat(getRandom()) / 3;
				final float offsetH = MathUtil.getRandomFloat(getRandom()) / 3;
				this.performShoot(SHOOT_OFFSET, offset, offsetH, this.getExistTick() % 10 == 0, FORWARD_SHOOT_ANGLE);
				this.performShoot(SHOOT_OFFSET, offset, offsetH, false, BACK_SHOOT_ANGLE);
			}
		} else {
			final int frontNum = this.isFacingFront() ? 1 : 2;
			final int backNum = this.isFacingFront() ? 2 : 1;
			if(this.getAttackTime() <= frontNum) {
				this.performShoot(SHOOT_OFFSET, 0, 0, this.getAttackTime() == 1, FORWARD_SHOOT_ANGLE);
			}
			if(this.getAttackTime() <= backNum) {
			    this.performShoot(SHOOT_OFFSET, 0, 0, false, BACK_SHOOT_ANGLE);
			}
			this.checkAndChangeFacing();//change head.
		}
	}
	
	/**
	 * deal with change facing
	 */
	private void checkAndChangeFacing() {
		if(this.getAttackTime() == 1 && ! this.isPlantInSuperMode()) {
			final int chance = this.isFacingFront() ? this.getDoubleChance() : 100 - this.getDoubleChance();
			if(this.getRandom().nextInt(100) < chance) {
				this.rotateFacing();
			}
		}
	}
	
	private void rotateFacing() {
		this.setRoundTick((this.getRoundTick() + 1) % MAX_ROUND_TIME);
	}
	
	/**
	 * Repeater head stay front chance. 
	 */
	public int getDoubleChance() {
		return PlantUtil.getPlantAverageProgress(this, 20, 80);
	}
	
	@Override
	public void startShootAttack() {
		this.setAttackTime(2);
	}
	
	public boolean isFacingFront() {
		return this.getRoundTick() == 0;
	}
	
	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if(compound.contains("round_tick")) {
			this.setRoundTick(compound.getInt("round_tick"));
		}
	}
	
	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		compound.putInt("round_tick", this.getRoundTick());
	}
	
	public int getRoundTick() {
		return this.entityData.get(ROUND_TICK);
	}
	
	public void setRoundTick(int tick) {
		this.entityData.set(ROUND_TICK, tick);
	}
	
	@Override
	public Plants getPlantEnumName() {
		return Plants.SPLIT_PEA;
	}
	
}
