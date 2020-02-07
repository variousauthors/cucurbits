package com.arrogantgamer.cucurbits.block.lavamelon;

import java.util.Random;

import javax.annotation.Nullable;

import com.arrogantgamer.cucurbits.ModItems;
import com.arrogantgamer.cucurbits.block.ModCropsBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/*
*    - [o] seeds, stem, attached stem, fruit
*    - [o] it searches below for lava and only grows fruit if it can find lava
*      - [o] how does it search? randomly. :. it gets slower and slower the less laval is below it
*      - [o] where does it search? for now I am stuck on this 3x3x5 area inspired by grass... but let's see
*        this encourages planting a patch of them to harvest the magma from a pool, but a
*        more dynamic shape, like a + or a ♢ encourages more dynamic planting patterns...
*        (actually yes, lets take inspiration from Melon with its strange growth patterns
*        this makes for more interesting gameplay)
*    - it generates around lava lakes? (but in that case, it should be able to search an area
*      that lets it pull laval from a lava pool even if it is on the edge of the pool...
*      - in addition, what is it plantable in? Can it grow in sand??? Gravel???
*        - PROBLEM <- sand and gravel get to decide what can be planted in them :D
*        - SOLUTION <- I can override the methods in stem that care about canSustainPlant 
*    - [o] the fruit
*      - [o] it can be crafted with a bucket to make a lava bucket
*      - [] it can be used instead of a lava bucket in recipes
*      - [o] it stacks and has the same burntime as lava
*        
*      - click with sheers, it releases the lava on that side (and carves the melon)
*        - click the top, it becomes like a lava cauldron: nooby says have a "whole" and "sliced"
*        state, and "lava" and "not lava" states
*        - I think I would just do a whole "lavamelon cauldron" block though
*      - [?] put it in a fluid extracting machine, and it will extract the lava
*      - [?] it can be used like a lava bucket in machines
*        - tested with silents mechanisms and it worked OK
*        - need to test with other tech mods
*    - wearing a carved one on your head provides some degree of fire protection  
*    
*/
public class LavamelonStemBlock extends StemBlock {
    public static Block.Properties properties = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.STEM);
    protected final StemGrownBlock crop;

    public LavamelonStemBlock(StemGrownBlock p_i48318_1_, Properties properties) {
	super(p_i48318_1_, properties);
	this.crop = p_i48318_1_;
	this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
	if (!worldIn.isAreaLoaded(pos, 1)) {
	    return; // Forge: prevent loading unloaded chunks when checking neighbor's light	    
	}

	if (worldIn.getLightSubtracted(pos, 0) >= 9) {
	    float f = ModCropsBlock.getGrowthChance(this, worldIn, pos);
	    if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
		int i = state.get(AGE);
		if (i < 7) {
		    worldIn.setBlockState(pos, state.with(AGE, Integer.valueOf(i + 1)), 2);
		} else {
		    BlockPos fuelpos = findFuel(worldIn, pos, random);
		    Direction direction = Direction.Plane.HORIZONTAL.random(random);
		    BlockPos fruitPos = pos.offset(direction);

		    if (canFruit(worldIn, fuelpos, fruitPos)) {
			createFruit(worldIn, fruitPos, fuelpos);
			attachStem(worldIn, pos, direction);
		    }
		}
		
		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
	    }
	}
    }
    
    private boolean isVanillaSoil(BlockState soil) {
	Block block = soil.getBlock();

	return block == Blocks.FARMLAND || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.GRASS_BLOCK;
    }
    
    private boolean isSuitableForFruit (World worldIn, BlockPos fruitPos) {
	BlockState soil = worldIn.getBlockState(fruitPos.down());

	return soil.canSustainPlant(worldIn, fruitPos.down(), Direction.UP, this) || isVanillaSoil(soil);
    }
    
    protected void attachStem (World worldIn, BlockPos pos, Direction direction) {
	worldIn.setBlockState(pos, this.crop.getAttachedStem().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, direction));
    }    

    protected void createFruit(World worldIn, BlockPos blockpos, BlockPos fuelpos) {
	consumeFuel(worldIn, fuelpos);

	worldIn.setBlockState(blockpos, this.crop.getDefaultState());
    }    
    
    protected void consumeFuel(World worldIn, BlockPos blockpos) {
	worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState());
    }

    private BlockPos getRandomPosition (World worldIn, BlockPos blockpos, Random random) {
	return blockpos.add(random.nextInt(3) - 1, random.nextInt(5) - 5, random.nextInt(3) - 1);
    }
    
    @Nullable
    protected BlockPos findFuel(World worldIn, BlockPos blockpos, Random random) {
	BlockPos fuelpos = null;

	for (int i = 0; i < 4; ++i) {
	    fuelpos = this.getRandomPosition(worldIn, blockpos, random);
	    BlockState potentialfuel = worldIn.getBlockState(fuelpos);

	    if (isFuel(potentialfuel)) {
		break;
	    }

	    fuelpos = null;
	}

	return fuelpos;
    }
    
    protected boolean isFuel (BlockState potentialFuel) {
	return potentialFuel.getBlock() == Blocks.LAVA
		&& potentialFuel.getBlock().getFluidState(potentialFuel).isSource();
    }

    protected boolean canFruit(World worldIn, @Nullable BlockPos fuelpos, BlockPos targetPos) {
	return fuelpos != null 
		&& worldIn.getBlockState(targetPos).isAir(worldIn, targetPos) 
		&& this.isFuel(worldIn.getBlockState(fuelpos))
		&& this.isSuitableForFruit(worldIn, targetPos);
    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    protected Item getSeedItem() {
	return ModItems.LAVAMELON_SEEDS;
    }

    @Override
    public StemGrownBlock getCrop() {
	return this.crop;
    }
}
