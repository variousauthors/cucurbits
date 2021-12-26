package variousauthors.scaffold.block.corecumber;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import variousauthors.scaffold.block.BlockStemCucurbit;

import java.util.Optional;
import java.util.Random;

public class BlockStemCorecumber extends BlockStemCucurbit
{
    public BlockStemCorecumber(Block crop, String name)
    {
        super(crop, name);
    }

    protected Optional<BlockPos> findFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        /* get 16 random positions below this block */
        int checked = 0;
        BlockPos fuelPos = null;
        Random rand = worldIn.rand;
        BlockPos lowerBound = new BlockPos(stemPos.getX() - 1, 0, stemPos.getZ() - 1);

        while (fuelPos == null && checked < 16) {
            BlockPos checkPos = lowerBound.add(rand.nextInt(3), rand.nextInt(stemPos.getY() - 1), rand.nextInt(3));

            Block block = worldIn.getBlockState(checkPos).getBlock();

            /* @TODO add a configurable ore list to the config and then steal a list from Botania */
            if (block instanceof BlockOre || block.getClass().getSimpleName().equals("BlockOre")) {
                fuelPos = checkPos;
            }

            checked++;
        }

        return Optional.ofNullable(fuelPos);
    }

    protected void tryToGrowCrop(World worldIn, BlockPos pos) {
        findFuelBlockInWorld(worldIn, pos).ifPresent(fuelPos -> {
            IBlockState fuelState = worldIn.getBlockState(fuelPos);
            NonNullList<ItemStack> drops = NonNullList.create();
            fuelState.getBlock().getDrops(drops, worldIn, fuelPos, fuelState, 0);

            if (drops.isEmpty()) return;

            /* @TODO this should use "porous stone" so that we can find them later */
            worldIn.setBlockState(fuelPos, Blocks.COBBLESTONE.getDefaultState());
            worldIn.setBlockState(pos, this.crop.getDefaultState());
            TileEntity te = worldIn.getTileEntity(pos);

            if (te instanceof TileEntityCorecumber) {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

                if (itemHandler == null) return;

                for (ItemStack drop : drops) {
                    itemHandler.insertItem(0, drop, false);
                }
            }
        });
    }
}
