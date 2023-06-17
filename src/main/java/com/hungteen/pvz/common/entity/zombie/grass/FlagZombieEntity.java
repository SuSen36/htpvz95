package com.hungteen.pvz.common.entity.zombie.grass;

import com.hungteen.pvz.client.model.entity.zombie.grass.BucketHeadZombieModel;
import com.hungteen.pvz.common.impl.zombie.GrassZombies;
import com.hungteen.pvz.common.impl.zombie.ZombieType;
import com.hungteen.pvz.common.potion.EffectRegister;
import com.hungteen.pvz.utils.ZombieUtil;
import com.hungteen.pvz.utils.interfaces.IHasMetal;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class FlagZombieEntity extends NormalZombieEntity implements IHasMetal {

	public FlagZombieEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public int getAttackCD() {
		if (!this.canNormalUpdate()) {//can not update means stop attack.
			return 10000000;
		}
		int cd = 12;
		if (this.hasEffect(EffectRegister.COLD_EFFECT.get())) {//cold will decrease attack CD.
			int lvl = this.getEffect(EffectRegister.COLD_EFFECT.get()).getAmplifier();
			cd += 3 * lvl;
		}else if (this.hasEffect(Effects.DIG_SPEED)) {//cold will decrease attack CD.
			int lvl = this.getEffect(Effects.DIG_SPEED).getAmplifier();
			cd -= 2 * lvl;
		}
		return cd;
	}
	@Override
	public float getWalkSpeed() {
		return ZombieUtil.WALK_LITTLE_FAST*1.8f;
	}

	@Override
	public float getLife() {
		return 19;
	}

	@Override
	public float getInnerLife() {
		return 110;
	}

	@Override
	public boolean hasMetal() {
		return this.getInnerDefenceLife() > 0;
	}

	@Override
	public void decreaseMetal() {
		this.setInnerDefenceLife(0);
	}

	@Override
	public void increaseMetal() {
		this.setInnerDefenceLife(this.getInnerLife());
	}

	/**
	 * check the visibility of buckethead model.
	 * {@link BucketHeadZombieModel#updateFreeParts(BucketHeadZombieEntity)}
	 */
	public boolean hasBucketHead(int stage) {
		final double percent = this.getInnerDefenceLife() / this.getInnerLife();
		if(stage == 3) {
			return percent > 2.0f / 3;
		} else if(stage == 2) {
			return percent > 1.0f / 3;
		} else if(stage == 1) {
			return percent > 0;
		}
		return false;
	}
	
	@Override
	public ZombieType getZombieType() {
		return GrassZombies.FLAG_ZOMBIE;
	}
}
