package com.hungteen.pvz.common.entity.zombie.part;

import com.hungteen.pvz.common.entity.zombie.base.DefenceZombieEntity;
import com.hungteen.pvz.common.misc.damage.PVZDamageSource;
import net.minecraft.entity.EntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class PVZHealthPartEntity extends PVZZombiePartEntity{

	protected DefenceZombieEntity zombie;
	
	public PVZHealthPartEntity(EntityType<?> entityTypeIn, World worldIn) {
		super(entityTypeIn, worldIn);
	}
	
	public PVZHealthPartEntity(DefenceZombieEntity owner, float sizeX, float sizeY) {
		super(owner, sizeX, sizeY);
		this.zombie = owner;
	}
	
	@Override
	public boolean hurt(DamageSource source, float damage) {
		//the source can pass through defence and deal damage to owner directly.
		if(source instanceof PVZDamageSource) {
			if(((PVZDamageSource) source).isThroughDamage()) {
				return super.hurt(source, damage);
			}
			((PVZDamageSource) source).setDefended(true);
		}
		return super.hurt(source, damage);
	}
	
	@Override
	public boolean canExist() {
		return super.canExist() && this.zombie.canPartsExist();
	}
	
}
