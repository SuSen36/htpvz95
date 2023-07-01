package com.hungteen.pvz.common.entity.plant.defence;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.entity.plant.enforce.SquashEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.OtherPlants;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.entity.*;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class TallNutEntity extends WallNutEntity {

	public TallNutEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public float getSuperLife() {
		return 800;
	}

	@Override
	public int getArmor() {
		return 15;
	}

	@Override
	public int getArmorToughness() {
		return 10;
	}

	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return new EntitySize(0.9f, 1.9f, false);
	}

	@Override
	public float getAttractRange() {
		return 3.5F;
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		DamageSource source = this.getLastDamageSource();
		if (!level.isClientSide && this.isDeadOrDying() && source instanceof PVZEntityDamageSource) {

			if (((PVZEntityDamageSource) source).isCrushDamage()) {
				return;
			}
			NutSquashEntity nutSquash = EntityRegister.NUT_SQUASH.get().create(level);
			nutSquash.setPos(this.getX(), this.getY(), this.getZ());
			this.level.addFreshEntity(nutSquash);

			this.remove();
		}
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TALL_NUT;
	}

	//亡语
	public static class NutSquashEntity extends SquashEntity {
		public NutSquashEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
			super(type, worldIn);
		}


		////窝瓜行为

		protected void dealDamage() {
			this.setAttackTime(0);
			this.canCollideWithPlant = true;
			this.isImmuneToWeak = false;
			EntityUtil.playSound(this, SoundRegister.GROUND_SHAKE.get());
			final float range = 1F;
			for (Entity entity : EntityUtil.getWholeTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range))) {
				entity.hurt(PVZEntityDamageSource.causeCrushDamage(this), this.getSkillValue(SkillTypes.NORMAL_ENHANCE_STRENGTH));
			}
		}
		public float getAgainChance(){
			return 0;
		}
		@Override
		public EntitySize getDimensions(Pose poseIn) {
			return new EntitySize(0.9f, 1.9f, false);
		}
		@Override
		public IPlantType getPlantType() {
			return OtherPlants.NUT_SQUASH;
		}
	}
}


