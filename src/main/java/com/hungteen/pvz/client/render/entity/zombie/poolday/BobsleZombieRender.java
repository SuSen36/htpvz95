package com.hungteen.pvz.client.render.entity.zombie.poolday;

import com.hungteen.pvz.client.model.entity.zombie.poolday.BobsleZombieModel;
import com.hungteen.pvz.client.render.entity.zombie.OldPVZZombieRender;
import com.hungteen.pvz.common.entity.zombie.poolday.BobsleZombieEntity;
import com.hungteen.pvz.utils.StringUtil;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BobsleZombieRender extends OldPVZZombieRender<BobsleZombieEntity>{

	public BobsleZombieRender(EntityRendererManager rendererManager) {
		super(rendererManager, new BobsleZombieModel(), 0.5f);
	}

	@Override
	protected float getScaleByEntity(BobsleZombieEntity entity) {
		if(entity.isMiniZombie()) return 0.15F;
		return 0.5f;
	}

	@Override
	public ResourceLocation getTextureLocation(BobsleZombieEntity entity) {
		return StringUtil.prefix("textures/entity/zombie/poolday/bobsle_zombie.png");
	}

}