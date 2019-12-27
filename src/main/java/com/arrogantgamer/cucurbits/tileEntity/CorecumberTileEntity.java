package com.arrogantgamer.cucurbits.tileEntity;

import com.arrogantgamer.cucurbits.ModBlocks;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class CorecumberTileEntity extends TileEntity {
    private ItemStack containedItem = ItemStack.EMPTY;

    public CorecumberTileEntity() {
	super(ModBlocks.CORECUMBER_TILE);
    }

    public void setContainedItem(ItemStack containedItem) {
        this.containedItem = containedItem;
	this.markDirty();
    }
    
    public ItemStack getContainedItem () {
	return this.containedItem.copy();
    }
        
    @Override
    public void read(CompoundNBT compound) {
	this.containedItem = ItemStack.read(compound.getCompound("inv"));
	
	super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
	CompoundNBT inv = this.containedItem.serializeNBT();
	compound.put("inv", inv);

	return super.write(compound);
    }
}
