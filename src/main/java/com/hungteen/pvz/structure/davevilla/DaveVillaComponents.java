package com.hungteen.pvz.structure.davevilla;

import java.util.List;
import java.util.Random;

import com.hungteen.pvz.entity.npc.CrazyDaveEntity;
import com.hungteen.pvz.entity.npc.PanneyEntity;
import com.hungteen.pvz.entity.plant.appease.PeaShooterEntity;
import com.hungteen.pvz.entity.plant.defence.WallNutEntity;
import com.hungteen.pvz.entity.plant.light.SunFlowerEntity;
import com.hungteen.pvz.register.EntityRegister;
import com.hungteen.pvz.register.StructureRegister;
import com.hungteen.pvz.utils.EntityUtil;
import com.hungteen.pvz.utils.StringUtil;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DaveVillaComponents {
	
	private static final BlockPos STRUCTURE_OFFSET = new BlockPos(0, 0, 0);
	public static final BlockState BASE_BLOCK = Blocks.DIRT.getDefaultState();
	public static final ResourceLocation res1 = StringUtil.prefix("dave_villa/davevilla1");
	public static final ResourceLocation res2 = StringUtil.prefix("dave_villa/davevilla2");
	public static final ResourceLocation res3 = StringUtil.prefix("dave_villa/davevilla3");
	public static final ResourceLocation res4 = StringUtil.prefix("dave_villa/davevilla4");
	
	public static void generate(TemplateManager manager, BlockPos pos1, Rotation rotation, List<StructurePiece> list, Random rand) {
	      BlockPos pos2,pos3,pos4;
//	      System.out.println(rotation);
	      switch (rotation) {
		  case CLOCKWISE_90:{
			  pos2=pos1.add(0, 0, 32);
			  pos3=pos1.add(-32, 0, 0);
			  pos4=pos2.add(-32, 0, 0);
			  break;
		  }
		  case CLOCKWISE_180:{
			  pos2=pos1.add(-32, 0, 0);
			  pos3=pos1.add(0, 0, -32);
			  pos4=pos2.add(0, 0, -32);
			  break;
		  }
		  case COUNTERCLOCKWISE_90:{
			  pos2=pos1.add(0, 0, -32);
			  pos3=pos1.add(32, 0, 0);
			  pos4=pos2.add(32, 0, 0);
			  break;
		  }
		  default:{
			  pos2=pos1.add(32, 0, 0);
			  pos3=pos1.add(0, 0, 32);
			  pos4=pos2.add(0, 0, 32);
			  break;
		  }
		  }
	      list.add(new DaveVillaComponent(manager, res1, pos1, rotation));
	      list.add(new DaveVillaComponent(manager, res2, pos2, rotation));
	      list.add(new DaveVillaComponent(manager, res3, pos3, rotation));
	      list.add(new DaveVillaComponent(manager, res4, pos4, rotation));
    }
	
	public static class DaveVillaComponent extends TemplateStructurePiece {

		protected final Rotation rotation;
		private final ResourceLocation res;
//		protected final BlockPos midPos;

		public DaveVillaComponent(TemplateManager manager, ResourceLocation res,BlockPos pos, Rotation rotation) {
			super(StructureRegister.DAVE_VILLA, 0);
			this.templatePosition = pos;
			this.rotation = rotation;
			this.res=res;
//			this.midPos=blockpos;
			this.setUpTemplate(manager);
		}

		public DaveVillaComponent(TemplateManager manager, CompoundNBT nbt) {
			super(StructureRegister.DAVE_VILLA, nbt);
			this.res = new ResourceLocation(nbt.getString("Template"));
			this.rotation = Rotation.valueOf(nbt.getString("Rot"));
//			this.midPos = new BlockPos(nbt.getInt("midX"),nbt.getInt("midY"),nbt.getInt("midZ"));
			this.setUpTemplate(manager);
		}

		private void setUpTemplate(TemplateManager p_204754_1_) {
			Template template = p_204754_1_.getTemplateDefaulted(this.res);
			PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation)
					.setMirror(Mirror.NONE).setCenterOffset(DaveVillaComponents.STRUCTURE_OFFSET)
					.addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
			this.setup(template, this.templatePosition, placementsettings);
		}
		
		public BlockPos getBlockPos()
		{
			return this.templatePosition;
		}
		
		@Override
		protected void readAdditional(CompoundNBT tagCompound) {
			super.readAdditional(tagCompound);
			tagCompound.putString("Template", this.res.toString());
	        tagCompound.putString("Rot", this.rotation.name());
		}

		@Override
		protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand,
				MutableBoundingBox sbb) {
			if(function.equals("dave")) {
				CrazyDaveEntity dave = EntityRegister.CRAZY_DAVE.get().create(worldIn.getWorld());
				EntityUtil.onMobEntitySpawn(worldIn, dave, pos);
			}else if(function.equals("panney")){
				PanneyEntity panney = EntityRegister.PANNEY.get().create(worldIn.getWorld());
				EntityUtil.onMobEntitySpawn(worldIn, panney, pos);
			}else if(function.equals("plant_pos1")){
				if(rand.nextInt(3)==0) {
					PeaShooterEntity nut = EntityRegister.PEA_SHOOTER.get().create(worldIn.getWorld());
					worldIn.setBlockState(pos.down(), Blocks.GRASS_BLOCK.getDefaultState(), 3);
					EntityUtil.onMobEntitySpawn(worldIn, nut, pos);
				}
			}else if(function.equals("plant_pos2")){
				if(rand.nextInt(3)==0) {
					SunFlowerEntity nut = EntityRegister.SUN_FLOWER.get().create(worldIn.getWorld());
					worldIn.setBlockState(pos.down(), Blocks.GRASS_BLOCK.getDefaultState(), 3);
					EntityUtil.onMobEntitySpawn(worldIn, nut, pos);
				}
			}else if(function.equals("plant_pos3")){
				if(rand.nextInt(3)==0) {
					PeaShooterEntity nut = EntityRegister.PEA_SHOOTER.get().create(worldIn.getWorld());
					worldIn.setBlockState(pos.down(), Blocks.GRASS_BLOCK.getDefaultState(), 3);
					EntityUtil.onMobEntitySpawn(worldIn, nut, pos);
				}
			}else if(function.equals("plant_pos4")){
				if(rand.nextInt(3)==0) {
					WallNutEntity nut = EntityRegister.WALL_NUT.get().create(worldIn.getWorld());
					worldIn.setBlockState(pos.down(), Blocks.GRASS_BLOCK.getDefaultState(), 3);
					EntityUtil.onMobEntitySpawn(worldIn, nut, pos);
				}
			}else if(function.equals("plant_pos5")){
				if(rand.nextInt(2)==0) {
					SunFlowerEntity nut = EntityRegister.SUN_FLOWER.get().create(worldIn.getWorld());
					worldIn.setBlockState(pos.down(), Blocks.GRASS_BLOCK.getDefaultState(), 3);
					EntityUtil.onMobEntitySpawn(worldIn, nut, pos);
				}
			}
		}
		
		@Override
		public boolean create(IWorld worldIn, ChunkGenerator<?> chunkGeneratorIn, Random randomIn,
				MutableBoundingBox mutableBoundingBoxIn, ChunkPos chunkPosIn) {
			BlockPos pos = this.templatePosition;
			BlockPos size = this.template.getSize();
			BlockPos ab = new BlockPos(0, size.getY(), 0);
			switch(rotation) {
			case CLOCKWISE_90:{
				ab=ab.add(-size.getZ(), 0, size.getX());
				break;
			}
			case CLOCKWISE_180:{
				ab=ab.add(-size.getX(), 0, -size.getZ());
				break;
			}
			case COUNTERCLOCKWISE_90:{
				ab=ab.add(size.getZ(), 0, -size.getX());
				break;
			}
			default:{
				ab=ab.add(size.getX(), 0, size.getZ());
				break;
			}
			}
			BlockPos to = pos.add(ab.getX(), ab.getY(), ab.getZ());
			BlockPos min = new BlockPos(Math.min(pos.getX(), to.getX()),Math.min(pos.getY(), to.getY()),Math.min(pos.getZ(), to.getZ()));
			BlockPos max = new BlockPos(Math.max(pos.getX(), to.getX()),Math.max(pos.getY(), to.getY()),Math.max(pos.getZ(), to.getZ()));
//			this.fillWithAir(worldIn, mutableBoundingBoxIn, min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
//			this.fillWithBlocks(worldIn, boundingboxIn, xMin, yMin, zMin, xMax, yMax, zMax, boundaryBlockState, insideBlockState, existingOnly);
//			System.out.println(min.getX()+" "+min.getY()+" "+min.getZ()+" "+max.getX()+" "+max.getY()+" "+max.getZ());
			super.create(worldIn, chunkGeneratorIn, randomIn, mutableBoundingBoxIn, chunkPosIn);
			for(int i=min.getX();i<=max.getX();i++) {
				for(int j=min.getZ();j<=max.getZ();j++) {
					int y=min.getY()-1;
					while(y>=50) {
						BlockPos tmp = new BlockPos(i,y,j);
						if(worldIn.getBlockState(tmp).isSolid()) {
							break;
						}
						else {
							worldIn.setBlockState(tmp, BASE_BLOCK, 3);
						}
						y--;
					}
				}
			}
			return true;
		}
	}
	
}
