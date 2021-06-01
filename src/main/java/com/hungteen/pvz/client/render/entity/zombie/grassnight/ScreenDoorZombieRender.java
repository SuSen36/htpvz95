package com.hungteen.pvz.client.render.entity.zombie.grassnight;

import com.hungteen.pvz.client.model.entity.zombie.grassnight.ScreenDoorZombieModel;
import com.hungteen.pvz.client.render.entity.zombie.OldPVZZombieRender;
import com.hungteen.pvz.common.entity.zombie.grassnight.ScreenDoorZombieEntity;
import com.hungteen.pvz.utils.StringUtil;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ScreenDoorZombieRender extends OldPVZZombieRender<ScreenDoorZombieEntity>{

	public ScreenDoorZombieRender(EntityRendererManager rendererManager) {
		super(rendererManager, new ScreenDoorZombieModel(), 0.5f);
	}

	@Override
	protected float getScaleByEntity(ScreenDoorZombieEntity entity) {
		if(entity.isMiniZombie()) return 0.15F;
		return 0.5f;
	}

	@Override
	public ResourceLocation getTextureLocation(ScreenDoorZombieEntity entity) {
		return StringUtil.prefix("textures/entity/zombie/grassnight/screendoor_zombie.png");
	}

}