package variousauthors.scaffold.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSeeds;
import variousauthors.scaffold.Scaffold;
import variousauthors.scaffold.block.ModBlocks;

public class ItemCornSeed extends ItemSeeds {

    public ItemCornSeed() {
        super(ModBlocks.cropCorn, Blocks.FARMLAND);
        setUnlocalizedName("corn_seed");
        setCreativeTab(Scaffold.creativeTab);
        setRegistryName("corn_seed");
    }

    public void registerItemModel() {
        Scaffold.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

}
