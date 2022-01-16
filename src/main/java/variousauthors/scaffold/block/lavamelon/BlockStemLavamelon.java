package variousauthors.scaffold.block.lavamelon;

import java.util.Iterator;
import java.util.Optional;
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
import sun.lwawt.macosx.CPrinterDevice;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.block.BlockStemCucurbit;

public class BlockStemLavamelon extends BlockStemCucurbit
{
    public BlockStemLavamelon(Block crop, String name)
    {
        super(crop, name);
    }

    @Override
    protected Optional<BlockPos> findFuelBlockInWorld(World worldIn, BlockPos stemPos, BlockPos fruitPos) {
        /** right now it scans the whole box,
         * but I would rather it randomly check a few
         * blocks per tick over a wider area */
        BlockPos from = stemPos.west().north().down(1);
        BlockPos to = stemPos.east().south().down(4);

        Iterator<BlockPos> neighbourhood = BlockPos.getAllInBox(from, to).iterator();
        BlockPos fuelPos = null;
        while (neighbourhood.hasNext() && fuelPos == null) {
            BlockPos current = neighbourhood.next();
            IBlockState blockState = worldIn.getBlockState(current);

            if (blockState.getBlock().equals(Blocks.LAVA)) {
                if (blockState.getValue(BlockLiquid.LEVEL) == 0) {
                    fuelPos = current;
                }
            }
        }

        return Optional.ofNullable(fuelPos);
    }
}
