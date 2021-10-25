package com.hungteen.pvz.common.item.spawn;

import java.util.List;

import com.hungteen.pvz.common.entity.zombie.roof.Edgar090505Entity;
import com.hungteen.pvz.common.item.PVZItemGroups;
import com.hungteen.pvz.register.EntityRegister;
import com.hungteen.pvz.utils.EntityUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class ZomBossDollItem extends Item {

	public ZomBossDollItem() {
		super(new Item.Properties().tab(PVZItemGroups.PVZ_MISC));
	}
	
	@Override
	public ActionResultType useOn(ItemUseContext context) {
		final PlayerEntity player = context.getPlayer();
		final World world = context.getLevel();
		final BlockPos pos = context.getClickedPos();
		if(! world.isClientSide && ! player.getCooldowns().isOnCooldown(this) && context.getClickedFace() == Direction.UP) {
			if(this.canSpawnHere(world, pos)) {
				Edgar090505Entity zomboss = EntityRegister.EDGAR_090505.get().create(world);
				EntityUtil.onEntitySpawn(world, zomboss, pos.above());
				context.getItemInHand().shrink(1);
			}
		}
		return super.useOn(context);
	}
	
	@Override
	public void appendHoverText(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("tooltip.pvz.zomboss_doll").withStyle(TextFormatting.RED));
	}
	
	@SuppressWarnings("deprecation")
	private boolean canSpawnHere(World world, BlockPos pos) {
		for(int i = - 5; i <= 5; ++ i) {
			for(int j = - 5; j <= 5; ++ j) {
				for(int k = 1; k <= 12; ++ k) {
					BlockPos tmp = pos.offset(i, k, j);
					if(! world.getBlockState(tmp).isAir(world, pos)) {
						return false;
					}
				}
			}
		}
		return world.getBlockState(pos).canOcclude();
	}

}