package com.arrogantgamer.cucurbits.block;

import com.arrogantgamer.cucurbits.ModBlocks;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class CorecumberBlock extends StemGrownBlock {
    public static Block.Properties properties = Block.Properties.create(Material.GOURD, MaterialColor.LIME).hardnessAndResistance(1.0F).sound(SoundType.WOOD);
    
    public CorecumberBlock(Block.Properties builder) {
	super(builder);
    }

    public StemBlock getStem() {
	return (StemBlock) ModBlocks.CORECUMBER_STEM;
    }

    public AttachedStemBlock getAttachedStem() {
	return (AttachedStemBlock) ModBlocks.ATTACHED_CORECUMBER_STEM;
    }
}
