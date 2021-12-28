package variousauthors.scaffold.block.bakers_squash;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.lwjgl.Sys;
import scala.tools.cmd.gen.AnyValReps;
import variousauthors.scaffold.ContainerFruit;
import variousauthors.scaffold.CucurbitFruit;
import variousauthors.scaffold.block.BlockStemCucurbit;
import variousauthors.scaffold.block.corecumber.TileEntityCorecumber;

import java.util.Iterator;
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
            } else {
                /** @ASK should this really be mutating the parameter? Vanilla does this. */
                pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));

                if (!canGrowCropAtPos(worldIn, pos)) return;

                tryToGrowCrop(worldIn, pos);
            }
        }

        net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
    }

    private NonNullList<ItemStack> nicelyHarvestCompanionCrop(World worldIn, BlockPos pos) {
        return NonNullList.create();
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

    /* get the drops from a block in the world with getDrops OR get the contents ofa ContainerFruit */
    private NonNullList<ItemStack> getDropsFromFuel(World worldIn, BlockPos fuelPos, IBlockState fuelState, int fortune) {
        Block fuelBlock = worldIn.getBlockState(fuelPos).getBlock();
        NonNullList<ItemStack> drops = NonNullList.create();

        // TODO remove this, this is just here do that I can use chests for debugging
        TileEntity te = worldIn.getTileEntity(fuelPos);
        IItemHandler itemHandler = te == null ? null : te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (fuelBlock instanceof ContainerFruit) {
            ((ContainerFruit<?>) fuelBlock).getContents(drops, worldIn, fuelPos);
        } else if (null != itemHandler) {
            // TODO remove this, this is just here do that I can use chests for debugging

            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);

                if (!stack.isEmpty()) {
                    drops.add(stack);
                }
            }
        } else {
            fuelState.getBlock().getDrops(drops, worldIn, fuelPos, fuelState, fortune);
        }

        return drops;
    }

    protected void tryToGrowCrop(World worldIn, BlockPos pos) {
        findInitialFuelBlockInWorld(worldIn, pos).ifPresent(fuelPos -> {
            IBlockState fuelState = worldIn.getBlockState(fuelPos);
            NonNullList<ItemStack> drops = getDropsFromFuel(worldIn, fuelPos, fuelState, 0);

            if (drops.isEmpty()) return;

            if (isCucurbitFruit(worldIn, fuelPos)) {
                worldIn.removeTileEntity(fuelPos);
            }

            worldIn.setBlockState(fuelPos, Blocks.AIR.getDefaultState());
            worldIn.setBlockState(pos, this.crop.getDefaultState());
            TileEntity te = worldIn.getTileEntity(pos);

            if (te instanceof TileEntityBakersSquash) {
                IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

                if (itemHandler == null) return;

                for (ItemStack drop : drops) {
                    ItemStack output = FurnaceRecipes.instance().getSmeltingResult(drop).copy();
                    output.setCount(drop.getCount());

                    if (!output.isEmpty()) {
                        itemHandler.insertItem(0, output, false);
                    }
                }
            }
        });
    }

    private Optional<BlockPos> findInitialFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        // check the companion crop positions
        BlockPos from = stemPos.west().north();
        BlockPos to = stemPos.east().south();

        Iterator<BlockPos> neighbourhood = BlockPos.getAllInBox(from, to).iterator();
        BlockPos fuelPos = null;

        while (neighbourhood.hasNext() && fuelPos == null) {
            BlockPos current = neighbourhood.next();

            // skip the middle
            if (current.equals(stemPos)) {
                continue;
            }

            IBlockState blockState = worldIn.getBlockState(current);

            if (isHarvestable(worldIn, current)) {
                // to do
            } else if (isCucurbitFruit(worldIn, current)) {
                NonNullList<ItemStack> drops = getDropsFromFuel(worldIn, current, blockState, 0);

                for (ItemStack drop : drops) {
                    if (!FurnaceRecipes.instance().getSmeltingResult(drop).isEmpty()) {
                        fuelPos = current;
                    }
                }
            }
        }

        return Optional.ofNullable(fuelPos);
    }

    protected boolean isHarvestable(World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();

        // check for getSeed and getCrop methods
        // check for PlantType "crop"
        // check for IGrowable
        // check for canGrow "false" since we want "harvestable"

        return false;
    }

    protected boolean isCucurbitFruit(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock() instanceof CucurbitFruit
                || worldIn.getBlockState(pos).getBlock() instanceof BlockChest;
    }
}
