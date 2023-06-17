package com.hungteen.pvz.common.entity.plant.defence;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.ai.goal.target.PVZRandomTargetGoal;
import com.hungteen.pvz.common.entity.plant.enforce.SquashEntity;
import com.hungteen.pvz.common.impl.SkillTypes;
import com.hungteen.pvz.common.impl.plant.PVZPlants;

import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.common.misc.sound.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.entity.*;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class TallNutEntity extends WallNutEntity{

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
		if(isDeadOrDying()){
		return 0;
	}
		return 3.5F;
	}

   //窝瓜行为

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new PVZRandomTargetGoal(this, true, false, 3, 3));
	}
   @Override
   protected void normalPlantTick() {
	   super.normalPlantTick();
	   if(!level.isClientSide) {
		   if(EntityUtil.isEntityValid(this.getTarget()) && isDeadOrDying()) {
			   this.setInvulnerable(true);
			   this.getLookControl().setLookAt(this.getTarget(), 30f, 30f);
			   this.setHealth(1);

			   if (this.getAttackTime() > 0) {
				   if (this.isOnGround() || this.isInWaterOrBubble()) {
					   this.dealDamage();
					   //check death.
					   this.remove();
				   }
			   } else {
				   if (this.getTarget() != null) {
					   EntityUtil.playSound(this, SoundRegister.SQUASH_HMM.get());
					   this.jumpToTarget(this.getTarget());
				   }
			   }
		   }
	      }
	   }

	protected void dealDamage(){
		this.setAttackTime(0);
		this.canCollideWithPlant = true;
		this.isImmuneToWeak = false;
		EntityUtil.playSound(this, SoundRegister.GROUND_SHAKE.get());
		final float range = 1F;
		for(Entity entity : EntityUtil.getWholeTargetableEntities(this, EntityUtil.getEntityAABB(this, range, range))) {
			entity.hurt(PVZEntityDamageSource.causeCrushDamage(this), this.getSkillValue(SkillTypes.NORMAL_ENHANCE_STRENGTH));
		}
	}

	/**
	 * jump to the top of the target.
	 * {@link #normalPlantTick()}
	 */
	private void jumpToTarget(LivingEntity target) {
		final int tick = 10;
		this.canCollideWithPlant = false;
		this.isImmuneToWeak = true;
		final Vector3d pos = target.position().add(target.getDeltaMovement().scale(tick * 0.8D));
		this.setPos(pos.x(), pos.y() + target.getBbHeight() + 1, pos.z());
		this.setDeltaMovement(this.getDeltaMovement().x(), 0, this.getDeltaMovement().z());
		this.setAttackTime(1);
	}
	@Override
	public boolean hurt(DamageSource source, float amount) {

		if (source instanceof PVZEntityDamageSource &&this.isDeadOrDying()) {
			this.invulnerableTime = 200;
		}else if (source instanceof PVZEntityDamageSource) {
			this.invulnerableTime = 0;//can get hurt each attack by pvz damage.
		}
		if(source.getEntity() instanceof LivingEntity && EntityUtil.checkCanEntityBeAttack(this, source.getEntity())){
			//determine whether to change target.
			if(EntityUtil.isEntityValid(this.getTarget()) && this.getRandom().nextFloat() < 0.4F){
				if(this.distanceTo(source.getEntity()) < this.distanceTo(this.getTarget())){
					this.setTarget((LivingEntity) source.getEntity());
				}
			}
		}
		return super.hurt(source, amount);
	}

	@Override
	protected boolean shouldLockXZ() {
		return this.isOnGround();
	}
	@Override
	public boolean canBeTargetBy(LivingEntity living) {
		return !isDeadOrDying()&&!hasMetal();
	}
	
	@Override
	public IPlantType getPlantType() {
		return PVZPlants.TALL_NUT;
	}
	
}
