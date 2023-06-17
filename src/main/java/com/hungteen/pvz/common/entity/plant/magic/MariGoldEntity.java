package com.hungteen.pvz.common.entity.plant.magic;

import com.hungteen.pvz.api.types.IPlantType;
import com.hungteen.pvz.common.entity.misc.drop.CoinEntity;
import com.hungteen.pvz.common.entity.misc.drop.CoinEntity.CoinType;
import com.hungteen.pvz.common.entity.misc.drop.SunEntity;
import com.hungteen.pvz.common.entity.plant.base.PlantProducerEntity;
import com.hungteen.pvz.common.impl.plant.PVZPlants;
import com.hungteen.pvz.common.entity.EntityRegister;
import com.hungteen.pvz.utils.EntityUtil;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.world.World;

public class MariGoldEntity extends PlantProducerEntity {

	public MariGoldEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
		super(type, worldIn);
	}

	@Override
	public void genSomething() {
		SunEntity sun = EntityRegister.SUN.get().create(level);
		sun.setAmount(this.getRandomAmount());
		EntityUtil.onEntityRandomPosSpawn(level, sun, blockPosition(), 3);
	}

	protected void genSpecCoin(SunEntity.SunType type) {
		SunEntity sun = EntityRegister.SUN.get().create(level);
		sun.setAmountByType(type);
		EntityUtil.onEntityRandomPosSpawn(level, sun, blockPosition(), 3);
	}

	@Override
	public void genSuper() {
		for (int i = 0; i < this.getSuperGenCnt(); ++i) {
			this.genSomething();
		}
		this.genSpecCoin(SunEntity.SunType.BIG);
	}

	private int getRandomAmount() {
		final int num = this.getRandom().nextInt(100);
		if (num <= 50) {
			return SunEntity.SunType.BIG.sun;
		}
		return SunEntity.SunType.MEDIUM.sun;
	}
	


	public int getSuperGenCnt() {
		return 5;
	}

	@Override
	public int getGenCD() {
		return 480;
	}
	
	@Override
	public EntitySize getDimensions(Pose poseIn) {
		return EntitySize.scalable(0.8f, 1.6f);
	}

	@Override
	public IPlantType getPlantType() {
		return PVZPlants.MARIGOLD;
	}

}
