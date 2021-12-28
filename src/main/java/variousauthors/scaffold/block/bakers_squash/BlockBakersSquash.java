package variousauthors.scaffold.block.bakers_squash;

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

public class BlockBakersSquash extends BlockTileEntity<TileEntityBakersSquash> implements CanRegisterItemBlock, ContainerFruit<TileEntityBakersSquash> {
    public BlockBakersSquash(String name)
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
    public Class<TileEntityBakersSquash> getTileEntityClass() {
        return TileEntityBakersSquash.class;
    }

    @Nullable
    @Override
    public TileEntityBakersSquash createTileEntity(World world, IBlockState state) {
        return new TileEntityBakersSquash();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityBakersSquash tile = getTileEntity(world, pos);

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
