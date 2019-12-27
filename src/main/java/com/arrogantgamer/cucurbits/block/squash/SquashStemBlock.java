package com.arrogantgamer.cucurbits.block.squash;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.arrogantgamer.cucurbits.block.ModCropsBlock;
import com.arrogantgamer.cucurbits.tileEntity.SquashStemTileEntity;

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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SquashStemBlock extends StemBlock {
    public static Block.Properties properties = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.STEM);

    protected final StemGrownBlock crop;

    public SquashStemBlock(StemGrownBlock p_i48318_1_, Properties properties) {
	super(p_i48318_1_, properties);
	this.crop = p_i48318_1_;
	this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
	return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
	return new SquashStemTileEntity();
    }

    private TileEntity getTileEntity(World worldIn, BlockPos pos) {
	return worldIn.getTileEntity(pos);
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos stempos, Random random) {
	if (!worldIn.isAreaLoaded(stempos, 1))
	    return; // Forge: prevent loading unloaded chunks when checking neighbor's light
	if (worldIn.getLightSubtracted(stempos, 0) >= 9) {
	    float f = ModCropsBlock.getGrowthChance(this, worldIn, stempos);
	    if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, stempos, state, random.nextInt((int) (25.0F / f) + 1) == 0)) {
		int i = state.get(AGE);
		if (i < 7) {
		    worldIn.setBlockState(stempos, state.with(AGE, Integer.valueOf(i + 1)), 2);
		} else {
		    BlockPos fuelpos = findFuel(worldIn, stempos, random);

		    Direction direction = Direction.Plane.HORIZONTAL.random(random);
		    BlockPos targetpos = stempos.offset(direction);
		    BlockState soil = worldIn.getBlockState(targetpos.down());
		    Block block = soil.getBlock();

		    if (canFruit(worldIn, fuelpos, targetpos) && (soil.canSustainPlant(worldIn, targetpos.down(), Direction.UP, this) || block == Blocks.FARMLAND
			    || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.GRASS_BLOCK)) {

			if (this.tryCreateFruit(worldIn, stempos, targetpos, fuelpos)) {
			    worldIn.setBlockState(stempos, this.crop.getAttachedStem().getDefaultState().with(HorizontalBlock.HORIZONTAL_FACING, direction));
			}
		    }
		}
		net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, stempos, state);
	    }

	}
    }

    @Nullable
    protected boolean tryCreateFruit(World worldIn, BlockPos stempos, BlockPos targetpos, BlockPos fuelpos) {
	ItemStack compressed = consumeFuel(worldIn, stempos, fuelpos);

	if (compressed.isEmpty()) {
	    return false;
	}

	if (!worldIn.setBlockState(targetpos, this.crop.getDefaultState())) {
	    return false;
	}
	
	SquashBlock fruit = (SquashBlock) worldIn.getBlockState(targetpos).getBlock();
	fruit.setContainedItem(worldIn, targetpos, compressed);

	return true;
    }

    protected ItemStack consumeFuel(World worldIn, BlockPos stempos, BlockPos fuelpos) {
	if (worldIn instanceof ServerWorld) {
	    if (isFuel(worldIn, stempos, fuelpos)) {
		
		List<ItemStack> drops = this.getPotentialFuel(worldIn, fuelpos);

		ItemStack compressed = ItemStack.EMPTY;
		
		for (ItemStack fuel : drops) {
		    SquashStemTileEntity te = (SquashStemTileEntity) this.getTileEntity(worldIn, stempos);

		    // for each fuel candidate, try inserting it into the stem
		    if (te.insertFuel(worldIn, fuel)) {
			// if we id indeed consume the fuel, break the block
			worldIn.setBlockState(fuelpos, Blocks.AIR.getDefaultState());
			// this was indeed fuel, now we can try to compress the fuel
			compressed = te.tryExtractFruit(worldIn);
			// we only want to do this successfully once
			break;
		    }		    
		}

		return compressed;
	    }
	}

	return ItemStack.EMPTY;
    }
    
    private List<ItemStack> getPotentialFuel (World worldIn, BlockPos fuelpos) {
	BlockState potentialFuel = worldIn.getBlockState(fuelpos);
	LootContext.Builder builder = (new LootContext.Builder((ServerWorld) worldIn))
		.withRandom(worldIn.rand)
		.withParameter(LootParameters.POSITION, fuelpos)
		.withParameter(LootParameters.TOOL, ItemStack.EMPTY);
	
	if (potentialFuel.hasTileEntity()) {
	    builder.withParameter(LootParameters.BLOCK_ENTITY, worldIn.getTileEntity(fuelpos));
	}

	return potentialFuel.getDrops(builder);
    }

    @Nullable
    protected BlockPos findFuel(World worldIn, BlockPos stempos, Random random) {
	BlockPos fuelpos = null;

	for (int i = 0; i < 4; ++i) {
	    fuelpos = stempos.add(random.nextInt(3) - 1, 0, random.nextInt(3) - 1);

	    // maybe also check for a non-cucurbit tile entity
	    if (isFuel(worldIn, stempos, fuelpos)) {
		break;
	    }

	    fuelpos = null;
	}

	return fuelpos;
    }

    private boolean isFuel(World worldIn, BlockPos stempos, BlockPos fuelpos) {
	BlockState potentialFuel = worldIn.getBlockState(fuelpos);

	if (potentialFuel.isAir(worldIn, fuelpos)) {
	    return false;
	}
	
	List<ItemStack> drops = this.getPotentialFuel(worldIn, fuelpos);

	if (drops.isEmpty()) {
	    return false;
	}

	boolean result = false;
	SquashStemTileEntity te = (SquashStemTileEntity) this.getTileEntity(worldIn, stempos);	
	
	for (ItemStack fuel : drops) {
	    result = result || te.isFuel(worldIn, fuel);	    
	}

	return result;
    }

    protected boolean canFruit(World worldIn, @Nullable BlockPos fuelpos, BlockPos targetpos) {
	// check that we did find fuel
	return fuelpos != null && worldIn.getBlockState(targetpos).isAir(worldIn, targetpos) && fuelpos != null;
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
