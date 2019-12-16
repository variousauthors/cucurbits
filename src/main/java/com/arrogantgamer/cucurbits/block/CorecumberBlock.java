package com.arrogantgamer.cucurbits.block;

import javax.annotation.Nullable;

import com.arrogantgamer.cucurbits.ModBlocks;
import com.arrogantgamer.cucurbits.tileEntity.CorecumberTileEntity;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class CorecumberBlock extends StemGrownBlock {
    public static Block.Properties properties = Block.Properties.create(Material.GOURD, MaterialColor.LIME).hardnessAndResistance(1.0F).sound(SoundType.WOOD);
    
    public CorecumberBlock(Block.Properties builder) {
	super(builder);
    }
    
    @Override
    public boolean hasTileEntity (BlockState state) {
	return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity (BlockState state, IBlockReader world) {
	return new CorecumberTileEntity();
    }

    public StemBlock getStem() {
	return (StemBlock) ModBlocks.CORECUMBER_STEM;
    }

    public AttachedStemBlock getAttachedStem() {
	return (AttachedStemBlock) ModBlocks.ATTACHED_CORECUMBER_STEM;
    }
}
