package variousauthors.cucurbits.block;

import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import variousauthors.cucurbits.Cucurbits;
import variousauthors.cucurbits.item.ModItems;

public class BlockCropCorn extends BlockCrops {

    public BlockCropCorn() {
        setUnlocalizedName("crop_corn");
        setRegistryName("crop_corn");
        setCreativeTab(Cucurbits.creativeTab);
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