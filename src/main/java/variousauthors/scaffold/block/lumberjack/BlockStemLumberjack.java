package variousauthors.scaffold.block.lumberjack;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import variousauthors.scaffold.ContainerFruit;
import variousauthors.scaffold.block.BlockStemCucurbit;
import variousauthors.scaffold.block.ModBlocks;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

public class BlockStemLumberjack extends BlockStemCucurbit
{
    public BlockStemLumberjack(Block crop, String name)
    {
        super(crop, name);
    }

    @Override
    public void growStem(World worldIn, BlockPos pos, IBlockState state, int amount) {
        int i = ((Integer)state.getValue(AGE)).intValue() + amount;
        int age = Integer.valueOf(Math.min(7, i));

        if (age == 7) {
            // turn off random ticks now that the stem is grown
            setTickRandomly(false);
        }

        worldIn.setBlockState(pos, state.withProperty(AGE, age), 2);
    }

    @Override
    protected void growFruit(World worldIn, BlockPos stemPos, IBlockState state, Random rand) {
        if (cropIsAlreadyGrown(worldIn, stemPos)) {
            // if crop is already grown, do the crop grown version
            tryToFeedCrop(worldIn, stemPos);
        } else {
            /* TODO should this really be mutating the parameter? Vanilla does this. */
            BlockPos targetPos = stemPos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (!canGrowCropAtPos(worldIn, targetPos)) return;

            tryToGrowCrop(worldIn, stemPos, targetPos);
        }
    }

    private NonNullList<ItemStack> nicelyHarvestCompanionCrop(World worldIn, BlockPos pos) {
        return NonNullList.create();
    }

    private void tryToFeedCrop(World worldIn, BlockPos stemPos) {
        findCropMatchingStem(worldIn, stemPos).ifPresent(cropPos -> {
            Block crop = worldIn.getBlockState(cropPos).getBlock();

            if (!(crop instanceof ContainerFruit)) return;

            ContainerFruit fruit = (ContainerFruit) crop;

            if (fruit.isFull(worldIn, cropPos)) return;

            findFuelBlockInWorld(worldIn, stemPos, cropPos).ifPresent(fuelPos -> {
                IBlockState fuelState = worldIn.getBlockState(fuelPos);
                NonNullList<ItemStack> drops = extractDropsFromFuel(worldIn, fuelPos, fuelState, 0, FUEL_EXTRACTION_RATE);

                /* maybe we should throw here, since we've already checked in a previous method that
                 * the drops were not empty... maybe just during dev? */
                if (drops.isEmpty()) return;

                /* we are not doing anything with the remainder right now
                 * but maybe later we can... */
                insert(worldIn, cropPos, drops);
            });
        });
    }

    /** the lumberjack does not need to break the saplings it is planted with,
     * it just gets 1 log of the type the sapling provides */
    private NonNullList<ItemStack> extractDropsFromFuel(World worldIn, BlockPos fuelPos, IBlockState fuelState, int fortune, int amount) {
        Block fuelBlock = worldIn.getBlockState(fuelPos).getBlock();
        NonNullList<ItemStack> drops = NonNullList.create();

        if (fuelBlock instanceof BlockSapling) {
            IBlockState saplingState = worldIn.getBlockState(fuelPos);

            switch ((BlockPlanks.EnumType)saplingState.getValue(BlockSapling.TYPE)) {
                case SPRUCE:
                case ACACIA:
                case DARK_OAK:
                case OAK:
                case BIRCH:
                case JUNGLE:
                default:
                    IBlockState logState = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
                    drops.add(new ItemStack(logState.getBlock()));
                    break;
            }
        }

        return drops;
    }

    /* returns the drops from an eligible block in the world */
    private NonNullList<ItemStack> getDropsFromFuel(World worldIn, BlockPos fuelPos, IBlockState fuelState, int fortune) {
        Block fuelBlock = worldIn.getBlockState(fuelPos).getBlock();
        NonNullList<ItemStack> drops = NonNullList.create();

        if (fuelBlock instanceof BlockSapling) {
            IBlockState saplingState = worldIn.getBlockState(fuelPos);

            switch ((BlockPlanks.EnumType)saplingState.getValue(BlockSapling.TYPE)) {
                case SPRUCE:
                case ACACIA:
                case DARK_OAK:
                case OAK:
                case BIRCH:
                case JUNGLE:
                default:
                    IBlockState logState = Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.ACACIA);
                    drops.add(new ItemStack(logState.getBlock()));
                    break;
            }
        }

        return drops;
    }

    final private int FUEL_EXTRACTION_RATE = 8;

    @Override
    protected void tryToGrowCrop(World worldIn, BlockPos stemPos, BlockPos targetPos) {
        findFuelBlockInWorld(worldIn, stemPos, targetPos).ifPresent(fuelPos -> {
            IBlockState fuelState = worldIn.getBlockState(fuelPos);
            NonNullList<ItemStack> drops = extractDropsFromFuel(worldIn, fuelPos, fuelState, 0, FUEL_EXTRACTION_RATE);

            /* maybe we should throw here, since we've already checked in a previous method that
            * the drops were not empty... maybe just during dev? */
            if (drops.isEmpty()) return;

            if (!worldIn.setBlockState(targetPos, this.crop.getDefaultState())) return;

            /* we are not doing anything with the remainder right now
            * but maybe later we can... */
            insert(worldIn, targetPos, drops);
        });
    }

    private void insert(World worldIn, BlockPos pos, NonNullList<ItemStack> drops) {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (isContainerFruit(block)) {
            ContainerFruit fruit = (ContainerFruit) block;

            if (!fruit.isFull(worldIn, pos)) {
                fruit.insertContents(drops, worldIn, pos);
            }
        }
    }

    @SubscribeEvent
    public static void onSaplingGrowTree(SaplingGrowTreeEvent event) {
        World worldIn = event.getWorld();

        if (!worldIn.isRemote) {
            BlockPos eventPos = event.getPos();
            BlockPos from = eventPos.west().north();
            BlockPos to = eventPos.east().south();
            Iterator<BlockPos> neighbourhood = BlockPos.getAllInBox(from, to).iterator();

            BlockPos stemPos = null;

            while (neighbourhood.hasNext() && stemPos == null) {
                BlockPos current = neighbourhood.next();
                Block block = worldIn.getBlockState(current).getBlock();

                if (block == ModBlocks.stemLumberjack) {
                    // stop that sapling!
                    event.setResult(Event.Result.DENY);
                    worldIn.scheduleUpdate(current, block, 0);
                }
            }
        }
    }

    @Override
    protected Optional<BlockPos> findFuelBlockInWorld(World worldIn, BlockPos stemPos, BlockPos fruitPos) {
        BlockPos from = stemPos.west().north();
        BlockPos to = stemPos.east().south();

        Iterator<BlockPos> neighbourhood = BlockPos.getAllInBox(from, to).iterator();
        BlockPos fuelPos = null;

        while (neighbourhood.hasNext() && fuelPos == null) {
            BlockPos current = neighbourhood.next();

            // skip the middle and the fruit
            if (current.equals(stemPos) || current.equals(fruitPos)) {
                continue;
            }

            IBlockState blockState = worldIn.getBlockState(current);

            if (blockState.getBlock() instanceof BlockSapling) {
                fuelPos = current;
            }
        }

        return Optional.ofNullable(fuelPos);
    }

    protected boolean isHarvestable(Block fuelBlock) {
        return fuelBlock instanceof BlockCrops;
    }

    protected boolean isContainerFruit(Block block) {
        return block instanceof ContainerFruit || block instanceof BlockChest;
    }

    protected boolean isContainerFruit(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos).getBlock() instanceof ContainerFruit
                || worldIn.getBlockState(pos).getBlock() instanceof BlockChest;
    }
}
