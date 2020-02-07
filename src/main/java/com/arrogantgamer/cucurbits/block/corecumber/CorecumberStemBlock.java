package com.arrogantgamer.cucurbits.block.corecumber;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/*
 *    - [o] it searches below it in a 3x3 column for ores, and makes fruit
 *    - [] it leaves "porous stone" when it pulls out ores (for now just do mossy cobblestone)
 *    - [o] the fruit, when broken, drop the drops from the ore
 *    - [o] how do we store the loot in the fruit!
 *        - [o] you should not be able to extract/insert items in any way
 **/
public class CorecumberStemBlock extends StemBlock {
    public static Block.Properties properties = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.STEM);

    private ResourceLocation oreTagId = new ResourceLocation("forge", "ores");
    protected final StemGrownBlock crop;

    public CorecumberStemBlock(StemGrownBlock p_i48318_1_, Properties properties) {
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
	List<ItemStack> products = consumeFuel(worldIn, fuelpos);

	if (products.isEmpty()) {
	    return;
	}
	
	if (worldIn.setBlockState(blockpos, this.crop.getDefaultState())) {
	    CorecumberBlock fruit = (CorecumberBlock) worldIn.getBlockState(blockpos).getBlock();
	    products.forEach((product) -> fruit.setContainedItem(worldIn, blockpos, product));
	}
    }

    protected List<ItemStack> consumeFuel(World worldIn, BlockPos fuelpos) {
	BlockState potentialOre = worldIn.getBlockState(fuelpos);

	if (worldIn instanceof ServerWorld) {
	    if (isFuel(potentialOre)) {
		LootContext.Builder builder = (new LootContext.Builder((ServerWorld) worldIn)).withRandom(worldIn.rand).withParameter(LootParameters.POSITION, fuelpos)
			.withParameter(LootParameters.TOOL, ItemStack.EMPTY);

		List<ItemStack> drops = potentialOre.getDrops(builder);

		worldIn.setBlockState(fuelpos, Blocks.MOSSY_COBBLESTONE.getDefaultState());

		return drops;
	    }
	}

	return Collections.emptyList();
    }

    private BlockPos getRandomPosition (World worldIn, BlockPos blockpos, Random random) {
	return blockpos.add(random.nextInt(3) - 1, random.nextInt(blockpos.getY()) - blockpos.getY() - 1, random.nextInt(3) - 1);
    }    
    
    @Nullable
    protected BlockPos findFuel(World worldIn, BlockPos blockpos, Random random) {
	BlockPos fuelpos = null;

	for (int i = 0; i < 4; ++i) {
	    fuelpos = this.getRandomPosition(worldIn, blockpos, random);
	    BlockState potentialFuel = worldIn.getBlockState(fuelpos);

	    if (isFuel(potentialFuel)) {
		break;
	    }

	    fuelpos = null;
	}

	return fuelpos;
    }

    private boolean isFuel(BlockState potentialOre) {
	return BlockTags.getCollection().getOrCreate(oreTagId).contains(potentialOre.getBlock());
    }

    /** runs the checklist:
     * [] is the target empty
     * [] did we find fuel
     * [] is the fuel what we expected it to be
     * [] is the target a suitable location for fruit
     * */
    protected boolean canFruit(World worldIn, @Nullable BlockPos fuelpos, BlockPos targetPos) {
	// check that we did find fuel
	return fuelpos != null 
		&& worldIn.getBlockState(targetPos).isAir(worldIn, targetPos) 
		&& isFuel(worldIn.getBlockState(fuelpos))
		&& this.isSuitableForFruit(worldIn, targetPos);
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
