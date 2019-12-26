package com.arrogantgamer.cucurbits.tileEntity;

import java.util.Optional;

import com.arrogantgamer.cucurbits.ModBlocks;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SquashStemTileEntity extends TileEntity {
    private ItemStack buffer = ItemStack.EMPTY;
    private static final int FRUIT_THRESHOLD = 9; // it requires 9 blocks to make a storage block
    private CraftingInventory craftingInventory = new CraftingInventory(new GUILessContainer(ContainerType.CRAFTING), 3, 3);

    public SquashStemTileEntity() {
	super(ModBlocks.SQUASH_STEM_TILE);
    }

    private static class GUILessContainer extends Container {

	protected GUILessContainer(ContainerType<?> type) {
	    super(type, -1); // we don't use the windowId
	}

	public void onCraftMatrixChanged(IInventory inventoryIn) {
	    // NO-OP
	}

	@Override
	public boolean canInteractWith(PlayerEntity playerIn) {
	    return false;
	}
    }

    /*
     * try to merge the given itemStack into the buffer returns true to indicate
     * success
     */
    public boolean insertFuel(World worldIn, ItemStack fuel) {
	if (!this.isFuel(worldIn, fuel)) {
	    return false;
	}

	if (this.buffer.isEmpty()) {
	    this.buffer = fuel;
	} else {
	    if (this.buffer.getItem() == fuel.getItem()) {
		this.buffer.grow(fuel.getCount());
	    }
	}

	this.markDirty();

	return true;
    }

    public boolean isFuel(World worldIn, ItemStack potentialFuel) {
	if (!this.buffer.isEmpty() && (this.buffer.getItem() != potentialFuel.getItem())) {
	    return false;
	}

	ItemStack representative = potentialFuel.copy();
	representative.setCount(1);

	this.craftingInventory.clear();

	for (int i = 0; i < 9; i++) {
	    this.craftingInventory.setInventorySlotContents(i, representative);
	}

	Optional<ICraftingRecipe> optional = worldIn.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingInventory, worldIn);

	return optional.isPresent();
    }

    /*
     * if there are at least 9 items in the buffer remove 9 items and craft storage
     * block returns the crafted storage block or EMPTY if it could not
     */
    public ItemStack tryExtractFruit(World worldIn) {
	if (worldIn.isRemote) {
	    return ItemStack.EMPTY;
	}

	if (!this.hasEnoughFuel()) {
	    return ItemStack.EMPTY;
	}

	// try craft storage block
	ItemStack fruit = this.createFruit(worldIn);

	if (fruit.isEmpty()) {
	    // something has gone wrong... is the buffer in a good state?
	    return ItemStack.EMPTY;
	}

	this.buffer.shrink(FRUIT_THRESHOLD);
	this.markDirty();

	return fruit;
    }

    private ItemStack createFruit(World worldIn) {
	ItemStack representative = this.buffer.copy();
	representative.setCount(1);

	this.craftingInventory.clear();

	for (int i = 0; i < 9; i++) {
	    this.craftingInventory.setInventorySlotContents(i, representative);
	}

	ItemStack fruit = ItemStack.EMPTY;
	Optional<ICraftingRecipe> optional = worldIn.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingInventory, worldIn);

	if (optional.isPresent()) {
	    ICraftingRecipe icraftingrecipe = optional.get();
	    fruit = icraftingrecipe.getCraftingResult(craftingInventory);
	}

	this.craftingInventory.clear();

	return fruit;
    }

    private boolean hasEnoughFuel() {
	return this.buffer.getCount() >= FRUIT_THRESHOLD;
    }

    @Override
    public void read(CompoundNBT compound) {
	this.buffer = ItemStack.read(compound);
	super.read(compound);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
	this.buffer.write(compound);

	return super.write(compound);
    }
}
