package variousauthors.scaffold.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.item.ModItems;

public class BlockCropCorn extends BlockCrops {

    public BlockCropCorn() {
        setUnlocalizedName("crop_corn");
        setRegistryName("crop_corn");
        setCreativeTab(Scaffold.creativeTab);
    }

    @Override
    protected Item getSeed() {
        return ModItems.cornSeed;
    }

    @Override
    protected Item getCrop() {
        return ModItems.corn;
    }

}