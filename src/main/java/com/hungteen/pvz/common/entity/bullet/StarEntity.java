package com.hungteen.pvz.common.entity.bullet;

import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.common.misc.PVZEntityDamageSource;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.entity.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

public class StarEntity extends AbstractBulletEntity {

	private static final DataParameter<Vector3d> START_POS = EntityDataManager.defineId(StarEntity.class,
			EntityUtil.VECTOR3D_SERIALIZER);
	private static final DataParameter<Vector3d> TARGET_DIR = EntityDataManager.defineId(StarEntity.class,
			EntityUtil.VECTOR3D_SERIALIZER);
	private static final DataParameter<Boolean> DIR_CHANGED = EntityDataManager.defineId(StarEntity.class,
			DataSerializers.BOOLEAN);
	private static final DataParameter<Float> CHANGE_DISTANCE = EntityDataManager.defineId(StarEntity.class,
			DataSerializers.FLOAT);
	private static final DataParameter<Integer> STAR_TYPE = EntityDataManager.defineId(StarEntity.class,
			DataSerializers.INT);
	private static final DataParameter<Integer> STAR_STATE = EntityDataManager.defineId(StarEntity.class,
			DataSerializers.INT);

	public StarEntity(EntityType<?> type, World worldIn) {
		super(type, worldIn);
	}

	public StarEntity(World worldIn, LivingEntity livingEntityIn, StarTypes starType,Vector3d direction, boolean directionChanged ,StarStates starState) {
		super(EntityRegister.STAR.get(), worldIn, livingEntityIn);
		this.setStarType(starType);
		this.setStarState(starState);
		if(directionChanged && direction != Vector3d.ZERO)this.setTargetDirection(direction);
		this.setDirectionChanged(!directionChanged);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(STAR_TYPE, 0);
		this.entityData.define(STAR_STATE, 0);
		this.entityData.define(START_POS, Vector3d.ZERO);
		this.entityData.define(TARGET_DIR, Vector3d.ZERO);
		this.entityData.define(DIR_CHANGED, false);
		this.entityData.define(CHANGE_DISTANCE, 1.0f);
	}

	@Override
	public void tick() {
		super.tick();

		if (!level.isClientSide && !this.isDirectionChanged()) {
			if (getStartPos().equals(Vector3d.ZERO)) {
				setStartPos(position());
				return;
			}

			double distanceSqr = getStartPos().distanceToSqr(position());
			double triggerDist = getChangeDistance() * getChangeDistance();
			if (distanceSqr >= triggerDist) {
				setDirectionChanged(true);

				Vector3d targetDir = getTargetDirection();
				if (!targetDir.equals(Vector3d.ZERO)) {
					Vector3d motion = getDeltaMovement();
					double horizontalSpeed = Math.hypot(motion.x, motion.z);
					Vector3d newMotion = new Vector3d(
							targetDir.x * horizontalSpeed,
							motion.y,
							targetDir.z * horizontalSpeed
					);
					setDeltaMovement(newMotion);

					this.hasImpulse = true;
				}
			}
		}
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		boolean flag = false;
		if (result.getType() == RayTraceResult.Type.ENTITY) {
			Entity target = ((EntityRayTraceResult) result).getEntity();
			if (this.shouldHit(target)) {
				target.invulnerableTime = 0;
				this.dealStarDamage(target); // attack
				flag = true;
			}
		}
		this.level.broadcastEntityEvent(this, (byte) 3);
		if (flag || !this.checkLive(result)) {
			this.remove();
		}
	}

	private void dealStarDamage(Entity target) {
		target.hurt(PVZEntityDamageSource.star(this, this.getThrower()), this.getAttackDamage());
	}

	@Override
	protected int getMaxLiveTick() {
		return 40;
	}

	public float getAttackDamage() {
		float damage = this.attackDamage;
		if(this.getStarType() == StarTypes.BIG) {
			damage += 5;
		}
		if(this.getStarType() == StarTypes.HUGE) {
			damage += 10;
		}
		return damage;
	}

	@Override
	public EntitySize getDimensions(Pose poseIn) {
		if(this.getStarType() == StarTypes.BIG) {
			return EntitySize.scalable(0.5f, 0.2f);
		}
		if(this.getStarType() == StarTypes.HUGE) {
			return EntitySize.scalable(0.8f, 0.2f);
		}
		return EntitySize.scalable(0.2f, 0.2f);
	}

	@Override
	protected float getGravityVelocity() {
		return 0f;
	}

	/**
	 * Updates the entity motion clientside, called by packets from the server
	 */
	@OnlyIn(Dist.CLIENT)
	public void lerpMotion(double x, double y, double z) {
		if (isDirectionChanged()) {
			Vector3d targetDir = getTargetDirection();
			double speed = Math.sqrt(x*x + z*z);

			this.setDeltaMovement(
					targetDir.x * speed,
					y,
					targetDir.z * speed
			);
			if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
				this.yRot += 10;
				this.yRotO = this.yRot;
				this.moveTo(this.getX(), this.getY(), this.getZ(), this.yRot, this.xRot);
			}
		} else {
			super.lerpMotion(x, y, z);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("target_dir", Constants.NBT.TAG_COMPOUND)) {
			CompoundNBT dirTag = compound.getCompound("target_dir");
			this.setTargetDirection(new Vector3d(
					dirTag.getDouble("x"),
					dirTag.getDouble("y"),
					dirTag.getDouble("z")
			));
		} else if (compound.contains("target_dir_x")) {
			float x = compound.getFloat("target_dir_x");
			float z = compound.getFloat("target_dir_z");
			this.setTargetDirection(new Vector3d(x, 0, z));
		}

		if (compound.contains("start_pos", Constants.NBT.TAG_COMPOUND)) {
			CompoundNBT startTag = compound.getCompound("start_pos");
			this.setStartPos(new Vector3d(
					startTag.getDouble("x"),
					startTag.getDouble("y"),
					startTag.getDouble("z")
			));
		}

		// 读取其他参数
		this.setDirectionChanged(compound.getBoolean("direction_changed"));
		this.setChangeDistance((float) compound.getDouble("change_distance"));
		if(compound.contains("star_state")) {
			this.setStarState(StarStates.values()[compound.getInt("star_state")]);
		}
		if(compound.contains("star_type")) {
			this.setStarType(StarTypes.values()[compound.getInt("star_type")]);
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);

		CompoundNBT dirTag = new CompoundNBT();
		Vector3d targetDir = this.getTargetDirection();
		dirTag.putDouble("x", targetDir.x);
		dirTag.putDouble("y", targetDir.y);
		dirTag.putDouble("z", targetDir.z);
		compound.put("target_dir", dirTag);

		CompoundNBT startTag = new CompoundNBT();
		Vector3d startPos = this.getStartPos();
		startTag.putDouble("x", startPos.x);
		startTag.putDouble("y", startPos.y);
		startTag.putDouble("z", startPos.z);
		compound.put("start_pos", startTag);

		compound.putBoolean("direction_changed", this.isDirectionChanged());
		compound.putDouble("change_distance", this.getChangeDistance());
		compound.putInt("star_state", this.getStarState().ordinal());
		compound.putInt("star_type", this.getStarType().ordinal());
	}

	@Override
	public void onSyncedDataUpdated(DataParameter<?> key) {
		if(TARGET_DIR.equals(key)) {
			double x = entityData.get(TARGET_DIR).x;
			double z = entityData.get(TARGET_DIR).z;
			this.setTargetDirection(new Vector3d(x, 0, z));
		}
		super.onSyncedDataUpdated(key);
	}

	public StarStates getStarState() {
		return StarStates.values()[entityData.get(STAR_STATE)];
	}

	public void setStarState(StarStates state) {
		entityData.set(STAR_STATE, state.ordinal());
	}

	public StarTypes getStarType() {
		return StarTypes.values()[entityData.get(STAR_TYPE)];
	}

	public void setStarType(StarTypes type) {
		entityData.set(STAR_TYPE, type.ordinal());
	}

	public Vector3d getStartPos() {
		return this.entityData.get(START_POS);
	}

	public void setStartPos(Vector3d pos) {
		this.entityData.set(START_POS, pos);
	}

	public Vector3d getTargetDirection() {
		return this.entityData.get(TARGET_DIR);
	}

	public void setTargetDirection(Vector3d dir) {
		Vector3d horizontal = new Vector3d(dir.x, 0, dir.z);
		double lengthSqr = horizontal.lengthSqr();

		Vector3d sanitized;
		if (lengthSqr < 1.0E-4D) {
			sanitized = Vector3d.ZERO;
		} else {
			sanitized = horizontal.normalize();
		}
		this.entityData.set(TARGET_DIR, sanitized);
	}
	public double getChangeDistance() {
		return this.entityData.get(CHANGE_DISTANCE);
	}

	public void setChangeDistance(float distance) {
		this.entityData.set(CHANGE_DISTANCE, distance);
	}

	public boolean isDirectionChanged() {
		return this.entityData.get(DIR_CHANGED);
	}

	public void setDirectionChanged(boolean changed) {
		this.entityData.set(DIR_CHANGED, changed);
	}

	public enum StarStates {
		YELLOW, PINK
	}

	public enum StarTypes {
		NORMAL, BIG, HUGE
	}

}