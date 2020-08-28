package com.hungteen.pvz.entity.plant.enforce;

import com.hungteen.pvz.entity.ai.PVZLookAroundGoal;
import com.hungteen.pvz.entity.ai.PVZNearestTargetGoal;
import com.hungteen.pvz.entity.misc.SmallChomperEntity;
import com.hungteen.pvz.entity.plant.PVZPlantEntity;
import com.hungteen.pvz.misc.damage.PVZDamageSource;
import com.hungteen.pvz.register.EntityRegister;
import com.hungteen.pvz.register.SoundRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.enums.Plants;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class ChomperEntity extends PVZPlantEntity{

	public final int ATTACK_CD = 20;
	private final int SUPER_RANGE = 20;
	private static final DataParameter<Integer> REST_TICK = EntityDataManager.createKey(ChomperEntity.class, DataSerializers.VARINT);
	
	public ChomperEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		dataManager.register(REST_TICK, 0);
	}
	
	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(2, new PVZLookAroundGoal(this));
		this.targetSelector.addGoal(0, new PVZNearestTargetGoal(this, true, 5, 2));
	}

	@Override
	protected void normalPlantTick() {
		super.normalPlantTick();
		if(!world.isRemote&&this.isPlantInSuperMode()) {//super
			if(this.getSuperTime()==1) {
				int cnt=this.getSuperAttackCnt();
				for(LivingEntity target:EntityUtil.getEntityAttackableTarget(this, EntityUtil.getEntityAABB(this, this.SUPER_RANGE, this.SUPER_RANGE))) {
					SmallChomperEntity chomper = EntityRegister.SMALL_CHOMPER.get().create(world);
					chomper.setPosition(target.getPosX(), target.getPosY(), target.getPosZ());
					chomper.setOwner(this);
					cnt--;
					world.addEntity(chomper);
					if(cnt==0) {
						break;
					}
				}
			}
		}
		if(!world.isRemote&&this.getAttackTarget()!=null) {
			this.lookController.setLookPositionWithEntity(this.getAttackTarget(), 30f, 30f);
		}
		if(!world.isRemote) {
			if(this.getAttackTime()<this.ATTACK_CD/2) {//attack stage
				if(this.getRestTick()>0) {//rest time cannot attack
					this.setRestTick(this.getRestTick()-1);
					return ;
				}
				if(this.getAttackTarget()==null) {//no target 
					return ;
				}
				if(this.getDistanceSq(this.getAttackTarget())>9) {//target too far away !
					this.setAttackTarget(null);
					this.setAttackTime(0);
					return ;
				}
				this.setAttackTime(this.getAttackTime()+1);
				if(this.getAttackTime()==this.ATTACK_CD/2) {//attack
					this.performAttack();
				}
			}else {
				this.setAttackTime(this.getAttackTime()+1);
				if(this.getAttackTime()>this.ATTACK_CD) {
					this.setAttackTime(0);
				}
			}
		}
	}
	
	@Override
	public boolean isInvulnerable() {
		return this.getAttackTime()>0;
	}
	
	private void performAttack() {
		LivingEntity target = this.getAttackTarget();
		if(target.getHealth()<=this.getMaxEatDamage()) {//eat to death need rest
			this.setRestTick(this.getRestCD());
		}
		this.playSound(SoundRegister.CHOMP.get(), 1, 1);
		target.attackEntityFrom(PVZDamageSource.causeEatDamage(this, this), this.getAttackDamage(target));
	}
	
	public float getAttackDamage(LivingEntity target) {
		if(target.getHealth()<=this.getMaxEatDamage()) {//eat to death
			return this.getMaxEatDamage();
		}else {
			return this.getNormalDamage();
		}
	}
	
	public int getMaxEatDamage() {
		int lvl=this.getPlantLvl();
		if(lvl<=20) {
			int now=(lvl-1)/4;
			return 200+now*20;
		}
		return 200;
	}
	
	public int getNormalDamage() {
		int lvl=this.getPlantLvl();
		if(lvl<=20) {
			int now=(lvl-1)/4;
			return now*5+40;
		}
		return 40;
	}
	
	public int getRestCD() {
		int lvl=this.getPlantLvl();
		if(lvl<=20) {
			int now = (lvl-1)/5;
			return 840-40*now;
		}
		return 840;
	}
	
	public int getSuperAttackCnt() {
		int lvl=this.getPlantLvl();
		if(lvl<=6) return 3;
		else if(lvl<=13) return 4;
		else if(lvl<=20) return 5;
		return 3;
	}
	
	@Override
	public boolean hasSuperMode() {
		return true;
	}

	@Override
	public int getSuperTimeLength() {
		return 20;
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putInt("rest_tick", this.getRestTick());
	}
	
	@Override
	public void readAdditional(CompoundNBT compound) {
		super.readAdditional(compound);
		this.setRestTick(compound.getInt("rest_tick"));
	}
	
	public int getRestTick() {
		return this.dataManager.get(REST_TICK);
	}
	
	public void setRestTick(int tick) {
		this.dataManager.set(REST_TICK, tick);
	}
	
	@Override
	public Plants getPlantEnumName() {
		return Plants.CHOMPER;
	}

}