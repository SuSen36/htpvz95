package com.hungteen.pvz.entity.plant.enforce;

import com.hungteen.pvz.entity.ai.PVZNearestTargetGoal;
import com.hungteen.pvz.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.misc.damage.PVZDamageSource;
import com.hungteen.pvz.misc.damage.PVZDamageType;
import com.hungteen.pvz.register.EntityRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.PlantUtil;
import com.hungteen.pvz.utils.enums.Plants;

import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TangleKelpEntity extends PVZPlantEntity{

	public TangleKelpEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, true, 1f, 2f));
	}
	
	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(!world.isRemote) {
			if(this.getAttackTime()>0) {
				this.setAttackTime(this.getAttackTime()+1);
				if(this.getPassengers().isEmpty()) {
					this.remove();
					return ;
				}
				this.setMotion(0, -0.03f, 0);
				if(this.getAttackTime()%100==0) {
					for(Entity target:this.getPassengers()) {
						target.attackEntityFrom(PVZDamageSource.causeNormalDamage(this, this), this.getAttackDamage());
					}
				}
				if(this.getAttackTime()>=1000) {
					this.remove();
				}
			}
			if(this.getAttackTime()==0){
				if(this.getAttackTarget()!=null) {
					this.setAttackTime(1);
					if(this.getAttackTarget().getRidingEntity()!=null) {
						this.getAttackTarget().stopRiding();
					}
					this.getAttackTarget().startRiding(this, true);
				}
			}
		}
	}
	
	@Override
	public void startSuperMode() {
		super.startSuperMode();
		if(!world.isRemote) {
			int cnt = this.getCount();
			for(LivingEntity target:EntityUtil.getEntityAttackableTarget(this, EntityUtil.getEntityAABB(this, 25, 3))) {
				if(target.isInWater()) {
					--cnt;
					TangleKelpEntity entity = EntityRegister.TANGLE_KELP.get().create(world);
					entity.setPosition(target.getPosX(), target.getPosY(), target.getPosZ());
					PlantUtil.copyPlantData(entity, this);
					world.addEntity(entity);
				}
				if(cnt<=0) {
					break;
				}
			}
		}
	}
	
	@Override
	public float getAttackDamage() {
		int lvl=this.getPlantLvl();
		if(lvl<=20) {
			int now = (lvl-1)/5;
			return now*2+13;
		}
		return 13;
	}
	
	@Override
	public double getMountedYOffset() {
		return 0;
	}
	
	private int getCount(){
		int lvl=this.getPlantLvl();
		if(lvl<=6) return 3;
		else if(lvl<=13) return 4;
		else if(lvl<=20) return 5;
		return 3;
	}
	
	@Override
	public EntitySize getSize(Pose poseIn) {
		return new EntitySize(0.6f, 1f, false);
	}
	
	@Override
	protected boolean checkWeak() {//check if it leave water
		if(this.isImmuneToWeak) return false;
    	BlockState state =this.world.getBlockState(new BlockPos(this).down());
        if(state.isSolid()&&world.isAirBlock(new BlockPos(this))) {
        	return true;
        }
        return false;
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		if(source instanceof PVZDamageSource) {
			if(((PVZDamageSource) source).getPVZDamageType()==PVZDamageType.EAT) return true;
		}
		return super.isInvulnerableTo(source);
	}
	
	@Override
	public boolean canBeRiddenInWater() {
		return true;
	}
	
	@Override
	public boolean canBeRiddenInWater(Entity rider) {
		return true;
	}
	
	@Override
	public boolean hasNoGravity() {
		return this.isInWater();
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public Plants getPlantEnumName() {
		return Plants.TANGLE_KELP;
	}

	@Override
	public boolean hasSuperMode() {
		return true;
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}

}
