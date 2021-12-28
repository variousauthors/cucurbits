package variousauthors.scaffold.block.bakers_squash;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import variousauthors.scaffold.ContainerFruit;
import variousauthors.scaffold.block.BlockStemCucurbit;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

public class BlockStemBakersSquash extends BlockStemCucurbit
{
    public BlockStemBakersSquash(Block crop, String name)
    {
        super(crop, name);
    }

    protected void growFruit(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (cropIsAlreadyGrown(worldIn, pos)) {
            // if crop is already grown, do the crop grown version
            tryToFeedCrop(worldIn, pos);
        } else {
            /* TODO should this really be mutating the parameter? Vanilla does this. */
            pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (!canGrowCropAtPos(worldIn, pos)) return;

            tryToGrowCrop(worldIn, pos);
        }
    }

    private NonNullList<ItemStack> nicelyHarvestCompanionCrop(World worldIn, BlockPos pos) {
        return NonNullList.create();
    }

    private void tryToFeedCrop(World worldIn, BlockPos pos) {
        findFuelBlockInWorld(worldIn, pos).ifPresent(fuelPos -> {
            findCropMatchingStem(worldIn, pos).ifPresent(crop -> {
                if (!(crop instanceof ContainerFruit)) return;

                ContainerFruit fruit = (ContainerFruit) crop;

                if (fruit.isFull(worldIn, pos)) return;

                IBlockState fuelState = worldIn.getBlockState(fuelPos);
                NonNullList<ItemStack> drops = extractDropsFromFuel(worldIn, fuelPos, fuelState, 0, FUEL_EXTRACTION_RATE);

                /* maybe we should throw here, since we've already checked in a previous method that
                 * the drops were not empty... maybe just during dev? */
                if (drops.isEmpty()) return;

                /* we are not doing anything with the remainder right now
                 * but maybe later we can... */
                cookAndInsert(worldIn, pos, drops);
            });
        });
    }

    protected Optional<BlockPos> findFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        /* remember to check if the fruit is full or if the insert would fill the fruit */

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
            } else if (isContainerFruit(worldIn, current)) {
                NonNullList<ItemStack> drops = getDropsFromFuel(worldIn, current, blockState, 0);

                for (ItemStack drop : drops) {
                    if (!FurnaceRecipes.instance().getSmeltingResult(drop).isEmpty()) {
                        fuelPos = current;
                    }
                }
            }
        }

        // TODO if we don't find any input in the companion plant positions
        // check a few random position in the ground
        // if the drop matches the internal inventory
        // then replace that block with... ash? sand? dirt? and add the cooked outout to the fruit

        return Optional.ofNullable(fuelPos);
    }

    /** this breaks the block, gets the drops, or asks the fruit to extract its contents */
    private NonNullList<ItemStack> extractDropsFromFuel(World worldIn, BlockPos fuelPos, IBlockState fuelState, int fortune, int amount) {
        Block fuelBlock = worldIn.getBlockState(fuelPos).getBlock();
        NonNullList<ItemStack> drops = NonNullList.create();

        // TODO remove this, this is just here do that I can use chests for debugging
        TileEntity te = worldIn.getTileEntity(fuelPos);
        IItemHandler itemHandler = te == null ? null : te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (fuelBlock instanceof ContainerFruit) {
            ((ContainerFruit<?>) fuelBlock).extractContents(drops, worldIn, fuelPos, amount);
        } else if (null != itemHandler) {
            // TODO remove this, this is just here do that I can use chests for debugging

            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.extractItem(i, amount, false);

                if (!stack.isEmpty()) {
                    drops.add(stack);
                }
            }
        } else {
            fuelState.getBlock().getDrops(drops, worldIn, fuelPos, fuelState, fortune);
        }

        return drops;
    }

    /* get the drops from a block in the world with getDrops OR get the contents of ContainerFruit */
    private NonNullList<ItemStack> getDropsFromFuel(World worldIn, BlockPos fuelPos, IBlockState fuelState, int fortune) {
        Block fuelBlock = worldIn.getBlockState(fuelPos).getBlock();
        NonNullList<ItemStack> drops = NonNullList.create();

        // TODO remove this, this is just here do that I can use chests for debugging
        TileEntity te = worldIn.getTileEntity(fuelPos);
        IItemHandler itemHandler = te == null ? null : te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.NORTH);

        if (fuelBlock instanceof ContainerFruit) {
            ((ContainerFruit) fuelBlock).getContents(drops, worldIn, fuelPos);
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

    final private int FUEL_EXTRACTION_RATE = 8;

    protected void tryToGrowCrop(World worldIn, BlockPos pos) {
        /* don't need to worry about the fruit being full, since the fruit does
        * not exist yet */
        findInitialFuelBlockInWorld(worldIn, pos).ifPresent(fuelPos -> {
            IBlockState fuelState = worldIn.getBlockState(fuelPos);
            NonNullList<ItemStack> drops = extractDropsFromFuel(worldIn, fuelPos, fuelState, 0, FUEL_EXTRACTION_RATE);

            /* maybe we should throw here, since we've already checked in a previous method that
            * the drops were not empty... maybe just during dev? */
            if (drops.isEmpty()) return;

            if (!worldIn.setBlockState(pos, this.crop.getDefaultState())) return;

            /* we are not doing anything with the remainder right now
            * but maybe later we can... */
            cookAndInsert(worldIn, pos, drops);
        });
    }

    /* this is here for now, the logic is that the stem does the cooking, and then
    * feeds the cooked drops into the fruit, which is just a container */
    /* cook and insert will do bounds checking on the fruit, and fail to insert if the fruit is full */
    private void cookAndInsert(World worldIn, BlockPos pos, NonNullList<ItemStack> drops) {
        NonNullList<ItemStack> cooked = NonNullList.create();

        for (ItemStack drop : drops) {
            ItemStack output = FurnaceRecipes.instance().getSmeltingResult(drop).copy();

            if (!output.isEmpty()) {
                output.setCount(drop.getCount());
                cooked.add(output);
            }
        }

        if (cooked.isEmpty()) return;

        Block block = worldIn.getBlockState(pos).getBlock();

        if (isContainerFruit(block)) {
            ContainerFruit fruit = (ContainerFruit) block;

            if (!fruit.isFull(worldIn, pos)) {
                fruit.insertContents(cooked, worldIn, pos);
            }
        }
    }

    /** searches the companion crop positions for valid fuel sources */
    private Optional<BlockPos> findInitialFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        /* we don't need to worry about the fruit being "full" because there is no fruit yet */

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
            } else if (isContainerFruit(worldIn, current)) {
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

        // check for getSeed and getCrop methods
        // check for PlantType "crop"
        // check for IGrowable
        // check for canGrow "false" since we want "harvestable"

        return false;
    }

    protected boolean isContainerFruit(Block block) {
        return block instanceof ContainerFruit || block instanceof BlockChest;
    }

    protected boolean isContainerFruit(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock() instanceof ContainerFruit
                || worldIn.getBlockState(pos).getBlock() instanceof BlockChest;
    }
}
