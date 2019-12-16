package com.arrogantgamer.cucurbits.tileEntity;

import com.arrogantgamer.cucurbits.ModBlocks;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class CorecumberTileEntity extends TileEntity implements ITickableTileEntity {

    public CorecumberTileEntity() {
	super(ModBlocks.CORECUMBER_TILE);
	System.out.println("constructor");
    }

    public void tick() {
	System.out.println("WAT");
	if (this.world.isRemote) {
	    System.out.println("TICK");
	}
    }

}
