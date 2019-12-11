package com.arrogantgamer.cucurbits.item;

import javax.annotation.Nullable;

import com.arrogantgamer.cucurbits.ModBlocks;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class LavamelonItem extends BlockItem {
    private final Fluid containedBlock;

    public LavamelonItem(Properties builder) {
	super(ModBlocks.LAVAMELON, builder);
	this.containedBlock = Fluids.LAVA;
	this.fluidSupplier = Fluids.LAVA.delegate;	
    }

    public int getBurnTime(ItemStack itemstack) {
	return 20000;
    }

    // I'm not sure what this is...
    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundNBT nbt) {
       if (this.getClass() == LavamelonItem.class)
          return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
       else
          return super.initCapabilities(stack, nbt);
    }    
    
    private final java.util.function.Supplier<? extends Fluid> fluidSupplier;
    public Fluid getFluid() { return fluidSupplier.get(); }    
}
