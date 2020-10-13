package com.hungteen.pvz.data;

import com.hungteen.pvz.PVZMod;
import com.hungteen.pvz.register.ItemRegister;
import com.hungteen.pvz.utils.StringUtil;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;

public class ItemModelGenerator extends ItemModelProvider{

	public ItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator, PVZMod.MOD_ID, existingFileHelper);
	}

	@Override
	public String getName() {
		return "Plants vs Zombies item and itemblock models";
	}

	@Override
	protected void registerModels() {
		generated(ItemRegister.HYPNO_SHROOM_ENJOY_CARD.getId().getPath(), StringUtil.prefix("item/"+ItemRegister.HYPNO_SHROOM_ENJOY_CARD.getId().getPath()));
	}
	
	private ItemModelBuilder generated(String name, ResourceLocation... layers) {
		ItemModelBuilder builder = withExistingParent(name, "item/generated");
		for (int i = 0; i < layers.length; i++) {
			builder = builder.texture("layer" + i, layers[i]);
		}
		return builder;
	}

}
