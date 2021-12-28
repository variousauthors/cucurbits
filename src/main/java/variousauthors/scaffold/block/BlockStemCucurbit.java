package variousauthors.scaffold.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.Random;

abstract public class BlockStemCucurbit extends BlockStem {

    public BlockStemCucurbit(Block crop, String name)
    {
        super(crop);

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    /** if all the conditions are juuuuuust right, the stem will do its magic */
    protected void growFruit(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (cropIsAlreadyGrown(worldIn, pos)) return;

        /** @ASK should this really be mutating the parameter? Vanilla does this. */
        pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));

        if (!canGrowCropAtPos(worldIn, pos)) return;

        tryToGrowCrop(worldIn, pos);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        this.checkAndDropBlock(worldIn, pos, state);

        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (!checkStemGrowthConditions(worldIn, pos)) return;

        float f = getGrowthChance(this, worldIn, pos);
        boolean def = rand.nextInt((int)(25.0F / f) + 1) == 0;

        if(!net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, def)) return;

        int i = ((Integer)state.getValue(AGE)).intValue();

        if (i < 7)
        {
            growStem(worldIn, pos, state, Integer.valueOf(i + 1));
        }
        else
        {
            growFruit(worldIn, pos, state, rand);
        }

        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    }

    abstract protected Optional<BlockPos> findFuelBlockInWorld(World worldIn, BlockPos stemPos);

    protected boolean checkStemGrowthConditions(World worldIn, BlockPos pos) {
        return worldIn.getLightFromNeighbors(pos.up()) >= 9;
    }

    /* TODO rename this to `fruiIsAlreadyGrown` */
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

    /* TODO rename this to `canGrowFruitAtPos` */
    protected boolean canGrowCropAtPos(World worldIn, BlockPos soilPos) {
        IBlockState soil = worldIn.getBlockState(soilPos.down());
        Block block = soil.getBlock();

        return worldIn.isAirBlock(soilPos)
                && (block.canSustainPlant(soil, worldIn, soilPos.down(), EnumFacing.UP, this)
                || block == Blocks.DIRT
                || block == Blocks.GRASS);
    }

    /** default implementation just puts the crop in the world */
    protected void tryToGrowCrop(World worldIn, BlockPos pos) {
        /* TODO rename this to `tryToGrowFruit` */
        findFuelBlockInWorld(worldIn, pos).ifPresent(fuelPos -> {
            worldIn.setBlockState(fuelPos, Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos, this.crop.getDefaultState());
        });
    }
}
