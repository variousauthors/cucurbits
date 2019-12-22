package com.arrogantgamer.cucurbits.block.squash;

import javax.annotation.Nullable;

import com.arrogantgamer.cucurbits.ModBlocks;
import com.arrogantgamer.cucurbits.tileEntity.SquashTileEntity;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class SquashBlock extends StemGrownBlock {
    public static Block.Properties properties = Block.Properties.create(Material.GOURD, MaterialColor.LIME).hardnessAndResistance(1.0F).sound(SoundType.WOOD);

    public SquashBlock(Block.Properties builder) {
	super(builder);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
	return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
	return new SquashTileEntity();
    }

    private TileEntity getTileEntity(World worldIn, BlockPos pos) {
	return worldIn.getTileEntity(pos);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
	this.spawnContainedItems(worldIn, pos);
	
	super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    protected void spawnContainedItems (World worldIn, BlockPos pos) {
	TileEntity te = this.getTileEntity(worldIn, pos);

	if (te instanceof SquashTileEntity) {
	    ItemStack item = ((SquashTileEntity) te).extractContainedItem();
	    
	    if (item != null) {
		InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), item);		
	    }
	}
    }

    public void setContainedItem(World worldIn, BlockPos pos, ItemStack item) {
	TileEntity te = this.getTileEntity(worldIn, pos);
	
	if (te instanceof SquashTileEntity) {
	    ((SquashTileEntity) te).setContainedItem(item);
	}
    }

    public StemBlock getStem() {
	return (StemBlock) ModBlocks.SQUASH_STEM;
    }

    public AttachedStemBlock getAttachedStem() {
	return (AttachedStemBlock) ModBlocks.ATTACHED_SQUASH_STEM;
    }
}
