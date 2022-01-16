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
    protected void growFruit(World worldIn, BlockPos stemPos, IBlockState state, Random rand) {
        if (cropIsAlreadyGrown(worldIn, stemPos)) return;

        /** @ASK should this really be mutating the parameter? Vanilla does this. */
        BlockPos targetPos = stemPos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));

        if (!canGrowCropAtPos(worldIn, targetPos)) return;

        tryToGrowCrop(worldIn, stemPos, targetPos);
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

    abstract protected Optional<BlockPos> findFuelBlockInWorld(World worldIn, BlockPos stemPos, BlockPos fruitPos);

    protected boolean checkStemGrowthConditions(World worldIn, BlockPos pos) {
        return worldIn.getLightFromNeighbors(pos.up()) >= 9;
    }

    /* TODO rename this to `fruitIsAlreadyGrown` */
    protected boolean cropIsAlreadyGrown(World worldIn, BlockPos pos) {
        return findCropMatchingStem(worldIn, pos)
                .map(match -> true)
                .orElse(false);
    }

    protected Optional<BlockPos> findCropMatchingStem(World worldIn, BlockPos stemPos) {
        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            BlockPos foundPos = stemPos.offset(enumfacing);
            Block crop = worldIn.getBlockState(foundPos).getBlock();

            if (crop == this.crop)
            {
                return Optional.ofNullable(foundPos);
            }
        }

        return Optional.empty();
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
    protected void tryToGrowCrop(World worldIn, BlockPos stemPos, BlockPos targetPos) {
        /* TODO rename this to `tryToGrowFruit` */
        findFuelBlockInWorld(worldIn, stemPos, targetPos).ifPresent(fuelPos -> {
            worldIn.setBlockState(fuelPos, Blocks.AIR.getDefaultState());
            worldIn.setBlockState(targetPos, this.crop.getDefaultState());
        });
    }
}
