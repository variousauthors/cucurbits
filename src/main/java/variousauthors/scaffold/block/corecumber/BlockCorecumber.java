package variousauthors.scaffold.block.corecumber;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import variousauthors.scaffold.CanRegisterItemBlock;
import variousauthors.scaffold.block.BlockBase;
import variousauthors.scaffold.block.BlockTileEntity;
import variousauthors.scaffold.block.pedestal.TileEntityPedestal;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockCorecumber extends BlockTileEntity<TileEntityCorecumber> implements CanRegisterItemBlock {
    public BlockCorecumber()
    {
        super(Material.ROCK, "corecumber");
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public Class<TileEntityCorecumber> getTileEntityClass() {
        return TileEntityCorecumber.class;
    }

    @Nullable
    @Override
    public TileEntityCorecumber createTileEntity(World world, IBlockState state) {
        return new TileEntityCorecumber();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityCorecumber tile = getTileEntity(world, pos);
        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
        ItemStack stack = itemHandler.getStackInSlot(0);
        if (!stack.isEmpty()) {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.spawnEntity(item);
        }
        super.breakBlock(world, pos, state);
    }
}