package variousauthors.cucurbits.item;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;
import variousauthors.cucurbits.Cucurbits;
import variousauthors.cucurbits.block.ModBlocks;

public class ItemCornSeed extends ItemSeeds {

    public ItemCornSeed() {
        super(ModBlocks.cropCorn, Blocks.FARMLAND);
        setUnlocalizedName("corn_seed");
        setCreativeTab(Cucurbits.creativeTab);
        setRegistryName("corn_seed");
    }

    public void registerItemModel() {
        Cucurbits.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

}
