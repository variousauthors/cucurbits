package variousauthors.scaffold.block.lumberjack;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import variousauthors.scaffold.CanRegisterItemBlock;
import variousauthors.scaffold.ContainerFruit;
import variousauthors.scaffold.block.BlockTileEntity;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockLumberjack extends BlockTileEntity<TileEntityLumberjack> implements CanRegisterItemBlock, ContainerFruit<TileEntityLumberjack> {
    public BlockLumberjack(String name)
    {
        super(Material.ROCK, name);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random random)
    {
        return 0;
    }

    @Override
    public Class<TileEntityLumberjack> getTileEntityClass() {
        return TileEntityLumberjack.class;
    }

    @Nullable
    @Override
    public TileEntityLumberjack createTileEntity(World world, IBlockState state) {
        return new TileEntityLumberjack();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityLumberjack tile = getTileEntity(world, pos);

        if (null == tile) return;

        IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (null == itemHandler) return;

        ItemStack stack = itemHandler.getStackInSlot(0);

        if (!stack.isEmpty()) {
            EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
            world.spawnEntity(item);
        }
        super.breakBlock(world, pos, state);
    }
}
