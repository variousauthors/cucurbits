package com.arrogantgamer.cucurbits.block.lavamelon;

import com.arrogantgamer.cucurbits.ModBlocks;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemBlock;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class LavamelonBlock extends StemGrownBlock {
    public static Block.Properties properties = Block.Properties.create(Material.GOURD, MaterialColor.LIME).hardnessAndResistance(1.0F).sound(SoundType.WOOD);
    
    public LavamelonBlock(Block.Properties builder) {
	super(builder);
    }

    public StemBlock getStem() {
	return (StemBlock) ModBlocks.LAVAMELON_STEM;
    }

    public AttachedStemBlock getAttachedStem() {
	return (AttachedStemBlock) ModBlocks.ATTACHED_LAVAMELON_STEM;
    }
}
