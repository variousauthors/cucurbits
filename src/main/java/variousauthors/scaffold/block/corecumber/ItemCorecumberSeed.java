package variousauthors.scaffold.block.corecumber;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import variousauthors.scaffold.CanRegisterItemModel;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.block.ModBlocks;

public class ItemCorecumberSeed extends ItemSeeds implements CanRegisterItemModel {
    public ItemCorecumberSeed(String name) {
        super(ModBlocks.stemCorecumber, Blocks.FARMLAND);
        setUnlocalizedName(name);
        setCreativeTab(Scaffold.creativeTab);
        setRegistryName(name);
    }
}
