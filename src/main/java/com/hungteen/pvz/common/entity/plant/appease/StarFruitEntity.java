package com.hungteen.pvz.common.entity.plant.appease;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.bullet.AbstractBulletEntity;
import com.hungteen.pvz.common.entity.bullet.StarEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantShooterEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class StarFruitEntity extends PlantShooterEntity {
	public static final float PER_ANGLE = 360F / 5;
	private static final float SHOOT_HEIGHT = 0.2F;

	public int lightTick = 0;

	public StarFruitEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
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
	}

	@Override
	protected boolean canAttackNow() {
		return this.getAttackTime() == 2;
	}

	@Override
	public int getSuperTimeLength() {
		return 100;
	}
   //子弹5分叉
	@Override
	public void shootBullet() {
		float now = this.yRot + 180F;
		for(int i = 0; i < 5; ++ i) {
			this.shootByAngle(now, SHOOT_HEIGHT);
			now += PER_ANGLE;
		}
		EntityUtil.playSound(this, SoundRegister.SNOW_SHOOT.get());
	}

	@Override
	protected AbstractBulletEntity createBullet() {
		final StarEntity.StarTypes type = this.isPlantInSuperMode() ? StarEntity.StarTypes.HUGE : StarEntity.StarTypes.NORMAL;
		Vector3d direction = this.getTarget()==null ? this.getLookAngle() : EntityUtil.getNormalisedVector2d(this, this.getTarget());
		return new StarEntity(level, this, type,direction,true ,StarEntity.StarStates.YELLOW);
	}

	@Override
	public float getAttackDamage() {
		return this.getSkillValue(SkillTypes.MORE_STAR_DAMAGE);
	}

	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return EntitySize.scalable(0.9F, 0.5F);
	}

	@Override
	public double getMaxShootAngle() {
		return 80;
	}

	@Override
	public void startShootAttack() {
		this.setAttackTime(2);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.STAR_FRUIT;
	}

}