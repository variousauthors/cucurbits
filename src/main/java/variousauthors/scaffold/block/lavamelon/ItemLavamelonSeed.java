package variousauthors.scaffold.block.lavamelon;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import variousauthors.scaffold.CanRegisterItemModel;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.block.ModBlocks;

public class ItemLavamelonSeed extends ItemSeeds implements CanRegisterItemModel {
    public ItemLavamelonSeed() {
        super(ModBlocks.stemLavamelon, Blocks.FARMLAND);
        setUnlocalizedName("seeds_lavamelon");
        setCreativeTab(Scaffold.creativeTab);
        setRegistryName("seeds_lavamelon");
    }
}
