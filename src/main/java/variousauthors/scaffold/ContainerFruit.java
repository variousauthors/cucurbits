package variousauthors.scaffold;

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
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack stack = itemHandler.getStackInSlot(0);

        if (!stack.isEmpty()) {
            drops.add((stack));
        }
    }
}
