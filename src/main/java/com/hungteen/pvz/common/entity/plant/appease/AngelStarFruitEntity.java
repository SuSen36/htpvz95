package com.hungteen.pvz.common.entity.plant.appease;

import com.hungteen.pvz.common.entity.bullet.AbstractBulletEntity;
import com.hungteen.pvz.common.entity.bullet.StarEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantShooterEntity;
import com.hungteen.pvz.register.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.Plants;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class AngelStarFruitEntity extends PlantShooterEntity {

	public static final float PER_ANGLE = 360F / 5;
	public int lightTick = 0;
	
	public AngelStarFruitEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public void normalPlantTick() {
		if(level.isClientSide) {
			if(this.lightTick > 0) {
				-- this.lightTick;
			}
			if(this.getAttackTime() > 0) {
			    this.lightTick = 8;
			}
		}
		super.normalPlantTick();
		if(this.isPlantInSuperMode()) {
			float now = this.getSuperTime() * 4;
			for(int i = 0; i < 5; ++ i) {
				this.shootStarByAngle(now);
				now += PER_ANGLE;
			}
		}
	}
	
	@Override
	public void shootBullet() {
		float now = this.yHeadRot;
		for(int i = 0; i < 5; ++ i) {
			this.shootStarByAngle(now);
			now += PER_ANGLE;
		}
		if(this.getRandom().nextInt(100) < this.getExtraAttackChance()) {
			now = this.yHeadRot + 36F;
			for(int i = 0; i < 5; ++ i) {
				this.shootStarByAngle(now);
				now += PER_ANGLE;
			}
		}
		EntityUtil.playSound(this, SoundRegister.SNOW_SHOOT.get());
	}
	
	public int getExtraAttackChance() {
		int lvl = this.getPlantLvl();
		if(lvl <= 19) return 17 + 3 * lvl;
		return 80;
	}
	
	@Override
	public float getAttackDamage() {
		int lvl = this.getPlantLvl();
		if(lvl <= 12) {
			int now = (lvl - 1) / 4;
			return 3 + 0.25F * now;
		} 
		if(lvl <= 20) {
			int now = (lvl - 13) / 2;
			return 3.75F + 0.25F * now;
		}
		return 4.5F;
	}
	
	@Override
	protected boolean canAttackNow() {
		return this.getAttackTime() == 2 && ! this.isPlantInSuperMode();
	}
	
	private void shootStarByAngle(float angle) {
		angle *= 3.14159F / 180F;
		double vx = - MathHelper.sin(angle);
		double vz = MathHelper.cos(angle);
		AbstractBulletEntity pea = this.createBullet();
		pea.setPos(getX(), getY() + 0.2F, getZ());
		double speed = 1.4D;
		pea.setDeltaMovement(vx * speed, 0, vz * speed);
		level.addFreshEntity(pea);
	}
	
	@Override
	protected AbstractBulletEntity createBullet() {
		final StarEntity.StarTypes type = this.isPlantInSuperMode() ? StarEntity.StarTypes.BIG : StarEntity.StarTypes.NORMAL;
		return new StarEntity(level, this, type, StarEntity.StarStates.PINK);
	}
	
	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return EntitySize.scalable(0.9F, 0.5F);
	}
	
	@Override
	public double getMaxShootAngle() {
		return 40;
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(2);
	}

	@Override
	public Plants getPlantEnumName() {
		return Plants.ANGEL_STAR_FRUIT;
	}

	@Override
	public int getSuperTimeLength() {
		if(this.isPlantInStage(1)) return 100;
		if(this.isPlantInStage(2)) return 200;
		return 300;
	}

}
