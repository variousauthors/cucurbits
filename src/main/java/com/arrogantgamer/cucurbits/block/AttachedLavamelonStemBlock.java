package com.arrogantgamer.cucurbits.block;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class AttachedLavamelonStemBlock extends AttachedStemBlock {
    public static Block.Properties properties = Block.Properties.create(Material.EARTH, MaterialColor.DIRT).hardnessAndResistance(0.5F).sound(SoundType.GROUND).tickRandomly();

    public AttachedLavamelonStemBlock(StemGrownBlock p_i48449_1_, Properties properties) {
	super(p_i48449_1_, properties);
    }

}
