package variousauthors.scaffold.block.bakers_squash;

import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
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
import scala.tools.cmd.gen.AnyValReps;
import variousauthors.scaffold.block.BlockStemCucurbit;
import variousauthors.scaffold.block.corecumber.TileEntityCorecumber;

import java.util.Optional;
import java.util.Random;

public class BlockStemBakersSquash extends BlockStemCucurbit
{
    public BlockStemBakersSquash(Block crop, String name)
    {
        super(crop, name);
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
            IBlockState newState = state.withProperty(AGE, Integer.valueOf(i + 1));
            worldIn.setBlockState(pos, newState, 2);
        }
        else
        {

            if (cropIsAlreadyGrown(worldIn, pos)) {
                // if crop is already grown, do the crop grown version
                tryToFeedCrop(worldIn, pos);

                return;
            } else {
                /** @ASK should this really be mutating the parameter? Vanilla does this. */
                pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));

                if (!canGrowCropAtPos(worldIn, pos)) return;

                tryToGrowCrop(worldIn, pos);
            }
        }

        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    }

    private boolean hasFurnaceRecipe(ItemStack item) {
        return false;
    }

    private NonNullList<ItemStack> nicelyHarvestCompanionCrop(World worldIn, BlockPos pos) {
        return NonNullList.create();
    }

    private Optional<BlockPos> findInitialFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        // check the companion crop positions

        // if it's a crop, check its drop
        // and if there is a furnace recipe
        // then so then harvest the crop nicely

        // if it is a Cucurbit fruit using fruitBlock.isCucurbitsFruit
        // and if the first item in its inventory has a furnace recipe
        // then extract one item and add its cooked output to the fruit

        return Optional.ofNullable(null);
    }

    private void tryToFeedCrop(World worldIn, BlockPos pos) {
        findFuelBlockInWorld(worldIn, pos).ifPresent(fuelPos -> {
            // TODO
        });
    }

    protected Optional<BlockPos> findFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        // check the companion crop positions

        // if it's a crop, check its drop
        // and if the furnace output is the same kind of item as the fruit has
        // then so then harvest the crop nicely

        // if it is a Cucurbit fruit using fruitBlock.isCucurbitsFruit
        // and if the furnace output is the same kind of item as the fruit has
        // then extract one item and add its cooked output to the fruit

        // if we don't find any input in the companion plant positions
        // check a few random position in the ground
        // if the drop matches the internal inventory
        // then replace that block with... ash? sand? dirt? and add the cooked outout to the fruit

        return Optional.ofNullable(null);
    }

    protected void tryToGrowCrop(World worldIn, BlockPos pos) {
        findInitialFuelBlockInWorld(worldIn, pos).ifPresent(fuelPos -> {
            // TODO
        });
    }
}
