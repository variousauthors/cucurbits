package variousauthors.cucurbits.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.oredict.OreDictionary;
import variousauthors.cucurbits.Cucurbits;

public class ItemCorn extends ItemFood {

    public ItemCorn() {
        super(3, 0.6f, false);
        setUnlocalizedName("corn");
        setRegistryName("corn");
        setCreativeTab(Cucurbits.creativeTab);
    }

    public void registerItemModel() {
        Cucurbits.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

    public void initOreDict() {
        OreDictionary.registerOre("cropCorn", this);
    }

}
