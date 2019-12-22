package com.arrogantgamer.cucurbits.block.squash;

import net.minecraft.block.AttachedStemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.StemGrownBlock;
import net.minecraft.block.material.Material;

public class AttachedSquashStemBlock extends AttachedStemBlock {
    public static Block.Properties properties = Block.Properties.create(Material.PLANTS).doesNotBlockMovement().hardnessAndResistance(0.0F).sound(SoundType.WOOD);
    
    public AttachedSquashStemBlock(StemGrownBlock p_i48449_1_, Properties properties) {
	super(p_i48449_1_, properties);
    }
}
