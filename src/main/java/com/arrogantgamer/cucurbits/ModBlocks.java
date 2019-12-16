package com.arrogantgamer.cucurbits;

import com.arrogantgamer.cucurbits.tileEntity.CorecumberTileEntity;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(Cucurbits.MODID)
public final class ModBlocks {

  public static final Block LAVAMELON = null; 
  public static final Block LAVAMELON_STEM = null; 
  public static final Block ATTACHED_LAVAMELON_STEM = null; 
  
  public static final Block CORECUMBER = null; 
  public static final Block CORECUMBER_STEM = null; 
  public static final Block ATTACHED_CORECUMBER_STEM = null;   

  @ObjectHolder("cucurbits:corecumber")
  public static final TileEntityType<CorecumberTileEntity> CORECUMBER_TILE = null;

}