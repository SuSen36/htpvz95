package com.hungteen.pvz.entity.drop;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public abstract class DropEntity extends MobEntity{

	private static final DataParameter<Integer> AMOUNT = EntityDataManager.createKey(DropEntity.class, DataSerializers.VARINT);
	protected int liveTime;
	
	public DropEntity(EntityType<? extends MobEntity> type, World worldIn) {
		super(type, worldIn);
		this.liveTime=0;
		this.setInvulnerable(true);
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		this.dataManager.register(AMOUNT, 1);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(4, new SwimGoal(this));
	}
	
	@Override
	public void tick() {
		super.tick();
		if(! world.isRemote) {
			this.liveTime++;
		    if(this.liveTime >= this.getMaxLiveTick()) {
			    this.remove();
		    }
		}
	}
	
	@Override
	protected void collideWithEntity(Entity entityIn) {
	}
	
	@Override
	protected void collideWithNearbyEntities() {
	}
	
	@Override
	public boolean canBeCollidedWith() {
		return false;
	}
	/**
	 * drop live tick,read from config file
	 */
	protected abstract int getMaxLiveTick();
	
	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		if(AMOUNT.equals(key)) {
			this.recalculateSize();
		}
		super.notifyDataManagerChange(key);
	}
	
	@Override
	public boolean onLivingFall(float distance, float damageMultiplier) {
		return false;
	}
	
	@Override
	protected void playFallSound() {
		return ;
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		this.liveTime = compound.getInt("live_time");
		this.setAmount(compound.getInt("drop_amount"));
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		compound.putInt("live_time", this.liveTime);
		compound.putInt("drop_amount", this.getAmount());
	}
	
	public int getAmount(){
		return this.dataManager.get(AMOUNT);
	}
	
	public void setAmount(int num){
		this.dataManager.set(AMOUNT, num);
	}
	
}