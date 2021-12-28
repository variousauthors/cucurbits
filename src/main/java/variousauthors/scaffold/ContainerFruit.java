package variousauthors.scaffold;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public interface ContainerFruit<TE extends TileEntity> {
    TE getTileEntity(IBlockAccess world, BlockPos pos);

    default void getContents(NonNullList<ItemStack> drops, World worldIn, BlockPos pos) {
        TE tile = getTileEntity(worldIn, pos);

        if (tile == null) return;

        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (itemHandler == null) return;

        ItemStack stack = itemHandler.getStackInSlot(0);

        if (!stack.isEmpty()) {
            drops.add((stack));
        }
    }

    default NonNullList<ItemStack> insertContents(NonNullList<ItemStack> drops, World worldIn, BlockPos pos) {
        TE tile = getTileEntity(worldIn, pos);

        if (tile == null) return drops;

        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (itemHandler == null) return drops;

        NonNullList<ItemStack> remainder = NonNullList.create();

        for (ItemStack drop : drops) {
            ItemStack result = itemHandler.insertItem(0, drop, false);

            if (!result.isEmpty()) {
                remainder.add(result);
            }
        }

        return remainder;
    }

    default void extractContents(NonNullList<ItemStack> drops, World worldIn, BlockPos pos, int amount) {
        TE tile = getTileEntity(worldIn, pos);

        if (tile == null) return;

        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (itemHandler == null) return;

        ItemStack stack = itemHandler.extractItem(0, amount, false);

        if (!stack.isEmpty()) {
            drops.add((stack));
        }

        ItemStack remainder = itemHandler.getStackInSlot(0);

        if (remainder.isEmpty()) {
            // remove the fruit
            worldIn.removeTileEntity(pos);
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }
}
