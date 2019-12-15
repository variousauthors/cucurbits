package com.arrogantgamer.cucurbits.block;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.arrogantgamer.cucurbits.Cucurbits;

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
			List<ItemStack> products = consumeFuel(worldIn, fuelpos);
			worldIn.setBlockState(blockpos, this.getFruit(products));
			worldIn.setBlockState(pos, this.crop.getAttachedStem().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, direction));
		    }
		}
		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
	    }

	}
    }

    protected BlockState getFruit (List<ItemStack> products) {
	if (products.isEmpty()) return this.crop.getDefaultState();
	
	Cucurbits.LOGGER.debug("found: ");
	
	products.forEach((product) -> {
	    Cucurbits.LOGGER.debug(product.getItem().getRegistryName());
	});
	// get the crop and fill it with the ore we found
	
	return this.crop.getDefaultState();
    }
    
    protected List<ItemStack> consumeFuel(World worldIn, BlockPos blockpos) {
	BlockState potentialOre = worldIn.getBlockState(blockpos);
	Cucurbits.LOGGER.debug("-> consumeFuel");

	if (isOre(potentialOre)) {
		Cucurbits.LOGGER.debug("-> isOre");
	    if (worldIn instanceof ServerWorld) {
		Cucurbits.LOGGER.debug("-> isServer");

                LootContext.Builder builder = (new LootContext.Builder((ServerWorld)worldIn)).withRandom(worldIn.rand).withParameter(LootParameters.POSITION, blockpos).withParameter(LootParameters.TOOL, ItemStack.EMPTY);
		List<ItemStack> drops = potentialOre.getDrops(builder);
		// TODO do this later once things are working otherwise we will destroy all the ore down there for testing :D
		// worldIn.setBlockState(blockpos, Blocks.COBBLESTONE.getDefaultState());	
		
		return drops;
	    }
	}
	
	return Collections.emptyList();
    }

    private ResourceLocation oreTagId = new ResourceLocation("forge", "ores");

    
    @Nullable
    protected BlockPos findFuel(World worldIn, BlockPos blockpos, Random random) {
	BlockPos fuelpos = null;

	for (int i = 0; i < 4; ++i) {
	    fuelpos = blockpos.add(random.nextInt(3) - 1, random.nextInt(blockpos.getY()) - blockpos.getY() - 1, random.nextInt(3) - 1);
	    BlockState potentialOre = worldIn.getBlockState(fuelpos);

	    Cucurbits.LOGGER.debug("fuel found: " + potentialOre.getBlock().getRegistryName());

	    // check the drops for ore tag?
	    if (isOre(potentialOre)) {
		Cucurbits.LOGGER.debug("success!");

		break;
	    };

	    fuelpos = null;
	}

	return fuelpos;
    }
    
    private boolean isOre(BlockState potentialOre) {
	return BlockTags.getCollection().getOrCreate(oreTagId).contains(potentialOre.getBlock());
    }

    protected boolean canFruit(World worldIn, @Nullable BlockPos fuelpos, BlockPos blockpos) {
	// check that we did find fuel
	return fuelpos != null && worldIn.getBlockState(blockpos).isAir(worldIn, blockpos) && isOre(worldIn.getBlockState(fuelpos));
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
