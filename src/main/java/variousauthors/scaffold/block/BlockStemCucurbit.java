package variousauthors.scaffold.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

abstract public class BlockStemCucurbit extends BlockStem {

    public BlockStemCucurbit(Block crop, String name)
    {
        super(crop);

        setUnlocalizedName(name);
        setRegistryName(name);
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
    }

    @Nullable
    abstract protected BlockPos findFuelBlockInWorld(World worldIn, BlockPos stemPos);

    abstract protected boolean checkStemGrowthConditions(World worldIn, BlockPos pos);


}
