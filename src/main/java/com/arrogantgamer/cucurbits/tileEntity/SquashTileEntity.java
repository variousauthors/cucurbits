package com.arrogantgamer.cucurbits.tileEntity;

import com.arrogantgamer.cucurbits.ModBlocks;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class SquashTileEntity extends TileEntity {
    private ItemStack containedItem = ItemStack.EMPTY;

    public SquashTileEntity() {
	super(ModBlocks.SQUASH_TILE);
    }

    /* TODO this is a questionable name... maybe like "insertItem" is better? */
    public void setContainedItem(ItemStack incoming) {
	// this needs to add the incoming stack to the buffer if they match
        this.containedItem = incoming;
	this.markDirty();
    }
    
    public ItemStack extractContainedItem() {
	// this needs to set the buffer to null if buffer length - 9 === 0
	ItemStack item = this.containedItem;
	this.containedItem = ItemStack.EMPTY;
	this.markDirty();	

        return item;
    }    
    
    @Override
    public void read(CompoundNBT compound) {
	this.containedItem = ItemStack.read(compound);
	super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
	this.containedItem.write(compound);

	return super.write(compound);
    }
}
