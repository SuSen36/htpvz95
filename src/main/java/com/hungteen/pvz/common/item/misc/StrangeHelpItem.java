package com.hungteen.pvz.common.item.misc;

import com.hungteen.pvz.client.gui.screen.StrangeHelpScreen;
import com.hungteen.pvz.common.item.PVZItemGroups;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class StrangeHelpItem extends Item{

	public StrangeHelpItem() {
		super(new Properties().tab(PVZItemGroups.PVZ_MISC).stacksTo(1));
	}
	
	@SuppressWarnings("deprecation")
	@OnlyIn(Dist.CLIENT)
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		if(worldIn.isClientSide) {
			DistExecutor.runWhenOn(Dist.CLIENT, ()->()->{
				Minecraft.getInstance().setScreen(new StrangeHelpScreen());
			});
		}
		return ActionResult.pass(playerIn.getItemInHand(handIn));
	}

}
