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
import javax.annotation.Nullable;
import java.util.Random;

public class BlockStemCorecumber extends BlockStemCucurbit
{
    public BlockStemCorecumber(Block crop, String name)
    {
        super(crop, name);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (!checkStemGrowthConditions(worldIn, pos)) return;

        float f = getGrowthChance(this, worldIn, pos);
        boolean def = rand.nextInt((int)(25.0F / f) + 1) == 0;

        if(!net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, def)) return;

        int i = ((Integer)state.getValue(AGE)).intValue();

        if (i < 7)
        {
            IBlockState newState = state.withProperty(AGE, Integer.valueOf(i + 1));
            worldIn.setBlockState(pos, newState, 2);
        }
        else
        {
            if (cropIsAlreadyGrown(worldIn, pos)) return;

            /** @ASK should this really be mutating the parameter? */
            pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (!canGrowCropAtPos(worldIn, pos)) return;

            tryToGrowCrop(worldIn, pos);
        }

        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    }

    @Nullable
    protected BlockPos findFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        /** get 16 random positions below this block */
        int checked = 0;
        BlockPos fuelPos = null;
        Random rand = worldIn.rand;
        BlockPos lowerBound = new BlockPos(stemPos.getX() - 1, 0, stemPos.getZ() - 1);

        while (fuelPos == null && checked < 16) {
            BlockPos checkPos = lowerBound.add(rand.nextInt(3), rand.nextInt(stemPos.getY() - 1), rand.nextInt(3));

            Block block = worldIn.getBlockState(checkPos).getBlock();

            /** @TODO add a configurable ore list to the config and then steal a list from Botania */
            if (block instanceof BlockOre || block.getClass().getSimpleName().equals("BlockOre")) {
                fuelPos = checkPos;
            }

            checked++;
        }

        return fuelPos;
    }

    protected boolean checkStemGrowthConditions(World worldIn, BlockPos pos) {
        return worldIn.getLightFromNeighbors(pos.up()) >= 9;
    }

    protected boolean cropIsAlreadyGrown(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop)
            {
                return true;
            }
        }

        return false;
    }

    public boolean canGrowCropAtPos(World worldIn, BlockPos soilPos) {
        IBlockState soil = worldIn.getBlockState(soilPos.down());
        Block block = soil.getBlock();

        return worldIn.isAirBlock(soilPos)
                && (block.canSustainPlant(soil, worldIn, soilPos.down(), EnumFacing.UP, this)
                || block == Blocks.DIRT
                || block == Blocks.GRASS);
    }

    protected void tryToGrowCrop(World worldIn, BlockPos pos) {
        BlockPos fuelPos = findFuelBlockInWorld(worldIn, pos);

        if (fuelPos == null) return;

        IBlockState fuelState = worldIn.getBlockState(fuelPos);
        NonNullList<ItemStack> drops = NonNullList.create();
        fuelState.getBlock().getDrops(drops, worldIn, fuelPos, fuelState, 0);

        if (drops.isEmpty()) return;

        /** @TODO this should use "porous stone" so that we can find them later */
        worldIn.setBlockState(fuelPos, Blocks.COBBLESTONE.getDefaultState());
        worldIn.setBlockState(pos, this.crop.getDefaultState());
        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TileEntityCorecumber) {
            IItemHandler itemHandler = ((TileEntityCorecumber) te).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);
            for (ItemStack drop : drops) {
                itemHandler.insertItem(0, drop, false);
            }
        }
    }
}
