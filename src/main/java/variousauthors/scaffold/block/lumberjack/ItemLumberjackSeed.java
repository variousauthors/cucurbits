package variousauthors.scaffold.block.lumberjack;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import variousauthors.scaffold.CanRegisterItemModel;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.block.ModBlocks;

public class ItemLumberjackSeed extends ItemSeeds implements CanRegisterItemModel {
    public ItemLumberjackSeed(String name) {
        super(ModBlocks.stemLumberjack, Blocks.FARMLAND);
        setUnlocalizedName(name);
        setCreativeTab(Scaffold.creativeTab);
        setRegistryName(name);
    }
}
