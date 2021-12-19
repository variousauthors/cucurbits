package variousauthors.scaffold.item;

import net.minecraft.item.ItemFood;
import net.minecraftforge.oredict.OreDictionary;
import variousauthors.scaffold.Scaffold;

public class ItemCorn extends ItemFood {

    public ItemCorn() {
        super(3, 0.6f, false);
        setUnlocalizedName("corn");
        setRegistryName("corn");
        setCreativeTab(Scaffold.creativeTab);
    }

    public void registerItemModel() {
        Scaffold.proxy.registerItemRenderer(this, 0, getRegistryName());
    }

    public void initOreDict() {
        OreDictionary.registerOre("cropCorn", this);
    }

}
