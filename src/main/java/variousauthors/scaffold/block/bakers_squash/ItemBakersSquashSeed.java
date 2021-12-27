package variousauthors.scaffold.block.bakers_squash;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import variousauthors.scaffold.CanRegisterItemModel;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.block.ModBlocks;

public class ItemBakersSquashSeed extends ItemSeeds implements CanRegisterItemModel {
    public ItemBakersSquashSeed(String name) {
        super(ModBlocks.stemBakersSquash, Blocks.FARMLAND);
        setUnlocalizedName(name);
        setCreativeTab(Scaffold.creativeTab);
        setRegistryName(name);
    }
}
