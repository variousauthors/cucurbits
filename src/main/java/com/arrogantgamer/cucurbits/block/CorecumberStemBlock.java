package com.arrogantgamer.cucurbits.block;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class CorecumberStemBlock extends StemBlock {
    public static Block.Properties properties = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.STEM);
    protected final StemGrownBlock crop;

    public CorecumberStemBlock(StemGrownBlock p_i48318_1_, Properties properties) {
	super(p_i48318_1_, properties);
	this.crop = p_i48318_1_;
	this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
	if (!worldIn.isAreaLoaded(pos, 1))
	    return; // Forge: prevent loading unloaded chunks when checking neighbor's light
	if (worldIn.getLightSubtracted(pos, 0) >= 9) {
	    float f = ModCropsBlock.getGrowthChance(this, worldIn, pos);
	    if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
		int i = state.get(AGE);
		if (i < 7) {
		    worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i + 1)), 2);
		} else {
		    BlockPos fuelpos = findFuel(worldIn, pos, random);

		    Direction direction = Direction.Plane.HORIZONTAL.random(random);
		    BlockPos blockpos = pos.offset(direction);
		    BlockState soil = worldIn.getBlockState(blockpos.down());
		    Block block = soil.getBlock();
		    if (canFruit(worldIn, fuelpos, blockpos) && (soil.canSustainPlant(worldIn, blockpos.down(), Direction.UP, this) || block == Blocks.FARMLAND
			    || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.GRASS_BLOCK)) {
			consumeFuel(worldIn, fuelpos);
			worldIn.setBlockState(blockpos, this.getFruit());
			worldIn.setBlockState(pos, this.crop.getAttachedStem().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, direction));
		    }
		}
		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
	    }

	}
    }

    protected BlockState getFruit () {
	return this.crop.getDefaultState();
    }
    
    protected void consumeFuel(World worldIn, BlockPos blockpos) {
	worldIn.setBlockState(blockpos, Blocks.COBBLESTONE.getDefaultState());
    }

    @Nullable
    protected BlockPos findFuel(World worldIn, BlockPos blockpos, Random random) {
	BlockPos fuelpos = null;

	for (int i = 0; i < 4; ++i) {
	    fuelpos = blockpos.add(random.nextInt(3) - 1, random.nextInt(5) - 5, random.nextInt(3) - 1);

	    // check the drops for ore tag?
	    if (worldIn.getBlockState(fuelpos).getMaterial() == Material.LAVA) {
		break;
	    }

	    fuelpos = null;
	}

	return fuelpos;
    }

    protected boolean canFruit(World worldIn, @Nullable BlockPos fuelpos, BlockPos blockpos) {
	// check that we did find fuel
	return fuelpos != null && worldIn.getBlockState(blockpos).isAir(worldIn, blockpos) && worldIn.getBlockState(fuelpos).getMaterial() == Material.LAVA;
    }

    // I'm not sure that we need this...
    @Nullable
    @OnlyIn(Dist.CLIENT)
    protected Item getSeedItem() {
	// return ModItems.CORECUMBER_SEEDS
	if (this.crop == Blocks.PUMPKIN) {
	    return Items.PUMPKIN_SEEDS;
	} else {
	    return this.crop == Blocks.MELON ? Items.MELON_SEEDS : null;
	}
    }

    @Override
    public StemGrownBlock getCrop() {
	return this.crop;
    }
}
