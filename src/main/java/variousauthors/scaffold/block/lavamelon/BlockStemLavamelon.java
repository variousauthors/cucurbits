package variousauthors.scaffold.block.lavamelon;

import java.util.Iterator;
import java.util.Random;
import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import variousauthors.scaffold.Scaffold;

public class BlockStemLavamelon extends BlockBush implements IGrowable
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 7);
    public static final PropertyDirection FACING = BlockTorch.FACING;
    private final Block crop;
    protected static final AxisAlignedBB[] STEM_AABB = new AxisAlignedBB[] {new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.125D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.25D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.375D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.5D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.625D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.75D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 0.875D, 0.625D), new AxisAlignedBB(0.375D, 0.0D, 0.375D, 0.625D, 1.0D, 0.625D)};

    public BlockStemLavamelon(Block crop, String name)
    {
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(Scaffold.creativeTab);

        this.setDefaultState(this.blockState.getBaseState().withProperty(AGE, Integer.valueOf(0)).withProperty(FACING, EnumFacing.UP));
        this.crop = crop;
        this.setTickRandomly(true);
        this.setCreativeTab((CreativeTabs)null);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return STEM_AABB[((Integer)state.getValue(AGE)).intValue()];
    }

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        int i = ((Integer)state.getValue(AGE)).intValue();
        state = state.withProperty(FACING, EnumFacing.UP);

        for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
        {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop && i == 7)
            {
                state = state.withProperty(FACING, enumfacing);
                break;
            }
        }

        return state;
    }

    /**
     * Return true if the block can sustain a Bush
     */
    protected boolean canSustainBush(IBlockState state)
    {
        return state.getBlock() == Blocks.FARMLAND;
    }

    /** @TODO nabbed this from BlockCrops but I think
     * I should move it out to a utility. Maybe call it calculateGrowthChance */
    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
    {
        float f = 1.0F;
        BlockPos blockpos = pos.down();

        for (int i = -1; i <= 1; ++i)
        {
            for (int j = -1; j <= 1; ++j)
            {
                float f1 = 0.0F;
                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));

                if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)blockIn))
                {
                    f1 = 1.0F;

                    if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(i, 0, j)))
                    {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0)
                {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
        boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();

        if (flag && flag1)
        {
            f /= 2.0F;
        }
        else
        {
            boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();

            if (flag2)
            {
                f /= 2.0F;
            }
        }

        return f;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);

        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (checkStemGrowthConditions(worldIn, pos))
        {
            float f = getGrowthChance(this, worldIn, pos);

            if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0))
            {
                int i = ((Integer)state.getValue(AGE)).intValue();

                if (i < 7)
                {
                    IBlockState newState = state.withProperty(AGE, Integer.valueOf(i + 1));
                    worldIn.setBlockState(pos, newState, 2);
                }
                else
                {
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL)
                    {
                        if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this.crop)
                        {
                            return;
                        }
                    }

                    pos = pos.offset(EnumFacing.Plane.HORIZONTAL.random(rand));
                    IBlockState soil = worldIn.getBlockState(pos.down());
                    Block block = soil.getBlock();

                    if (worldIn.isAirBlock(pos) && (block.canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this) || block == Blocks.DIRT || block == Blocks.GRASS))
                    {
                        BlockPos fuelPos = findFuelBlockInWorld(worldIn, pos);

                        if (fuelPos != null) {
                            worldIn.setBlockState(fuelPos, Blocks.AIR.getDefaultState());
                            worldIn.setBlockState(pos, this.crop.getDefaultState());
                        }
                    }
                }
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
            }
        }
    }

    @Nullable
    private BlockPos findFuelBlockInWorld(World worldIn, BlockPos stemPos) {
        BlockPos from = stemPos.west().north().down(1);
        BlockPos to = stemPos.east().south().down(4);

        Iterator<BlockPos> neighbourhood = BlockPos.getAllInBox(from, to).iterator();
        BlockPos fuelPos = null;
        while (neighbourhood.hasNext() && fuelPos == null) {
            BlockPos current = neighbourhood.next();
            Block block = worldIn.getBlockState(current).getBlock();

            if (block == Blocks.LAVA) {
                fuelPos = current;
            }
        }

        return fuelPos;
    }

    private boolean checkStemGrowthConditions(World worldIn, BlockPos pos) {
        return worldIn.getLightFromNeighbors(pos.up()) >= 9;
    }

    public void growStem(World worldIn, BlockPos pos, IBlockState state)
    {
        int i = ((Integer)state.getValue(AGE)).intValue() + MathHelper.getInt(worldIn.rand, 2, 5);
        worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(Math.min(7, i))), 2);
    }

    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
    }

    @Override
    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        {
            Item item = this.getSeedItem();

            if (item != null)
            {
                int i = ((Integer)state.getValue(AGE)).intValue();

                for (int j = 0; j < 3; ++j)
                {
                    if (RANDOM.nextInt(15) <= i)
                    {
                        drops.add(new ItemStack(item));
                    }
                }
            }
        }
    }

    @Nullable
    protected Item getSeedItem()
    {
        if (this.crop == Blocks.PUMPKIN)
        {
            return Items.PUMPKIN_SEEDS;
        }
        else
        {
            return this.crop == Blocks.MELON_BLOCK ? Items.MELON_SEEDS : null;
        }
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        Item item = this.getSeedItem();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }

    /**
     * Whether this IGrowable can grow
     */
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return ((Integer)state.getValue(AGE)).intValue() != 7;
    }

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        this.growStem(worldIn, pos, state);
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(AGE)).intValue();
    }

    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {AGE, FACING});
    }
}
