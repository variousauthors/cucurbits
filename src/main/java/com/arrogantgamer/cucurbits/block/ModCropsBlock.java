package com.arrogantgamer.cucurbits.block;

import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ModCropsBlock extends CropsBlock {

    protected ModCropsBlock(Properties builder) {
	super(builder);
    }

    protected static float getGrowthChance(Block blockIn, IBlockReader worldIn, BlockPos pos) {
	return CropsBlock.getGrowthChance(blockIn, worldIn, pos);
    }
}
